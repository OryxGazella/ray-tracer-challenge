package soy.frank.rayTracer.shapes

import soy.frank.rayTracer.maths.Ray
import soy.frank.rayTracer.maths.Tuple
import soy.frank.rayTracer.soy.frank.rayTracer.maths.Intersection
import kotlin.math.pow

class Sphere : Shape {
    override fun intersects(ray: Ray): Array<Intersection> {
        val sphereToRay = ray.origin - Tuple.point(0f, 0f, 0f)
        val a = ray.direction dot ray.direction
        val b = 2f * (ray.direction dot sphereToRay)
        val c = (sphereToRay dot sphereToRay) - 1f
        val del = b.pow(2) - 4 * a * c
        return if (del >= 0)
            arrayOf(
                Intersection((-b - del.pow(0.5f))/(2 * a), this),
                Intersection((-b + del.pow(0.5f))/(2 * a), this),
            )
        else
            arrayOf()
    }
}