package org.beammapdb.volume

import org.junit.Assert.*
import org.junit.Test
import org.beammapdb.crash.CrashJVM
import org.beammapdb.TT
import java.io.File

/**
 * Created by jan on 3/10/16.
 */
class FileCrashTestr: CrashJVM(){


    override fun verifySeed(startSeed: Long, endSeed: Long, params:String): Long {
        val seed = endSeed
        assertTrue(File(getTestDir(), "" + seed).exists())
        val f = File(getTestDir(), "/" + seed)
        assertTrue(f.exists())

        return Math.max(startSeed,endSeed)+1;
    }

    override fun doInJVM(startSeed: Long, params:String) {
        var seed = startSeed;

        while(true){
            seed++
            startSeed(seed)

            val f = File(getTestDir(), "/" + seed)
            f.createNewFile()
            commitSeed(seed)
        }
    }

    @Test fun test(){
        val runtime = 4000L + org.mapdb.TT.testScale() *60*1000;
        val start = System.currentTimeMillis()
        org.mapdb.crash.CrashJVM.Companion.run(this, time = runtime, killDelay = 200)
        assertTrue(System.currentTimeMillis() - start >= runtime)
    }
}
