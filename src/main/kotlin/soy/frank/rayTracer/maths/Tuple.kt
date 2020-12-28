package soy.frank.rayTracer.maths

import kotlin.math.pow
import kotlin.math.sqrt

data class Tuple(val x: Float, val y: Float, val z: Float, val w: Float) {
    val type = if (w == 0.0f) Type.Vector else Type.Point

    enum class Type {
        Point,
        Vector
    }

    companion object {
        fun point(x: Float, y: Float, z: Float) = Tuple(x, y, z, 1.0f)
        fun vector(x: Float, y: Float, z: Float) = Tuple(x, y, z, 0.0f)
    }

    operator fun plus(other: Tuple) = Tuple(x + other.x, y + other.y, z + other.z, w + other.w)
    operator fun minus(other: Tuple) = Tuple(x - other.x, y - other.y, z - other.z, w - other.w)
    operator fun unaryMinus() = Tuple(-x, -y, -z, -w)
    operator fun times(scalar: Float) = Tuple(x * scalar, y * scalar, z * scalar, w * scalar)
    operator fun div(scalar: Float) = times(1 / scalar)
    fun magnitude() = sqrt(x.pow(2) + y.pow(2) + z.pow(2) + w.pow(2))
    fun normalize() = div(magnitude())
    infix fun dot(other: Tuple) = x * other.x + y * other.y + z * other.z + w * other.w
    infix fun cross(other: Tuple) = Tuple(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x,
            0f)
}