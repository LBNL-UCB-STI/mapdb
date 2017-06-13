package org.beammapdb

import org.beammapdb.jsr166Tests.CopyOnWriteArrayListTest

class IndexTreeList_JSR166Test: CopyOnWriteArrayListTest(){

    override fun emptyArray():MutableList<Int?> {
        val store = org.mapdb.StoreDirect.Companion.make();
        val index = org.mapdb.IndexTreeLongLongMap.Companion.make(store)
        val list = IndexTreeList(store = store, serializer= Serializer.INTEGER, isThreadSafe = true,
                map =index, counterRecid =  store.put(0L, Serializer.LONG_PACKED))
        return list
    }

}