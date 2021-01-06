package soy.frank.rayTracer.maths

import kotlin.math.cos
import kotlin.math.sin

class Matrix {
    private val matrix: FloatArray
    val numberOfRows: Int
    val numberOfColumns: Int
    private val determinant: Float by lazy {
        this.calculateDeterminant()
    }

    fun determinant(): Float = this.determinant

    constructor(rows: Collection<List<Float>>) {
        numberOfRows = rows.size
        numberOfColumns = if (rows.isEmpty()) 0 else rows.first().size
        matrix = rows.flatten().toFloatArray()
    }

    internal constructor(numberOfRows: Int, numberOfColumns: Int, matrix: FloatArray) {
        this.matrix = matrix
        this.numberOfRows = numberOfRows
        this.numberOfColumns = numberOfColumns
    }

    companion object {
        fun translation(x: Float, y: Float, z: Float) = Matrix(
            listOf(
                listOf(1f, 0f, 0f, x),
                listOf(0f, 1f, 0f, y),
                listOf(0f, 0f, 1f, z),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        fun scaling(x: Float, y: Float, z: Float) = Matrix(
            listOf(
                listOf(x, 0f, 0f, 0f),
                listOf(0f, y, 0f, 0f),
                listOf(0f, 0f, z, 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        fun rotationX(angleInRadians: Float) = Matrix(
            listOf(
                listOf(1f, 0f, 0f, 0f),
                listOf(0f, cos(angleInRadians), -sin(angleInRadians), 0f),
                listOf(0f, sin(angleInRadians), cos(angleInRadians), 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        fun rotationY(angleInRadians: Float) = Matrix(
            listOf(
                listOf(cos(angleInRadians), 0f, sin(angleInRadians), 0f),
                listOf(0f, 1f, 0f, 0f),
                listOf(-sin(angleInRadians), 0f, cos(angleInRadians), 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        fun rotationZ(angleInRadians: Float) = Matrix(
            listOf(
                listOf(cos(angleInRadians), -sin(angleInRadians), 0f, 0f),
                listOf(sin(angleInRadians), cos(angleInRadians), 0f, 0f),
                listOf(0f, 0f, 1f, 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        fun shearing(x_y: Float, x_z: Float, y_x: Float, y_z: Float, z_x: Float, z_y: Float) = Matrix(
            listOf(
                listOf(1f, x_y, x_z, 0f),
                listOf(y_x, 1f, y_z, 0f),
                listOf(z_x, z_y, 1f, 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )

        val IdentityMatrix44 = Matrix(
            listOf(
                listOf(1f, 0f, 0f, 0f),
                listOf(0f, 1f, 0f, 0f),
                listOf(0f, 0f, 1f, 0f),
                listOf(0f, 0f, 0f, 1f),
            )
        )
    }

    operator fun get(rowIndex: Int, columnIndex: Int) = matrix[columnIndex + rowIndex * numberOfColumns]
    operator fun times(other: Matrix): Matrix {
        return Matrix(
            numberOfRows, other.numberOfColumns,
            matrixMultiplication(other.matrix, other.numberOfColumns)
        )
    }

    operator fun times(tuple: Tuple): Tuple {
        val (x, y, z, w) =
            matrixMultiplication(floatArrayOf(tuple.x, tuple.y, tuple.z, tuple.w), 1)
        return Tuple(x, y, z, w)
    }

    private fun matrixMultiplication(rhsMatrix: FloatArray, rhsNumberOfColumns: Int) =
        (0 until numberOfRows)
            .flatMap { row ->
                (0 until rhsNumberOfColumns)
                    .map { column ->
                        (0 until numberOfColumns)
                            .map { offset ->
                                matrix[offset + row * numberOfRows] *
                                        rhsMatrix[column + (offset) * rhsNumberOfColumns]
                            }
                            .sum()
                    }
            }
            .toFloatArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix
        if (!matrix.contentEquals(other.matrix)) return false
        return true
    }

    override fun hashCode(): Int {
        return matrix.hashCode()
    }

    override fun toString(): String {
        return "Matrix(matrix=${matrix.contentToString()}, numberOfRows=$numberOfRows, numberOfColumns=$numberOfColumns)"
    }

    fun transposed() = Matrix(
        numberOfColumns,
        numberOfRows,
        (0 until numberOfColumns)
            .flatMap { column ->
                (0 until numberOfRows).map { row ->
                    get(row, column)
                }

            }.toFloatArray()
    )

    private fun calculateDeterminant(): Float {
        return if (is2x2()) matrix[0] * matrix[3] - matrix[1] * matrix[2]
        else {
            (0 until numberOfColumns)
                .fold(0f) { acc, rowEntry ->
                    acc + cofactor(0, rowEntry) * get(0, rowEntry)
                }
        }
    }

    private fun is2x2() = numberOfRows == 2 && numberOfColumns == 2

    fun submatrix(rowExcluded: Int, columnExcluded: Int): Matrix {
        return Matrix(
            numberOfRows - 1,
            numberOfColumns - 1,
            (0 until numberOfRows)
                .filter { it != rowExcluded }
                .flatMap { row ->
                    (0 until numberOfColumns)
                        .filter { it != columnExcluded }
                        .map { column ->
                            get(row, column)
                        }
                }.toFloatArray()
        )
    }

    fun isInvertible() = calculateDeterminant() != 0f
    fun minor(row: Int, column: Int) = submatrix(row, column).calculateDeterminant()
    fun cofactor(row: Int, column: Int): Float = minor(row, column) *
            if ((row + column) % 2 == 0) 1f else -1f

    fun inverse() = Matrix(
        numberOfColumns,
        numberOfRows,
        (0 until numberOfColumns)
            .flatMap { column ->
                (0 until numberOfRows).map { row ->
                    1f / calculateDeterminant() * cofactor(row, column)
                }

            }.toFloatArray()
    )
}
