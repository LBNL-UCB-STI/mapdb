package org.beammapdb

import org.beammapdb.jsr166Tests.ConcurrentSkipListSetTest

/**
 * Created by jan on 4/2/16.
 */
class BTreeKeySet_JSR166_ConcurrentSkipListSetTest: ConcurrentSkipListSetTest(){

    override fun emptySet() = org.mapdb.DBMaker.memoryDB().make()
            .treeSet("aa").serializer(org.mapdb.Serializer.INTEGER).create()
}