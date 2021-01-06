package soy.frank.rayTracer

import groovy.transform.Field
import soy.frank.rayTracer.maths.Color
import soy.frank.rayTracer.maths.Matrix
import soy.frank.rayTracer.maths.Ray
import soy.frank.rayTracer.maths.Tuple
import soy.frank.rayTracer.rendering.Canvas
import soy.frank.rayTracer.shapes.Sphere
import soy.frank.rayTracer.soy.frank.rayTracer.maths.Intersection

@Field static def map = [:]

static Tuple asTuple(String tuple) { map[tuple] as Tuple }

static Color asColor(String color) { map[color] as Color }

static Matrix asMatrix(String matrix) { map[matrix] as Matrix }

static Canvas asCanvas(String canvas) { map[canvas] as Canvas }

static Ray asRay(String ray) { map[ray] as Ray }

static Sphere asSphere(String sphere) { map[sphere] as Sphere }

static Intersection asIntersection(String intersection) { map[intersection] as Intersection }