package soy.frank.rayTracer

import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.rendering.Canvas

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

Canvas canvas
def colors = [:]

Given("canvas := Canvas\\({int}, {int})") { int width, int height ->
    canvas = new Canvas(width, height)
}

Then("canvas.{word} = {int}") { String property, Integer expectedValue ->
    assert canvas.invokeMethod("get${property.capitalize()}", null) == expectedValue
}

Then("every pixel of canvas is Color\\({float}, {float}, {float})") { Float redValue, Float greenValue, Float blueValue ->
    def expectedColor = new Color(redValue, greenValue, blueValue)
    canvas.pixels.forEach { k, row ->
        row.each { Color entry ->
            assert entry == expectedColor
        }
    }
}


Given(/^([a-zA-Z]+) := Color\((-?\d+\.?\d*), (-?\d+\.?\d*), (-?\d+\.?\d*)\)$/) { String colorName, float redValue, float greenValue, float blueValue ->
    colors[colorName] = new Color(redValue, greenValue, blueValue)
}


When("canvas.writePixel\\({int}, {int}, {word})") { Integer x, Integer y, String colorName ->
    canvas.writePixel(x, y, colors[colorName] as Color)
}

Then("canvas.pixelAt\\({int}, {int}) = {word}") { Integer x, Integer y, String colorName ->
    assert canvas.pixelAt(x, y) == colors[colorName] as Color
}

Then("lines {int}-{int} of canvas.ppm are") { Integer from, Integer to, String docString ->
    def lines = canvas.ppm.
            drop(from - 1).
            take(to - from + 1).
            join("\n")
    assert lines == docString
}

When("every pixel of c is set to Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    def color = new Color(red, green, blue)
    (0 ..< canvas.width).each { x ->
        (0 ..< canvas.height).each { y ->
            canvas.writePixel(x, y, color)
        }
    }
}

Then(/ppm ends with a newline/) {  ->
    assert canvas.ppm.last() == "\n"
}