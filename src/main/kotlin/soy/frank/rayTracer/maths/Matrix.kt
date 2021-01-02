package soy.frank.rayTracer.maths

class Matrix {
    private val matrix: FloatArray
    val numberOfRows: Int
    val numberOfColumns: Int
    private var determinant: Float? = null

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

    fun determinant(): Float {
        return if (is2x2()) matrix[0] * matrix[3] - matrix[1] * matrix[2]
        else {
            if (this.determinant != null) return this.determinant!!
            val determinant = (0 until numberOfColumns)
                .fold(0f) { acc, rowEntry ->
                    acc + cofactor(0, rowEntry) * get(0, rowEntry)
                }
            this.determinant = determinant
            determinant
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

    fun isInvertible() = determinant() != 0f
    fun minor(row: Int, column: Int) = submatrix(row, column).determinant()
    fun cofactor(row: Int, column: Int): Float = minor(row, column) *
            if ((row + column) % 2 == 0) 1f else -1f

    fun inverse() = Matrix(
        numberOfColumns,
        numberOfRows,
        (0 until numberOfColumns)
            .flatMap { column ->
                (0 until numberOfRows).map { row ->
                    1f / determinant() * cofactor(row, column)
                }

            }.toFloatArray()
    )
}
