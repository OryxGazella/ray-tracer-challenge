package soy.frank.rayTracer.maths

operator fun Float.times(tuple: Tuple) = tuple.times(this)
operator fun Float.times(color: Color) = color * this