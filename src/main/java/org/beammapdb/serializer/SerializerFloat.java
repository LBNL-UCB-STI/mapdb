package org.beammapdb.serializer;

import org.beammapdb.DataOutput2;
import org.beammapdb.DataInput2;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jan on 2/28/16.
 */
public class SerializerFloat extends SerializerFourByte<Float> {

    @Override
    protected Float unpack(int l) {
        return new Float(Float.intBitsToFloat(l));
    }

    @Override
    protected int pack(Float l) {
        return Float.floatToIntBits(l);
    }


    @Override
    public void serialize(DataOutput2 out, Float value) throws IOException {
        out.writeFloat(value);
    }

    @Override
    public Float deserialize(DataInput2 in, int available) throws IOException {
        return new Float(in.readFloat());
    }


    @Override
    public int valueArraySearch(Object keys, Float key) {
        //TODO PERF this can be optimized, but must take care of NaN
        return Arrays.binarySearch(valueArrayToArray(keys), key);
    }


}
