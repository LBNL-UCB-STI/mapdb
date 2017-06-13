package org.beammapdb

import org.beammapdb.jsr166Tests.ConcurrentHashMapV8Test
import java.util.concurrent.ConcurrentMap

/**
 * Created by jan on 4/2/16.
 */
class HtreeMapV8Test: ConcurrentHashMapV8Test() {

    override fun newMap(): ConcurrentMap<*, *>? {
        return org.mapdb.HTreeMap.Companion.make<Any, Any>()
    }

    override fun newMap(size: Int): ConcurrentMap<*, *>? {
        return newMap()
    }

}