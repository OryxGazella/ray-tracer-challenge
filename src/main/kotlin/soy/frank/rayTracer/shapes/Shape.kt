package soy.frank.rayTracer.shapes

import soy.frank.rayTracer.maths.Ray
import soy.frank.rayTracer.soy.frank.rayTracer.maths.Intersection

interface Shape {
    fun intersects(ray: Ray): Array<Intersection>
}