package soy.frank.rayTracer.maths

data class Ray(val origin: Tuple, val direction: Tuple) {
    fun position(time: Float) = origin + time * direction
}
