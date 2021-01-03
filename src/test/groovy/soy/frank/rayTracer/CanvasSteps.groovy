package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.rendering.Canvas

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

@Field private Object testVariables = TestVariables.map

Given("canvas := Canvas\\({int}, {int})") { int width, int height ->
    testVariables.canvas = new Canvas(width, height)
}

Then("canvas.{word} = {int}") { String property, Integer expectedValue ->
    assert testVariables.canvas.invokeMethod("get${property.capitalize()}", null) == expectedValue
}

Then("every pixel of canvas is Color\\({float}, {float}, {float})") { Float redValue, Float greenValue, Float blueValue ->
    def expectedColor = new Color(redValue, greenValue, blueValue)
    testVariables.canvas.pixels.forEach { k, row ->
        row.each { Color entry ->
            assert entry == expectedColor
        }
    }
}

When("canvas.writePixel\\({int}, {int}, {word})") { Integer x, Integer y, String colorName ->
    testVariables.canvas.writePixel(x, y, testVariables[colorName] as Color)
}

Then("canvas.pixelAt\\({int}, {int}) = {word}") { Integer x, Integer y, String colorName ->
    assert testVariables.canvas.pixelAt(x, y) == testVariables[colorName] as Color
}

Then("lines {int}-{int} of canvas.ppm are") { Integer from, Integer to, String docString ->
    def lines = testVariables.canvas.ppm.
            drop(from - 1).
            take(to - from + 1).
            join("\n")
    assert lines == docString
}

When("every pixel of c is set to Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    def canvas = testVariables.canvas as Canvas
    def color = new Color(red, green, blue)
    (0 ..< canvas.width).each { x ->
        (0 ..< canvas.height).each { y ->
            canvas.writePixel(x, y, color)
        }
    }
}

Then(/ppm ends with a newline/) {  ->
    assert testVariables.canvas.ppm.last() == "\n"
}