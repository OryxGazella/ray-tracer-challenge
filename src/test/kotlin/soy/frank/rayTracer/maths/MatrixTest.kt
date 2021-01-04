package soy.frank.rayTracer.maths

import org.junit.Ignore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import soy.frank.rayTracer.rendering.Canvas
import java.io.File
import kotlin.math.roundToInt

internal class MatrixTest {
    @Test
    internal fun companionCreatesTranslationMatrix() {
        assertEquals(
            Matrix.translation(1f, 2f, 3f),
            Matrix(
                listOf(
                    listOf(1f, 0f, 0f, 1f),
                    listOf(0f, 1f, 0f, 2f),
                    listOf(0f, 0f, 1f, 3f),
                    listOf(0f, 0f, 0f, 1f),
                ))
        )
    }

    @Test
    internal fun companionCreatesScalingMatrix() {
        assertEquals(
            Matrix.scaling(1f, 2f, 3f),
            Matrix(
                listOf(
                    listOf(1f, 0f, 0f, 0f),
                    listOf(0f, 2f, 0f, 0f),
                    listOf(0f, 0f, 3f, 0f),
                    listOf(0f, 0f, 0f, 1f),
                ))
        )

    }

    @Test
    @Ignore("Chapter 4 end of chapter challenge")
    internal fun clockFace() {
        val c = Canvas(600, 600)
        val origin = Tuple.point(0f, 0f, 0f)
        val upTranslation = Matrix.translation(0f, 280f, 0f)
        val centerTranslation = Matrix.translation(300f, 300f, 0f)
        (0 until 12).forEach {
            val (x, y) = centerTranslation * Matrix.rotationZ((Math.PI/6f * it).toFloat()) * upTranslation * origin
            c.writePixel(x.roundToInt(), y.roundToInt(), Color(255f, 255f, 255f))
        }
        File("clockFace.ppm").writeText(c.ppm.joinToString("\n"))
    }
}