package soy.frank.rayTracer

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import org.junit.Assert.*

class StepDefinitions {

    private var actualSum : Int = 0

    @Given("{int} added to {int}")
    fun added_to(augend: Int, addend: Int) {
        actualSum = Calculator.add(augend, addend)
    }

    @Then("sum is equal to {int}")
    fun sum_is_equal_to(expectedSum: Int?) {
        assertEquals(expectedSum, actualSum)
    }
}

// Implementation classes are kept as close to the tests
// until they're ready to graduate this is a stub
class Calculator {
    companion object {
        fun add(augend: Int, addend: Int)  = augend + addend
    }
}