package soy.frank.rayTracer.groovyExtensions

import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.maths.ExtensionsKt
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple

// These address the mismatch between Groovy and Kotlin operators
// but only matter in tests
/*  |Kotlin     | Groovy    |
    |-----------|-----------|
    |times      | multiply  |
    |unaryMinus | negative  |
    |get        | getAt     |
*/

static float getAt(Matrix self, int row, int column) { self.get(row, column) }

static Matrix multiply(Matrix self, Matrix other) { self.times(other) }

static Tuple multiply(Matrix self, Tuple other) { self.times(other) }

static Tuple multiply(Float self, Tuple other) { ExtensionsKt.times(self, other) }

static Tuple multiply(Tuple self, float other) { self.times(other) }

static Tuple negative(Tuple self) { self.unaryMinus() }

static Color multiply(Float self, Color other) { ExtensionsKt.times(self, other) }

static Color multiply(Color self, Float other) { self.times(other) }

static Color multiply(Color self, Color other) { self.times(other) }
