package soy.frank.rayTracer.maths

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TupleTest {

    @Test
    fun plus() {
        val tuple1 = Tuple(1f, 2f, 3f, 4f)
        val tuple2 = Tuple(5f, 6f, 7f, 8f)

        val sum = tuple1 + tuple2

        assertEquals(Tuple(6f, 8f, 10f, 12f), sum)
    }

    @Test
    fun minus() {
        val tuple1 = Tuple(5f, 6f, 7f, 8f)
        val tuple2 = Tuple(1f, 2f, 3f, 4f)

        val difference = tuple1 - tuple2

        assertEquals(Tuple(4f, 4f, 4f, 4f), difference)
    }

    @Test
    fun unaryMinus() {
        val tuple = Tuple(1f, 2f, 3f, 4f)

        assertEquals(Tuple(-1f, -2f, -3f, -4f), -tuple)
    }

    @Test
    fun times_float() {
        val vec = Tuple.vector(1f, 2f, 3f)

        assertEquals(Tuple(3f, 6f, 9f, 0f), vec * 3f)
        assertEquals(Tuple(3f, 6f, 9f, 0f), 3f * vec)
    }

    @Test
    fun div() {
        val tuple = Tuple(3f, 6f, 9f, 12f)

        assertEquals(Tuple(1f, 2f, 3f, 4f), tuple / 3f)
    }

    @Test
    fun dot() {
        val vec1 = Tuple.vector(1f, 0f, 0f)
        val vec2 = Tuple.vector(.5f, 500f, 123f)

        assertEquals(.5f, vec1 dot vec2)
    }

    @Test
    fun cross() {
        val vec1 = Tuple.vector(1f, 0f, 0f)
        val vec2 = Tuple.vector(0f, 1f, 0f)

        assertEquals(Tuple.vector(0f, 0f, 1f), vec1 cross vec2)
    }
}