package org.beammapdb

import org.junit.Assert.*
import org.junit.Test
import org.mapdb.StoreAccess.cacheRecords
import java.io.File
import java.io.RandomAccessFile

/**
 * Created by jan on 3/22/16.
 */
class StoreWALTest: StoreDirectAbstractTest() {

    override fun openStore(file: File): StoreWAL {
        return org.mapdb.StoreWAL.Companion.make(file = file.path)
    }

    override fun openStore(): StoreWAL {
        return org.mapdb.StoreWAL.Companion.make()
    }


    @Test override fun delete_after_close(){
        val dir = org.mapdb.TT.tempDir()
        val store = org.mapdb.StoreWAL.Companion.make(dir.path + "/aa", fileDeleteAfterClose = true)
        store.put(11, Serializer.INTEGER)
        store.commit()
        store.put(11, Serializer.INTEGER)
        store.commit()
        assertNotEquals(0, dir.listFiles().size)
        store.close()
        assertEquals(0, dir.listFiles().size)
    }

    @Test(expected= org.mapdb.DBException.WrongConfiguration::class)
    fun checksum_disabled(){
        org.mapdb.StoreWAL.Companion.make(checksum = true)
    }

    @Test fun no_head_checksum(){
        var store = org.mapdb.StoreWAL.Companion.make(checksumHeader = false)
        assertEquals(0, store.volume.getInt(16)) //features
        assertEquals(0, store.volume.getInt(20)) //checksum

        store = org.mapdb.StoreWAL.Companion.make(checksumHeader = true)
        assertEquals(1, store.volume.getInt(16)) //features
        assertNotEquals(0, store.volume.getInt(20)) //checksum

    }

    @Test fun headers2(){
        val f = org.mapdb.TT.tempFile()
        val store = openStore(f)
        store.put(org.mapdb.TT.randomByteArray(1000000), Serializer.BYTE_ARRAY)

        val raf = RandomAccessFile(f.path, "r");
        raf.seek(0)
        assertEquals(org.mapdb.CC.FILE_HEADER.toInt(), raf.readUnsignedByte())
        assertEquals(org.mapdb.CC.FILE_TYPE_STOREDIRECT.toInt(), raf.readUnsignedByte())
        assertEquals(0, raf.readChar().toInt())
        raf.close()

        val wal = RandomAccessFile(f.path + ".wal.0", "r");
        wal.seek(0)
        assertEquals(org.mapdb.CC.FILE_HEADER.toInt(), wal.readUnsignedByte())
        assertEquals(org.mapdb.CC.FILE_TYPE_STOREWAL_WAL.toInt(), wal.readUnsignedByte())
        assertEquals(0, wal.readChar().toInt())
        wal.close()
        f.delete()
    }


    @Test fun updateCached(){
        val sizes = intArrayOf(6,20,200,4000,16000, 50000, 70000, 1024*1024*2)
        for(size in sizes) {
            val store = openStore()
            store.commit()
            val recid = store.put(ByteArray(size), Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 < store.cacheRecords.map { it.size() }.sum())
            store.update(recid, null, Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 == store.cacheRecords.map { it.size() }.sum())
        }

        for(size in sizes) {
            val store = openStore()
            store.commit()
            val recid = store.put(ByteArray(size), Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 < store.cacheRecords.map { it.size() }.sum())
            store.update(recid, ByteArray(1), Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 == store.cacheRecords.map { it.size() }.sum())
        }

        for(size in sizes) {
            val store = openStore();
            store.commit()
            val recid = store.put(ByteArray(size), Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 < store.cacheRecords.map { it.size() }.sum())
            store.delete(recid, Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 == store.cacheRecords.map { it.size() }.sum())
        }

        for(size in sizes) {
            val store = openStore();
            store.commit()
            val v = ByteArray(size)
            val recid = store.put(v, Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 < store.cacheRecords.map { it.size() }.sum())
            store.compareAndSwap(recid, v, null, Serializer.BYTE_ARRAY_NOSIZE)
            assertTrue(0 == store.cacheRecords.map { it.size() }.sum())
        }

    }
}