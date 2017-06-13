package org.beammapdb.serializer;

import org.beammapdb.DataOutput2;
import org.beammapdb.DataInput2;

import java.io.IOException;

/**
 * Created by jan on 2/28/16.
 */
public class SerializerChar extends GroupSerializerObjectArray<Character> {
    @Override
    public void serialize(DataOutput2 out, Character value) throws IOException {
        out.writeChar(value.charValue());
    }

    @Override
    public Character deserialize(DataInput2 in, int available) throws IOException {
        return in.readChar();
    }

    @Override
    public int fixedSize() {
        return 2;
    }

    @Override
    public boolean isTrusted() {
        return true;
    }

    //TODO value array
}
