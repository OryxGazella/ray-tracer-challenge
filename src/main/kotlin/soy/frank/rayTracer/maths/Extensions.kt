package soy.frank.rayTracer.maths

operator fun Float.times(tuple: Tuple): Tuple {
    return tuple.times(this)
}