package soy.frank.rayTracer

import groovy.transform.Field
import io.cucumber.datatable.DataTable
import io.cucumber.groovy.EN
import io.cucumber.groovy.Hooks
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

this.metaClass.mixin(EN)
this.metaClass.mixin(Hooks)

List<Matrix> matrices
@Field
Tuple tuple

Before {
    matrices = []
}

Given(/the following matrix (M|A|B):/) { DataTable table ->
    matrices.add(tableToMatrix(table))
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
    def matrixToTest = (prefix == 'B') ? matrices[1] : matrices[0]
    assert matrixToTest[x, y] - (evaluate(value) as float) < 0.00001f
}

Then(/A = B/) { ->
    assert matrices[0] == matrices[1]
}

Then(/A != B/) { ->
    assert matrices[0] != matrices[1]
}

Then(/A * B is the following matrix:/) { DataTable dataTable ->
    def rows = dataTable.asLists().collect {
        it.collect {
            Float.parseFloat(it)
        }
    }
    def expected = new Matrix(rows)

    assert matrices[0] * matrices[1] == expected
}

Given("b := tuple\\({float}, {float}, {float}, {float})") { float x, float y, float z, float w ->
    tuple = new Tuple(x, y, z, w)
}

Then("A * b = tuple\\({float}, {float}, {float}, {float})") { float x, float y, float z, float w ->
    assert matrices[0] * this.tuple == new Tuple(x, y, z, w)
}

Then(/A * identity_matrix = A/) {  ->
    assert matrices[0] * Matrix.@Companion.identityMatrix44 == matrices[0]
}

Then(/identity_matrix * A = A/) {  ->
    assert Matrix.@Companion.identityMatrix44 * matrices[0] == matrices[0]
}

Then(/identity_matrix * a = a/) {  ->
    assert Matrix.@Companion.identityMatrix44 * this.tuple == this.tuple
}

Then(/^A.transposed\(\) is the following matrix:$/) { DataTable table ->
    assert matrices[0].transposed() == tableToMatrix(table)
}

Then(/^([A-Z]).determinant\(\) = (.*)$/) { String prefix, float determinant ->
    def matrixToTest = (prefix == 'A') ? matrices.first() : matrices.last()
    assert matrixToTest.determinant() == determinant
}

Then(/^A.submatrix\((.*), (.*)\) is the following matrix:$/) { Integer row, Integer column, DataTable table ->
    assert matrices[0].submatrix(row, column) == tableToMatrix(table)
}

Given(/^B := A.submatrix\((.*), (.*)\)/) { Integer row, Integer column ->
    matrices.add(matrices[0].submatrix(row, column))
}

Then(/^A.minor\((.*), (.*)\) = (.*)/) { Integer row, Integer column, Float minor ->
    assert matrices[0].minor(row, column) == minor
}

Then(/^A.cofactor\((.*), (.*)\) = (.*)$/) { Integer row, Integer column, float cofactor ->
    assert matrices[0].cofactor(row, column) == cofactor
}

Then(/A is invertible/) {  ->
    assert matrices[0].invertible
}

Then(/A is not invertible/) {  ->
    assert !matrices[0].invertible
}

Given(/^B := A.inverse\(\)$/) {  ->
    matrices.add(matrices[0].inverse())
}

Then(/^B is the following matrix:$/) { DataTable table ->
    def expectedMatrix = tableToMatrix(table)
    def matrixToTest = matrices.last()
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
    def A = matrices[0]
    def B = matrices[1]
    def actual = (A * B) * B.inverse()
    compareMatrices(actual, A)
}