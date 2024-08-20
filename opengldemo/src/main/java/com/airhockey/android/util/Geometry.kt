package com.airhockey.android.util

class Geometry {
    companion object {
        class Point(x: Float, y: Float, z: Float) {
            var x = x
            var y = y
            var z = z

            fun translateY(distance: Float): Point {
                return Point(x, y + distance, z)
            }

            fun tranlate(vector: Vector): Point {
                return Point(x + vector.x, y + vector.y, z + vector.z)
            }

            override fun toString(): String {
                return "($x,$y,$z)"
            }
        }

        class Circle(center: Point, radius: Float) {
            val center: Point = center
            val radius = radius
            fun scale(scale: Float): Circle {
                return Circle(center, radius * scale);
            }

        }

        class Cylinder(center: Point, radius: Float, height: Float) {
            val center: Point = center
            val radius = radius
            var height = height

        }

        class Ray(point: Point, vector: Vector) {
            val point = point
            val vector = vector

        }

        class Vector(x:Float, y:Float, z:Float){
            val x = x
            val y = y
            val z = z

            fun length(): Float{
                return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            }

            fun crossProduct(other: Vector): Vector {
                return Companion.Vector(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
                )
            }

            fun dotProduct(other: Vector): Float {
                return x * other.x + y * other.y + z * other.z
            }

            fun scale(f: Float): Vector {
                return Vector(x * f, y * f, z * f)
            }
        }

        class Sphere(center: Point, radius: Float){
            val center = center
            val radius = radius
        }

        class Plane(point: Point, normal: Vector){
            val point = point
            val normal = normal
        }

        fun intersects(sphere: Sphere, ray: Ray):Boolean{
            return distanceBetween(sphere.center,ray) < sphere.radius
        }

        fun intersectionPoint(ray: Ray, plane: Plane): Point {
            var rayToPlaneVector = vectorBetween(ray.point, plane.point)
            var scaleFactor =
                rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
            var intersectionPoint = ray.point.tranlate(ray.vector.scale(scaleFactor))
            return intersectionPoint
        }

        fun distanceBetween(point: Point, ray: Ray): Float {
            var p1ToPoint = vectorBetween(ray.point, point)
            var p2ToPoint = vectorBetween(ray.point.tranlate(ray.vector), point)

            var areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
            var lengthOfBase = ray.vector.length()
            var distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase
            return distanceFromPointToRay
        }

        fun vectorBetween(from: Point, to: Point): Vector {
            return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
        }
    }
}