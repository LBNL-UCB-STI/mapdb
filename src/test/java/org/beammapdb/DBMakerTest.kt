package org.beammapdb

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mapdb.VolumeAccess.sliceShift
import org.beammapdb.volume.ByteArrayVol
import org.beammapdb.volume.FileChannelVol
import org.beammapdb.volume.RandomAccessFileVol

class DBMakerTest{

    @Rule @JvmField
    val expectedException = ExpectedException.none()!!

    @Test fun sharded_htreemap_close(){
        val executor = org.mapdb.TT.executor()

        val map = org.mapdb.DBMaker.heapShardedHashMap(8).expireExecutor(executor).expireAfterCreate(100).create()
        assertTrue(executor.isShutdown.not())
        map.close()
        assertTrue(executor.isShutdown)
        assertTrue(executor.isTerminated)
    }

    @Test fun conc_scale(){
        val db = org.mapdb.DBMaker.memoryDB().concurrencyScale(32).make()
        assertEquals(org.mapdb.DataIO.shift(32), (db.getStore() as StoreDirect).concShift)
    }


    @Test fun conc_disable(){
        var db = org.mapdb.DBMaker.memoryDB().make()
        assertTrue(db.isThreadSafe)
        assertTrue(db.getStore().isThreadSafe)
        assertTrue(db.hashMap("aa1").create().isThreadSafe)
        assertTrue(db.treeMap("aa2").create().isThreadSafe)

        db = org.mapdb.DBMaker.memoryDB().concurrencyDisable().make()
        assertFalse(db.isThreadSafe)
        assertFalse(db.getStore().isThreadSafe)
        assertFalse(db.hashMap("aa1").create().isThreadSafe)
        assertFalse(db.treeMap("aa2").create().isThreadSafe)
    }

    @Test fun raf(){
        val file = org.mapdb.TT.tempFile()
        val db = org.mapdb.DBMaker.fileDB(file).make()
        assertTrue((db.getStore() as StoreDirect).volumeFactory == RandomAccessFileVol.FACTORY)
        file.delete()
    }

    @Test fun channel(){
        val file = org.mapdb.TT.tempFile()
        val db = org.mapdb.DBMaker.fileDB(file).fileChannelEnable().make()
        assertTrue((db.getStore() as StoreDirect).volumeFactory == FileChannelVol.FACTORY)
        file.delete()
    }


    @Test fun mmap(){
        val file = org.mapdb.TT.tempFile()
        val db = org.mapdb.DBMaker.fileDB(file).fileMmapEnable().make()
        assertTrue((db.getStore() as StoreDirect).volumeFactory is org.mapdb.volume.MappedFileVol.MappedFileFactory)
        file.delete()
    }


    @Test fun mmap_if_supported(){
        val file = org.mapdb.TT.tempFile()
        val db = org.mapdb.DBMaker.fileDB(file).fileChannelEnable().fileMmapEnableIfSupported().make()
        if(DataIO.JVMSupportsLargeMappedFiles())
            assertTrue((db.getStore() as StoreDirect).volumeFactory is org.mapdb.volume.MappedFileVol.MappedFileFactory)
        else
            assertTrue((db.getStore() as StoreDirect).volumeFactory == FileChannelVol.FACTORY)

        file.delete()
    }


    @Test fun readonly_vol(){
        val f = org.mapdb.TT.tempFile()
        //fill with content
        var db = org.mapdb.DBMaker.fileDB(f).make()
        db.atomicInteger("aa",1)
        db.close()

        fun checkReadOnly(){
            assertTrue(((db.getStore()) as StoreDirect).volume.isReadOnly)
            org.mapdb.TT.assertFailsWith(UnsupportedOperationException::class.java) {
                db.hashMap("zz").create()
            }
        }

        db = org.mapdb.DBMaker.fileDB(f).readOnly().make()
        checkReadOnly()
        db.close()

        db = org.mapdb.DBMaker.fileDB(f).readOnly().fileChannelEnable().make()
        checkReadOnly()
        db.close()

        db = org.mapdb.DBMaker.fileDB(f).readOnly().fileMmapEnable().make()
        checkReadOnly()
        db.close()

        f.delete()
    }

    @Test fun checksumStore(){
        val db = org.mapdb.DBMaker.memoryDB().checksumStoreEnable().make()
        assertTrue(((db.getStore()) as StoreDirect).checksum)
    }

