package org.beammapdb

import org.junit.Test
import org.junit.Assert.*

class PumpTest{

    val org.mapdb.Pump.Sink<*, *>.rootRecidRecid:  Long
        get() = org.fest.reflect.core.Reflection.method("getRootRecidRecid").`in`(this).invoke() as  Long


    @Test fun single(){
        check((1..6).map{Pair(it, it*2)})
    }

    @Test fun cent(){
        check((1..100).map{Pair(it, it*2)})
    }

    @Test fun kilo(){
        check((1..1000).map{Pair(it, it*2)})
    }

    @Test fun multi(){
        if(org.mapdb.TT.shortTest())
            return
        for(limit in 0 .. 1000) {
            check((0 .. limit).map { Pair(it, it * 2) })
        }
    }


    @Test fun mega(){
        check((1..1000000).map{Pair(it, it*2)})
    }

    @Test(expected = org.mapdb.DBException.NotSorted::class)
    fun notSorted(){
        check((6 downTo 1).map{Pair(it, it*2)})
    }

    private fun check(source: List<Pair<Int, Int>>) {
        val store = StoreTrivial()
        val taker = org.mapdb.Pump.treeMap(
                store = store,
                keySerializer = Serializer.INTEGER,
                valueSerializer = Serializer.INTEGER,
                dirNodeSize = 10,
                leafNodeSize = 10
        )
        taker.putAll(source)
        taker.create()

        val root = taker.rootRecidRecid
                ?: throw AssertionError()
        assertNotEquals(0L, root)

        val map = org.mapdb.BTreeMap.Companion.make(
                store = store,
                rootRecidRecid = root,
                valueSerializer = Serializer.INTEGER,
                keySerializer = Serializer.INTEGER)
//        map.printStructure(System.out)
        map.verify()

        assertEquals(source.size, map.size)
        source.forEach {
            assertEquals(it.second, map[it.first])
        }

    }
}
