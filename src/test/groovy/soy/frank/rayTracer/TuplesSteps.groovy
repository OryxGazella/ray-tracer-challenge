package soy.frank.rayTracer

import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.maths.ExtensionsKt
import soy.frank.rayTracer.maths.Tuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

List<Tuple> xs
List<Color> cs
def delta = 0.000001f

Before() {
    xs = []
    cs = []
}

Given("(v|z)(1/2) := vector\\({float}, {float}, {float})") { float x, float y, float z ->
    xs.add(Tuple.@Companion.vector(x, y, z))
}

Given("a(1/2) := Tuple\\({float}, {float}, {float}, {float})") { float x, float y, float z, float w ->
    xs.add new Tuple(x, y, z, w)
}

Given("p(1/2) := point\\({float}, {float}, {float})") { float x, float y, float z ->
    xs.add(Tuple.@Companion.point(x, y, z))
}

Then("a is a {string}") { String expectedType ->
    assert expectedType.toLowerCase() == xs[0].type.toString().toLowerCase()
}

Then("a is not a {string}") { String nonExpectedType ->
    assert nonExpectedType.toLowerCase() != xs[0].type.toString()
}

Then("(p|v|a|b) = {string}") { String expectedToString ->
    assert expectedToString == xs[0].toString()
}

Then("a1 + a2 = {string}") { String expectedTuple ->
    def sum = xs[0] + xs[1]
    assert expectedTuple == sum.toString()
}

Then("(p/v)(zero)(1) - (p/v)(2) = {string}") { String expectedTuple ->
    def difference = xs[0] - xs[1]
    assert expectedTuple == difference.toString()
}

/* Groovy and Kotlin have different names for operators
  Groovy    | Kotlin
  negative  | unaryMinus
  multiply  | times
  */
When("b := a * {float}") { Float scalar ->
    xs[0] = ExtensionsKt.times(scalar, xs[0])
}

//See above
When("b := {float} * a") { float scalar ->
    xs[0] = xs[0].times(scalar)
}

//See above
Then("-a = {string}") { String expectedTuple ->
    assert expectedTuple == (xs[0].unaryMinus()).toString()
}

When("b := a / {float}") { float scalar ->
    xs[0] = xs[0] / scalar
}

Then("magnitude\\(v) = {float}") { float expectedMagnitude ->
    assert Math.abs(expectedMagnitude - xs[0].magnitude()) < delta
}

Given("b := v.normalize") { ->
    xs[0] = xs[0].normalize()
}

Then("v1 dot v2 = {float}") { float expectedDotProduct ->
    assert expectedDotProduct == xs[0].dot(xs[1])
}

Then("v1 cross v2 = {string}") { String expectedTuple ->
    assert expectedTuple == xs[0].cross(xs[1]).toString()
}

Given("c(1|2) := Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    cs.add(new Color(red, green, blue))
}

Then("color.{word} = {float}") { String color, float value ->
    assert cs[0].invokeMethod("get${color.capitalize()}", null) == value
}

static def compareColor(float red, float green, float blue, Color toCompare) {
    def colorValue = [
            'red'  : red,
            'green': green,
            'blue' : blue
    ]
    ["red", "green", "blue"].each { color ->
        assert Math.abs(toCompare.invokeMethod("get${color.capitalize()}", null) as float - colorValue[color]) < 0.000001f
    }
}

Then("c1 + c2 = Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    def addition = cs[0] + cs[1]
    compareColor(red, green, blue, addition)
}

Then("c1 - c2 = Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    def difference = cs[0] - cs[1]
    compareColor(red, green, blue, difference)
}

Then("c1 * {float} = Color\\({float}, {float}, {float})") { float scalar, float red, float green, float blue ->
    def result = cs[0].times(scalar)
    assert new Color(red, green, blue) == result
}

Then("{float} * c1 = Color\\({float}, {float}, {float})") { float scalar, float red, float green, float blue ->
    def result = ExtensionsKt.times(scalar, cs[0])
    assert new Color(red, green, blue) == result
}

Then("c1 * c2 = Color\\({float}, {float}, {float})") { float red, float green, float blue ->
    def product = cs[0].times(cs[1])
    compareColor(red, green, blue, product)
}
