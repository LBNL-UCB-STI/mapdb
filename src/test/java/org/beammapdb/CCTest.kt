package org.beammapdb

import org.junit.Test
import org.junit.Assert.assertEquals

class CCTest{
    @Test fun constants(){
        assertEquals(org.mapdb.CC.PAGE_SIZE, 1L shl CC.PAGE_SHIFT)
    }
}