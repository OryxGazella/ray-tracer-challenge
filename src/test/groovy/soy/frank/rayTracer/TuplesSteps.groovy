package soy.frank.rayTracer

import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.ExtensionsKt
import soy.frank.rayTracer.maths.Tuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

List<Tuple> xs
def delta = 0.000001f

Before() {
    xs = []
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