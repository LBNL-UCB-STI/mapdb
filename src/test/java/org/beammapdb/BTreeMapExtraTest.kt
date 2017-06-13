package org.beammapdb

class BTreeMapExtraTest: org.mapdb.MapExtraTest(){

    override fun makeMap(): MapExtra<Int?, String?> {
        return org.mapdb.BTreeMap.Companion.make(keySerializer = Serializer.INTEGER, valueSerializer = Serializer.STRING)
    }

}

