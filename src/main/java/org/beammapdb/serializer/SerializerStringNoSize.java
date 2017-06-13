package org.beammapdb.serializer;

import org.beammapdb.DataOutput2;
import org.beammapdb.Serializer;
import org.beammapdb.DataInput2;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by jan on 2/28/16.
 */
public class SerializerStringNoSize implements Serializer<String> {

    private final Charset UTF8_CHARSET = Charset.forName("UTF8");

    @Override
    public void serialize(DataOutput2 out, String value) throws IOException {
        final byte[] bytes = value.getBytes(UTF8_CHARSET);
        out.write(bytes);
    }


    @Override
    public String deserialize(DataInput2 in, int available) throws IOException {
        if (available == -1) throw new IllegalArgumentException("STRING_NOSIZE does not work with collections.");
        byte[] bytes = new byte[available];
        in.readFully(bytes);
        return new String(bytes, UTF8_CHARSET);
    }

    @Override
    public boolean isTrusted() {
        return true;
    }

    @Override
    public boolean needsAvailableSizeHint() {
        return true;
    }

}
