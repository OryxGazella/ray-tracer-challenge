package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.maths.Tuple

import static soy.frank.rayTracer.TestVariables.asColor
import static soy.frank.rayTracer.TestVariables.asTuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

@Field
def testVariables = TestVariables.map

def delta = 0.000001f

Given("{word} := vector\\({float}, {float}, {float})") { String variableName, float x, float y, float z ->
    testVariables[variableName] = Tuple.@Companion.vector(x, y, z)
}

Given(/^([a-z]\d*) := Tuple\((.*), (.*), (.*), (.*)\)$/) { String variableName, float x, float y, float z, float w ->
    testVariables[variableName] = new Tuple(x, y, z, w)
}

Given("{word} := point\\({float}, {float}, {float})") { String variableName, float x, float y, float z ->
    testVariables[variableName] = Tuple.@Companion.point(x, y, z)
}

Then("{word} is a {string}") { String variableName, String expectedType ->
    assert expectedType.toLowerCase() == asTuple(variableName).type.toString().toLowerCase()
}

Then("{word} is not a {string}") { String variableName, String nonExpectedType ->
    assert nonExpectedType.toLowerCase() != asTuple(variableName).type.toString()
}

Then(/^([a-z]+\d*) = (.*)$/) { String variableName, String expectedToString ->
    assert expectedToString == asTuple(variableName).toString()
}

Then(/^(\w+\d*) \+ (\w+\d*) = (Tuple.*)$/) { String lhs, String rhs, String expectedToString ->
    assert (asTuple(lhs) + asTuple(rhs)).toString() == expectedToString
}

Then("{word} - {word} = {string}") { String lhs, String rhs, String expectedTuple ->
    assert (asTuple(lhs) - asTuple(rhs)).toString() == expectedTuple
}

When("{word} := {word} * {float}") { String result, String base, Float scalar ->
    testVariables[result] = scalar * asTuple(base)
}

When("{word} := {float} * {word}") { String result, float scalar, String base ->
    testVariables[result] = asTuple(base) * scalar
}

Then("-a = {string}") { String expectedTuple ->
    assert expectedTuple == (-asTuple('a')).toString()
}

When("{word} := {word} / {float}") { String result, String base, float scalar ->
    testVariables[result] = asTuple(base) / scalar
}

Then("magnitude\\({word}) = {float}") { String tupleName, float expectedMagnitude ->
    assert Math.abs(expectedMagnitude - asTuple(tupleName).magnitude()) < delta
}

Given("{word} := {word}.normalize") { String result, String base ->
    testVariables[result] = asTuple(base).normalize()
}

Then("{word} dot {word} = {float}") { String lhs, String rhs, float expectedDotProduct ->
    assert expectedDotProduct == asTuple(lhs).dot(asTuple(rhs))
}

Then("{word} cross {word} = {string}") { String lhs, String rhs, String expectedTuple ->
    assert expectedTuple == asTuple(lhs).cross(asTuple(rhs)).toString()
}

Given(/^([a-zA-Z]+\d*) := Color\((-?\d+\.?\d*), (-?\d+\.?\d*), (-?\d+\.?\d*)\)$/) { String colorName, float redValue, float greenValue, float blueValue ->
    testVariables[colorName] = new Color(redValue, greenValue, blueValue)
}

Then("color.{word} = {float}") { String color, float value ->
    assert asColor('color').invokeMethod("get${color.capitalize()}", null) == value
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

Then("{word} + {word} = Color\\({float}, {float}, {float})") { String lhs, String rhs, float red, float green, float blue ->
    Color addition = asColor(lhs) + asColor(rhs)
    compareColor(red, green, blue, addition)
}

Then("{word} - {word} = Color\\({float}, {float}, {float})") { String lhs, String rhs, float red, float green, float blue ->
    Color difference = asColor(lhs) - asColor(rhs)
    compareColor(red, green, blue, difference)
}

Then(/^([a-z]+\d*) \* (-?\d+\.?\d*) = Color\((.*), (.*), (.*)\)$/) { String variableName, float scalar, float red, float green, float blue ->
    def result = asColor(variableName) * scalar
    assert new Color(red, green, blue) == result
}

Then(/^([a-z]\d*) \* ([a-z]+\d*) = Color\((.*), (.*), (.*)\)$/) { String lhs, String rhs, float red, float green, float blue ->
    Color product = asColor(lhs) * asColor(rhs)
    compareColor(red, green, blue, product)
}

Then(/^(\d+\.?\d*) \* ([a-z]+\d*) = Color\((.*), (.*), (.*)\)$/) { float scalar, String variableName, float red, float green, float blue ->
    def result = scalar * asColor(variableName)
    assert new Color(red, green, blue) == result
}