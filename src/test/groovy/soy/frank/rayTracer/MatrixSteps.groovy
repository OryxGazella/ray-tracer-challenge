package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.datatable.DataTable
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

import static soy.frank.rayTracer.TestVariables.asMatrix
import static soy.frank.rayTracer.TestVariables.asTuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

@Field private Object testVariables = TestVariables.map

private static tableToMatrix(DataTable table) {
    new Matrix(
            table.asLists().collect {
                it.collect {
                    Float.parseFloat(it)
                }
            })
}

Given(/^the following matrix ([A-Z]):$/) { String varName, DataTable table ->
    testVariables[varName] = tableToMatrix(table)
}

Then(/^([A-Z])\[(.*), (.*)\] = (.*)$/) { String prefix, int x, int y, String value ->
    assert asMatrix(prefix)[x, y] - (evaluate(value) as float) < 0.00001f
}

Then(/A = B/) { ->
    assert asMatrix('A') == asMatrix('B')
    assert asMatrix('A') !== asMatrix('B')
}

Then(/A != B/) { ->
    assert asMatrix('A') != asMatrix('B')
}

Then(/A * B is the following matrix:/) { DataTable dataTable ->
    def rows = dataTable.asLists().collect {
        it.collect {
            Float.parseFloat(it)
        }
    }
    def expected = new Matrix(rows)
    assert asMatrix('A') * asMatrix('B') == expected
}

Then(/^A \* ([a-z]) = Tuple\((.*), (.*), (.*), (.*)\)$/) { String tuple, float x, float y, float z, float w ->
    assert asMatrix('A') * asTuple(tuple) == new Tuple(x, y, z, w)
}

Then(/A * identity_matrix = A/) { ->
    assert asMatrix('A') * Matrix.@Companion.identityMatrix44 == asMatrix('A')
}

Then(/identity_matrix * A = A/) { ->
    assert Matrix.@Companion.identityMatrix44 * asMatrix('A') == asMatrix('A')
}

Then(/identity_matrix * a = a/) { ->
    assert Matrix.@Companion.identityMatrix44 * asTuple('a') == asTuple('a')
}

Then(/^A.transposed\(\) is the following matrix:$/) { DataTable table ->
    assert asMatrix('A').transposed() == tableToMatrix(table)
}

Then(/^([A-Z]).determinant\(\) = (.*)$/) { String prefix, float determinant ->
    assert asMatrix(prefix).determinant() == determinant
}

Then(/^A.submatrix\((.*), (.*)\) is the following matrix:$/) { Integer row, Integer column, DataTable table ->
    assert asMatrix('A').submatrix(row, column) == tableToMatrix(table)
}

Given(/^B ← A.submatrix\((.*), (.*)\)/) { Integer row, Integer column ->
    testVariables.B = asMatrix('A').submatrix(row, column)
}

Then(/^A.minor\((.*), (.*)\) = (.*)/) { Integer row, Integer column, Float minor ->
    assert asMatrix('A').minor(row, column) == minor
}

Then(/^A.cofactor\((.*), (.*)\) = (.*)$/) { Integer row, Integer column, float cofactor ->
    assert asMatrix('A').cofactor(row, column) == cofactor
}

Then(/A is invertible/) { ->
    assert asMatrix('A').invertible
}

Then(/A is not invertible/) { ->
    assert !asMatrix('A').invertible
}

Given(/^(\w*) ← (\w*).inverse\(\)$/) { String resultVariable, String sourceVariable ->
    testVariables[resultVariable] = asMatrix(sourceVariable).inverse()
}

Then(/^B is the following matrix:$/) { DataTable table ->
    def expectedMatrix = tableToMatrix(table)
    def matrixToTest = asMatrix('B')
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
    def A = asMatrix('A')
    def B = asMatrix('B')
    def actual = (A * B) * B.inverse()
    compareMatrices(actual, A)
}