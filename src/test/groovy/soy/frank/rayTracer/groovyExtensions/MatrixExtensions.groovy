package soy.frank.rayTracer.groovyExtensions

import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

//At the time of writing IntelliJ thinks this is unused
// groovy access looks for getAt instead of Kotlin's get
@SuppressWarnings("unused")
class MatrixExtensions {
    static float getAt(Matrix self, int row, int column) {
        self.get(row, column)
    }

    static Matrix multiply(Matrix self, Matrix other) {
        self.times(other)
    }

    static Tuple multiply(Matrix self, Tuple other) {
        self.times(other)
    }
}
