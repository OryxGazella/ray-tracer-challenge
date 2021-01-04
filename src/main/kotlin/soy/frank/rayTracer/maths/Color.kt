package soy.frank.rayTracer.maths

import kotlin.math.roundToInt

class Color {
    private val tuple: Tuple

    constructor(red: Float, green: Float, blue: Float) {
        tuple = Tuple.vector(red, green, blue)
    }

    operator fun plus(other: Color) = Color(other.tuple + tuple)
    operator fun minus(other: Color) = Color(tuple - other.tuple)
    operator fun times(scalar: Float) = Color(tuple.times(scalar))
    operator fun times(other: Color) = Color(
        red * other.red,
        green * other.green,
        blue * other.blue
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Color

        if (tuple != other.tuple) return false

        return true
    }

    override fun hashCode(): Int {
        return tuple.hashCode()
    }

    override fun toString(): String {
        return "Color(red=$red, green=$green, blue=$blue)"
    }

    fun asPixMap(): String =
        "${(clamp(red) * 255).roundToInt()} ${(clamp(green) * 255).roundToInt()} ${(clamp(blue) * 255).roundToInt()}"


    constructor(fromTuple: Tuple) {
        tuple = fromTuple
    }

    val red: Float
        get() = tuple.x
    val green: Float
        get() = tuple.y
    val blue: Float
        get() = tuple.z

    companion object {
        val black = Color(0f, 0f, 0f)

        private fun clamp(toClamp: Float): Float {
            if (toClamp > 1f) return 1f
            if (toClamp < 0f) return 0f
            return toClamp
        }
    }
}