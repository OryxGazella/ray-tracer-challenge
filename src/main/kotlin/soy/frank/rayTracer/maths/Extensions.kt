package soy.frank.rayTracer.soy.frank.rayTracer.maths

import soy.frank.rayTracer.maths.Tuple

operator fun Float.times(tuple: Tuple): Tuple {
    return tuple.times(this)
}