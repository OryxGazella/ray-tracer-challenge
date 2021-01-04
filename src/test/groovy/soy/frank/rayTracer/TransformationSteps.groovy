package soy.frank.rayTracer

import groovy.transform.Field

import io.cucumber.groovy.EN
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

import static soy.frank.rayTracer.TestVariables.asMatrix
import static soy.frank.rayTracer.TestVariables.asTuple

this.metaClass.mixin(EN)
@Field def private testVariables = TestVariables.map

private static compareTuples(Tuple tuple, Tuple other) {
    ['x', 'y', 'z', 'w'].each { name ->
        assert Math.abs((tuple.properties[name] as float) - (other.properties[name] as float)) < 0.000001f
    }
}

Given(/^([A-Z]+\d*|transform) := (\w+)\((-?\d+.?\d*), (-?\d+.?\d*), (-?\d+.?\d*)\)$/) {
    String transformVariable, String transformName, Float x, Float y, Float z ->
        testVariables[transformVariable] = Matrix.@Companion.invokeMethod(transformName, [x, y, z])
}

Given("{word} := rotationX\\(π \\/ {int})") { String transformName, Integer scalar ->
    testVariables[transformName] = Matrix.@Companion.rotationX((Math.PI / scalar) as float)
}

Given("{word} := rotationY\\(π \\/ {int})") { String transformName, Integer scalar ->
    testVariables[transformName] = Matrix.@Companion.rotationY((Math.PI / scalar) as float)
}

Given("{word} := rotationZ\\(π \\/ {int})") { String transformName, Integer scalar ->
    testVariables[transformName] = Matrix.@Companion.rotationZ((Math.PI / scalar) as float)
}

Given("transform := shearing\\({float}, {float}, {float}, {float}, {float}, {float})") {
    Float x_y, Float x_z, Float y_x, Float y_z, Float z_x, Float z_y ->
        testVariables.transform = Matrix.@Companion.shearing(x_y, x_z, y_x, y_z, z_x, z_y)
}

Then("{word} * p = point\\({float}, {float}, {float})") { String transformName, Float x, Float y, Float z ->
    compareTuples(asMatrix(transformName) * asTuple('p'), Tuple.@Companion.point(x, y, z))
}

Then("{word} * v = vector\\({float}, {float}, {float})") { String transformName, Float x, Float y, Float z ->
    assert asMatrix(transformName) * asTuple('v') == Tuple.@Companion.vector(x, y, z)
}

Then("transform * v = v") { ->
    assert asMatrix('transform') * asTuple('v') == asTuple('v')
}

When(/^([a-z]+\d*) := ([A-Z]+) \* ([a-z]+\d*)$/) { String resultName, String matrix, String tupleName  ->
    testVariables[resultName] = asMatrix(matrix) * asTuple(tupleName)
}

When("T := C * B * A") {  ->
    testVariables.T = asMatrix('C') * asMatrix('B') * asMatrix('A')
}