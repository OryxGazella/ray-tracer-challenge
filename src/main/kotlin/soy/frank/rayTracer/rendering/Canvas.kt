package soy.frank.rayTracer.rendering

import soy.frank.rayTracer.maths.Color

data class Canvas(val width: Int, val height: Int) {

    fun writePixel(x: Int, y: Int, color: Color) {
        var row = pixels[y]
        if (row == null) {
            row = emptyRow()
        }
        row[x] = color
        pixels[y] = row
    }

    private fun emptyRow() = Array(width) {
        Color.black
    }

    fun pixelAt(x: Int, y: Int): Color {
        val row = pixels[y] ?: return Color.black
        return row[x]
    }

    private val pixels: MutableMap<Int, Array<Color>> = mutableMapOf()

    val ppm: List<String>
        get() {
            val pixelLines = (0 until height)
                    .asSequence()
                    .map {
                        pixels[it] ?: emptyRow()
                    }
                    .map { row ->
                        row
                                .map(Color::asPixMap)
                                .fold(mutableListOf<StringBuilder>()) { acc, pixMap ->
                                    if (acc.isEmpty()) acc.add(StringBuilder())
                                    if (acc.last().length + pixMap.length + 1 > 70) {
                                        acc.add(StringBuilder())
                                    }
                                    var toAppend = " $pixMap"
                                    if(acc.last().isEmpty()) toAppend = pixMap
                                    acc.last().append(toAppend)
                                    acc
                                }
                                .map { it.toString() }
                    }
                    .flatten()
                    .toList()

            val header = listOf("P3", "$width $height", "255")
            return listOf(
                    header,
                    pixelLines,
                    listOf("\n")
            ).flatten()
        }
}