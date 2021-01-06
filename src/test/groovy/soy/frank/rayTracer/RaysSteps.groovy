package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.datatable.DataTable
import io.cucumber.groovy.EN
import soy.frank.rayTracer.maths.Ray
import soy.frank.rayTracer.maths.Tuple
import soy.frank.rayTracer.shapes.Sphere
import soy.frank.rayTracer.soy.frank.rayTracer.maths.Intersection

import static soy.frank.rayTracer.TestVariables.asIntersection
import static soy.frank.rayTracer.TestVariables.asRay
import static soy.frank.rayTracer.TestVariables.asSphere
import static soy.frank.rayTracer.TestVariables.asTuple

this.metaClass.mixin(EN)
@Field def private testVariables = TestVariables.map

When(/^r ← Ray\(origin, direction\)$/) { ->
    testVariables.r = new Ray(asTuple('origin'), asTuple('direction'))
}

Given("^r ← Ray\\(" +
        "point\\((-?\\d+.?\\d*), (-?\\d+.?\\d*), (-?\\d+.?\\d*)\\), " +
        "vector\\((-?\\d+.?\\d*), (-?\\d+.?\\d*), (-?\\d+.?\\d*)\\).*\$") {
    Float px, Float py, Float pz, Float vx, Float vy, Float vz ->
        testVariables.r = new Ray(Tuple.@Companion.point(px, py, pz), Tuple.@Companion.vector(vx, vy, vz))
}

Then(/^r.origin = origin$/) { ->
    assert asRay('r').origin == asTuple('origin')
}

Then(/^r.direction = direction$/) { ->
    assert asRay('r').direction == asTuple('direction')
}


Then('r.position\\({float}) = point\\({float}, {float}, {float})') { Float t, Float px, Float py, Float pz ->
    assert asRay('r').position(t) == Tuple.@Companion.point(px, py, pz)
}

Given(/^s ← Sphere\(\)/) { ->
    testVariables.s = new Sphere()
}

When("xs ← s.intersects\\(r)") { ->
    testVariables.xs = asSphere("s").intersects(asRay("r"))
}

When(/^i\d? ← Intersection\((.*), s\)$/) { Float tValue ->
    testVariables.i = new Intersection(tValue, asSphere('s'))
}

Given(/xs ← Intersections:/) { DataTable dataTable ->
}

When(/i ← hit\(xs)/) {  ->
}

Then("xs.count = {int}") { Integer int1 ->
    assert (testVariables.xs as Intersection[]).length == 2
}

Then("xs[{int}] = {float}") { Integer index, float value ->
    assert (testVariables.xs as Intersection[])[index].t == value
}

Then(/i.t = {float}/) { Float tValue ->
    assert asIntersection('i').t == tValue
}

Then(/i.target = s/) {  ->
    assert asIntersection('i').target == asSphere('s')
}

Then(/xs[{int}].target = s/) { Integer index ->
    assert (testVariables.xs[index] as Intersection).target == asSphere('s')
}