package org.beammapdb

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.beammapdb.jsr166Tests.ConcurrentSkipListSubMapTest
import java.util.concurrent.ConcurrentNavigableMap

@RunWith(Parameterized::class)
class BTreeMap_ConcurrentSkipListSubMapTest_JSR166Test(
        val mapMaker:(generic:Boolean)-> ConcurrentNavigableMap<Int, String>
) : ConcurrentSkipListSubMapTest()
{

    override fun emptyMap(): ConcurrentNavigableMap<Int, String>? {
        return mapMaker(false)
    }


    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun params(): Iterable<Any> {
            return org.mapdb.BTreeMap_ConcurrentMap_GuavaTest.Companion.params()
        }
    }

}
