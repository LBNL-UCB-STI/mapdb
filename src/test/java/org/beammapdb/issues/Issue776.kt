package org.beammapdb.issues

import org.junit.Test
import org.beammapdb.DBMaker
import org.beammapdb.TT

class Issue776{

    @Test fun stack_overflow_in_shutdown_hook(){

        val f = org.mapdb.TT.tempFile()
        val db = org.mapdb.DBMaker.fileDB(f)
                .fileMmapEnable()
                .fileMmapPreclearDisable()
                .cleanerHackEnable()
                .concurrencyDisable()
                .closeOnJvmShutdown()
                .make()
        db.close()

    }

}