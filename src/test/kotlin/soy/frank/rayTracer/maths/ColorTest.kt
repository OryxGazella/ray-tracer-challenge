package soy.frank.rayTracer.maths

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ColorTest {
    @Test
    fun hasRgbValues() {
        val c = Color(1f, 2f, 3f)

        assertEquals(1f, c.red)
        assertEquals(2f, c.green)
        assertEquals(3f, c.blue)
    }

    @Test
    internal fun multipliesWithScalar() {
        val color = Color(.25f, .25f, .25f)

        assertEquals(Color(.5f, .5f, .5f), color * 2f)
        assertEquals(Color(.5f, .5f, .5f), 2f * color)
    }

    private val delta = 0.00001f

    @Test
    internal fun hadamardProduct() {
        val c1 = Color(1f, .2f, .4f)
        val c2 = Color(.9f, 1f, .1f)

        val expectedColor = Color(.9f, .2f, .04f)
        val hadamardProduct = c1 * c2

        assertEquals(expectedColor.red, hadamardProduct.red, delta)
        assertEquals(expectedColor.green, hadamardProduct.green, delta)
        assertEquals(expectedColor.blue, hadamardProduct.blue, delta)
    }
}