package soy.frank.rayTracer.maths

class Color {
    private val tuple : Tuple
    constructor(red: Float, green : Float, blue: Float) {
        tuple = Tuple.vector(red, green, blue)
    }

    operator fun plus(other: Color) = Color(other.tuple + tuple)
    operator fun minus(other: Color) = Color(tuple - other.tuple)
    operator fun times(scalar: Float) = Color(tuple.times(scalar))
    operator fun times(other: Color) = Color(
            red * other.red,
            green * other.green,
            blue * other.blue)

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


    constructor(fromTuple: Tuple) {
        tuple = fromTuple
    }

    val red: Float
        get() = tuple.x
    val green: Float
        get() = tuple.y
    val blue: Float
        get() = tuple.z
}