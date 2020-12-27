package soy.frank.rayTracer

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import soy.frank.rayTracer.maths.Tuple
import soy.frank.rayTracer.soy.frank.rayTracer.maths.times


class TuplesSteps {
    private var xs: MutableList<Tuple> = mutableListOf()

    @Given("(v|z)(1/2) := vector\\({float}, {float}, {float})")
    fun p_vector(x: Float, y: Float, z: Float) {
        xs.add(Tuple.vector(x, y, z))
    }

    @Given("a(1/2) := Tuple\\({float}, {float}, {float}, {float})")
    fun a1_tuple(x: Float, y: Float, z: Float, w: Float) {
        xs.add(Tuple(x, y, z, w))
    }

    @Given("p(1/2) := point\\({float}, {float}, {float})")
    fun p1_point(x: Float, y: Float, z: Float) {
        xs.add(Tuple.point(x, y, z))
    }

    @Then("a is a {string}")
    fun a_is_a_point(expectedType: String) {
        assertEquals(expectedType.toLowerCase(), xs[0].type.name.toLowerCase())
    }

    @Then("a is not a {string}")
    fun a_is_not_a_vector(nonExpectedType: String) {
        assertNotEquals(nonExpectedType.toLowerCase(), xs[0].type.name.toLowerCase())
    }

    @Then("(p|v|a|b) = {string}")
    fun p(expectedToString: String) {
        assertEquals(expectedToString, xs[0].toString())
    }

    @Then("a1 + a2 = {string}")
    fun a1_a2(expectedTuple: String?) {
        val sum = xs[0] + xs[1]
        assertEquals(expectedTuple, sum.toString())
    }

    @Then("(p/v)(zero)(1) - (p/v)(2) = {string}")
    fun p1_p2(expectedTuple: String?) {
        val difference = xs[0] - xs[1]
        assertEquals(expectedTuple, difference.toString())
    }

    @Then("-a = {string}")
    fun negate_a(expectedTuple: String?) {
        assertEquals(expectedTuple, (-xs[0]).toString())
    }

    @When("b := a * {float}")
    fun b_a(scalar: Float) {
        xs[0] = scalar * xs[0]
    }

    @When("b := a / {float}")
    fun b_div_a(scalar: Float) {
        xs[0] = xs[0] / scalar
    }

    @When("b := {float} * a")
    fun a_b(scalar: Float) {
        xs[0] = xs[0] * scalar
    }

    private val delta = 0.000001f

    @Then("magnitude\\(v) = {float}")
    fun magnitude_v(expectedMagnitude: Float) {
        assertEquals(expectedMagnitude, xs[0].magnitude(), delta)
    }

    @Given("b := v.normalize")
    fun b_v_normalize() {
        xs[0] = xs[0].normalize()
    }

    @Then("v1 dot v2 = {float}")
    fun v1_dot_v2(expectedDotProduct: Float) {
        assertEquals(expectedDotProduct, xs[0] dot xs[1])
    }

    @Then("v1 cross v2 = {string}")
    fun v1_cross_v2(expectedTuple: String) {
        assertEquals(expectedTuple, (xs[0] cross xs[1]).toString())
    }

}