    @Test(timeout=10000)
    fun file_lock_wait(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).make()
        org.mapdb.TT.fork {
            Thread.sleep(2000)
            db1.close()
        }
        val db2 = org.mapdb.DBMaker.fileDB(f).fileLockWait(6000).make()
        db2.close()
        f.delete()
    }


    @Test(timeout=10000)
    fun file_lock_wait2(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).make()
        org.mapdb.TT.fork {
            Thread.sleep(2000)
            db1.close()
        }
        val db2 = org.mapdb.DBMaker.fileDB(f).fileLockWait().make()
        db2.close()
        f.delete()
    }

    @Test(timeout=10000)
    fun file_lock_wait_time_out_same_jvm() {
        val f = org.mapdb.TT.tempFile()

        val db1 = org.mapdb.DBMaker.fileDB(f)
                .make()

        try {
            expectedException.expect(org.mapdb.DBException.FileLocked::class.java)
            org.mapdb.DBMaker.fileDB(f)
                    .fileLockWait(2000)
                    .make()
        } finally {
            db1.close()
            f.delete()
        }
    }

    @Test(timeout=30000)
    fun file_lock_wait_time_out_different_jvm() {
        val f = org.mapdb.TT.tempFile()
        val process = org.mapdb.TT.forkJvm(ForkedLockTestMain::class.java, f.absolutePath)

        // Wait for the forked process to write to STDOUT, which happens after it
        // has successfully opened and locked the database.
        process.inputStream.read()

        try {
            expectedException.expect(org.mapdb.DBException.FileLocked::class.java)
            org.mapdb.DBMaker.fileDB(f)
                    .fileLockWait(2000)
                    .make()
        } finally {
            if(!process.isAlive) {
                fail(process.errorStream.reader().readText())
            } else {
                process.destroyForcibly()
                f.delete()
            }
        }
    }

    @Test fun file_lock_disable_RAF(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).make()
        org.mapdb.DBMaker.fileDB(f).fileLockDisable().make()
    }

    @Test fun file_lock_disable_RAF2(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).transactionEnable().make()
        org.mapdb.DBMaker.fileDB(f).fileLockDisable().transactionEnable().make()
    }

    @Test fun file_lock_disable_Channel(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).make()
        org.mapdb.DBMaker.fileDB(f).fileLockDisable().make()
    }

    @Test fun file_lock_disable_Channel2(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).fileChannelEnable().transactionEnable().make()
        org.mapdb.DBMaker.fileDB(f).fileChannelEnable().fileLockDisable().transactionEnable().make()
    }

    @Test fun file_lock_disable_mmap(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).fileMmapEnable().make()
        org.mapdb.DBMaker.fileDB(f).fileLockDisable().make()
    }

    @Test fun file_lock_disable_mmap2(){
        val f = org.mapdb.TT.tempFile()
        val db1 = org.mapdb.DBMaker.fileDB(f).transactionEnable().make()
        org.mapdb.DBMaker.fileDB(f).fileLockDisable().fileMmapEnable().transactionEnable().make()
    }

    @Test fun fileIncrement(){
        val db = org.mapdb.DBMaker.memoryDB().allocateIncrement(100).make()
        val store = db.getStore() as StoreDirect
        val volume = store.volume as ByteArrayVol
        assertEquals(org.mapdb.CC.PAGE_SHIFT, volume.sliceShift)
    }


    @Test fun fileIncrement2(){
        val db = org.mapdb.DBMaker.memoryDB().allocateIncrement(2*1024*1024).make()
        val store = db.getStore() as StoreDirect
        val volume = store.volume as ByteArrayVol
        assertEquals(1+ CC.PAGE_SHIFT, volume.sliceShift)
    }


    @Test fun fromVolume(){
        val vol = ByteArrayVol()
        val db = org.mapdb.DBMaker.volumeDB(vol, false).make()
        assertTrue(vol === (db.getStore() as StoreDirect).volume)
    }

    object ForkedLockTestMain {
        @JvmStatic
        fun main(args : Array<String>) {
            if(args.size != 1) {
                System.err.println("No database specified!")
                System.exit(3)
            }

            val file = args[0]
            val db1 = org.mapdb.DBMaker.fileDB(file).make()
            System.out.println("Locked database.")
            Thread.sleep(60000)
            db1.close()
        }
    }
}
