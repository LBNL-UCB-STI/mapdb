package org.beammapdb

import org.beammapdb.jsr166Tests.ConcurrentSkipListSubMapTest
import org.beammapdb.jsr166Tests.JSR166Test
import java.util.concurrent.ConcurrentNavigableMap

class SortedTableMap_ConcurrentSkipListSubMapTest_JSR166Test()
    : ConcurrentSkipListSubMapTest()
{


    protected override fun map5(): ConcurrentNavigableMap<*, *>? {
        val consumer = org.mapdb.SortedTableMap.Companion.createFromSink(
                keySerializer = Serializer.INTEGER,
                valueSerializer = Serializer.STRING_INTERN,
                volume = CC.DEFAULT_MEMORY_VOLUME_FACTORY.makeVolume(null, false))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.zero, "Z"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.one, "A"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.two, "B"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.three, "C"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.four, "D"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.five, "E"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.seven, "F"))

        val map =  consumer.create()
        assertFalse(map.isEmpty())
        assertEquals(7, map.size.toLong())
        return map.subMap(org.mapdb.jsr166Tests.JSR166Test.one, true, JSR166Test.seven, false)
    }

    protected override fun dmap5(): ConcurrentNavigableMap<*, *>? {
        val consumer = org.mapdb.SortedTableMap.Companion.createFromSink(
                keySerializer = Serializer.INTEGER,
                valueSerializer = Serializer.STRING_INTERN,
                volume = CC.DEFAULT_MEMORY_VOLUME_FACTORY.makeVolume(null, false))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.m5, "E"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.m4, "D"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.m3, "C"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.m2, "B"))
        consumer.put(Pair(org.mapdb.jsr166Tests.JSR166Test.m1, "A"))

        val map = consumer.create().descendingMap()
        assertFalse(map.isEmpty())
        assertEquals(5, map.size.toLong())
        return map
    }


    override fun emptyMap(): ConcurrentNavigableMap<Int, String>? {
        return org.mapdb.SortedTableMap.Companion.createFromSink(
                keySerializer = Serializer.INTEGER,
                valueSerializer = Serializer.STRING_INTERN,
                volume = CC.DEFAULT_MEMORY_VOLUME_FACTORY.makeVolume(null, false))
                .create()
    }


    override protected fun isReadOnly(): Boolean {
        return true
    }


}
