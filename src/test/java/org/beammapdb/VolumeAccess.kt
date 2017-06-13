package org.beammapdb.VolumeAccess

import org.beammapdb.volume.Volume
import org.fest.reflect.core.Reflection
import org.beammapdb.volume.*

val Volume.sliceShift: Int
    get() = Reflection.field("sliceShift").ofType(Int::class.java).`in`(this).get()

