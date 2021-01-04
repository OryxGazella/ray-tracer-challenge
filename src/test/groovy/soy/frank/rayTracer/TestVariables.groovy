package soy.frank.rayTracer

import groovy.transform.Field
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Tuple
import soy.frank.rayTracer.rendering.Canvas

@Field static def map = [:]

static Tuple asTuple(tuple) { map[tuple] as Tuple }

static Color asColor(color) { map[color] as Color }

static Matrix asMatrix(matrix) { map[matrix] as Matrix }

static Canvas asCanvas(canvas) { map[canvas] as Canvas}