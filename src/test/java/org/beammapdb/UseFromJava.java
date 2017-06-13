package org.beammapdb;

import org.junit.Test;

/**
 * Tests jave interoperability
 */
public class UseFromJava {
    @Test
    public void basic_store() {
        StoreTrivial st = new StoreTrivial();
        st.put(1L, Serializer.LONG);
        st.close();
    }
}