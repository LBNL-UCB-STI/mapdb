package org.beammapdb


import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class HTreeMap_Expiration_Multithreaded {

    internal val duration = 10 * 60 * 1000.toLong()
    internal var b = ByteArray(100)

    @Test fun expireUUID() {
        if (org.mapdb.TT.shortTest())
            return

        val endTime = duration + System.currentTimeMillis()

        val db = org.mapdb.DBMaker.memoryDB().make()
        val m = db.hashMap("aa")
                .keySerializer(org.mapdb.Serializer.UUID)
                .valueSerializer(org.mapdb.Serializer.BYTE_ARRAY)
                .expireAfterCreate(20, TimeUnit.SECONDS)
                .expireExecutor(org.mapdb.TT.executor(3))
                .create()

        org.mapdb.TT.fork(10) {
            var r = Random(1)
            run {
                var i = 0
                while (i < 2e5) {
                    val u = UUID(r.nextLong(), r.nextLong())
                    m.put(u, b)
                    i++
                }
            }

            while (System.currentTimeMillis() < endTime) {
                r = Random(1)
                run {
                    var i = 0
                    while (i < 1e5) {
                        val u = UUID(r.nextLong(), r.nextLong())
                        m.get(u)
                        m.put(u, b)
                        i++
                    }
                }
                var i = 1e5.toInt()
                while (i < 2e5) {
                    val u = UUID(r.nextLong(), r.nextLong())
                    m[u]
                    i++
                }
            }
        }
    }

    @Test fun expiration_with_size(){
        if(org.mapdb.TT.shortTest()) return

        val db = org.mapdb.DBMaker.memoryDB().make()
        val m = db
                .hashMap("map", Serializer.JAVA, Serializer.JAVA)
                .expireAfterCreate(100)
                .expireExecutor(org.mapdb.TT.executor(1))
                .expireExecutorPeriod(1)
                .layout(1,4,4)
                .createOrOpen()

        for(i in 0 until 1e7.toInt()){
            m.put("london"+i, "aa")
//            if(i%10000==0)
//                Iterators.size(m.iterator())
        }
    }
}
