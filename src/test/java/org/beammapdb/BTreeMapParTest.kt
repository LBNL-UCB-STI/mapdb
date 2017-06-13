package org.beammapdb

import org.junit.Test

import java.util.concurrent.atomic.AtomicLong

import org.junit.Assert.assertEquals

class BTreeMapParTest {


    internal var scale = org.mapdb.TT.testScale()
    internal val threadNum = 6 * scale
    internal val max = 1e6.toInt() * scale

    @Test
    @Throws(InterruptedException::class)
    fun parInsert() {
        if (scale == 0)
            return


        val m = org.mapdb.DBMaker.memoryDB().make().treeMap("test").valueSerializer(org.mapdb.Serializer.LONG).keySerializer(org.mapdb.Serializer.LONG).make()

        val t = System.currentTimeMillis()
        val counter = AtomicLong()

        org.mapdb.TT.fork(threadNum, { core ->
            var n: Long = core.toLong()
            while (n < max) {
                m.put(n, n)
                n += threadNum.toLong()
            }
        })

        //        System.out.printf("  Threads %d, time %,d\n",threadNum,System.currentTimeMillis()-t);


        assertEquals(max.toLong(), m.size.toLong())
    }
}
