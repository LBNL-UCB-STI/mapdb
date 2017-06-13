package org.beammapdb.issues;


import org.beammapdb.Serializer;
import org.beammapdb.volume.MappedFileVol;
import org.junit.Test;
import org.beammapdb.SortedTableMap;
import org.beammapdb.TestWithTempDir;
import org.beammapdb.volume.Volume;

import java.util.Map;

public class Issue_815 extends TestWithTempDir{

    @Test
    public void sliceSize_reopen(){
        String file = tempFile().getPath();
        Volume volume = MappedFileVol.FACTORY.makeVolume(file, false,0L,22,0,false);
        SortedTableMap.Sink<Integer, String> sink = SortedTableMap
                .create(volume, Serializer.INTEGER, // key serializer
                        Serializer.STRING) // value serializer
                .pageSize(4 * 1024 * 1024) // set Page Size to 4MB
                .nodeSize(8) // set Node Size to 8 entries
                .createFromSink();

        for(int i=0;i<1e6;i++){
            sink.put(i, ""+i);
        }

        Map m = sink.create();


    }


}
