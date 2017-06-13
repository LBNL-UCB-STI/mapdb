package org.beammapdb.serializer;

import org.beammapdb.DataOutput2;
import org.beammapdb.DataInput2;

import java.io.IOException;

/**
 * Created by jan on 2/28/16.
 */
public class SerializerIllegalAccess extends GroupSerializerObjectArray<Object> {
    @Override
    public void serialize(DataOutput2 out, Object value) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public Object deserialize(DataInput2 in, int available) throws IOException {
        throw new IllegalAccessError();
    }

    @Override
    public boolean isTrusted() {
        return true;
    }

}
