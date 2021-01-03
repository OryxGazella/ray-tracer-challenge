package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.datatable.DataTable
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

@Field private Object testVariables = TestVariables.map

Before {
}

Given(/^the following matrix ([A-Z]):$/) { String varName, DataTable table ->
    testVariables[varName] = tableToMatrix(table)
}

private static tableToMatrix(DataTable table) {
    def rows = table.asLists().collect {
        it.collect {
            Float.parseFloat(it)
        }
    }
    new Matrix(
            rows)
}

Then(/^([A-Z])\[(.*), (.*)\] = (.*)$/) { String prefix, int x, int y, String value ->
    def matrixToTest = testVariables[prefix] as Matrix
    assert matrixToTest[x, y] - (evaluate(value) as float) < 0.00001f
}

Then(/A = B/) { ->
    def A = testVariables.A as Matrix
    def B = testVariables.B as Matrix
    assert A == B
    assert A !== B
}

Then(/A != B/) { ->
    def A = testVariables.A as Matrix
    def B = testVariables.B as Matrix
    assert A != B
}

Then(/A * B is the following matrix:/) { DataTable dataTable ->
    def rows = dataTable.asLists().collect {
        it.collect {
            Float.parseFloat(it)
        }
    }
    def expected = new Matrix(rows)
    assert testVariables.A * testVariables.B == expected
}

Then(/^A \* ([a-z]) = Tuple\((.*), (.*), (.*), (.*)\)$/) {String tuple,  float x, float y, float z, float w ->
    assert testVariables.A * testVariables[tuple] == new Tuple(x, y, z, w)
}

Then(/A * identity_matrix = A/) { ->
    assert testVariables.A * Matrix.@Companion.identityMatrix44 == testVariables.A
}

Then(/identity_matrix * A = A/) { ->
    assert Matrix.@Companion.identityMatrix44 *
            (testVariables.A as Matrix) ==
            testVariables.A
}

Then(/identity_matrix * a = a/) { ->
    assert Matrix.@Companion.identityMatrix44 * (testVariables.a as Tuple) == testVariables.a
}

Then(/^A.transposed\(\) is the following matrix:$/) { DataTable table ->
    assert testVariables.A.transposed() == tableToMatrix(table)
}

Then(/^([A-Z]).determinant\(\) = (.*)$/) { String prefix, float determinant ->
    def matrixToTest = testVariables[prefix]
    assert matrixToTest.determinant() == determinant
}

Then(/^A.submatrix\((.*), (.*)\) is the following matrix:$/) { Integer row, Integer column, DataTable table ->
    assert testVariables.A.submatrix(row, column) == tableToMatrix(table)
}

Given(/^B := A.submatrix\((.*), (.*)\)/) { Integer row, Integer column ->
    testVariables.B = testVariables.A.submatrix(row, column)
}

Then(/^A.minor\((.*), (.*)\) = (.*)/) { Integer row, Integer column, Float minor ->
    assert testVariables.A.minor(row, column) == minor
}

Then(/^A.cofactor\((.*), (.*)\) = (.*)$/) { Integer row, Integer column, float cofactor ->
    assert testVariables.A.cofactor(row, column) == cofactor
}

Then(/A is invertible/) { ->
    assert testVariables.A.invertible
}

Then(/A is not invertible/) { ->
    assert !testVariables.A.invertible
}

Given(/^B := A.inverse\(\)$/) { ->
    testVariables.B = testVariables.A.inverse()
}

Then(/^B is the following matrix:$/) { DataTable table ->
    def expectedMatrix = tableToMatrix(table)
    def matrixToTest = testVariables.B as Matrix
    compareMatrices(matrixToTest, expectedMatrix)
}

private static compareMatrices(matrixToTest, expectedMatrix) {
    def rows = matrixToTest.numberOfRows
    assert rows == expectedMatrix.numberOfRows
    def columns = matrixToTest.numberOfColumns
    assert columns == expectedMatrix.numberOfColumns
    (0..<rows).each { int row ->
        (0..<columns).each { int column ->
            assert matrixToTest[row, column] - expectedMatrix[row, column] < 0.00001f
        }
    }
}

Then(/^\(A \* B\) \* B.inverse\(\) = A$/) { ->
    def A = testVariables.A
    def B = testVariables.B
    def actual = (A * B) * B.inverse()
    compareMatrices(actual, A)
}