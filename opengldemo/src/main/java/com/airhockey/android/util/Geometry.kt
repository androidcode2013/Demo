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
    }
}