package utils

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(
    val x: Long,
    val y: Long
) {
    infix operator fun plus(direction: Direction): Point {
        return Point(
            x = direction.x + this.x,
            y = direction.y + this.y
        )
    }

    fun distanceTo(other: Point): Long {
        return abs(other.x - this.x) + abs(other.y - this.y)
    }
}

fun Array<Point>.pointInPolygon(point: Point): Boolean {
    val eps = 0.000001
    var crossings = 0

    repeat(this.size) {
        val minX = min(this[it].x, this[(it + 1) % this.size].x)
        val maxX = max(this[it].x, this[(it + 1) % this.size].x)

        if (point.x in minX + 1..maxX) {
            val dx = (this[(it + 1) % this.size].x - this[it].x).toFloat()
            val dy = (this[(it + 1) % this.size].y - this[it].y).toFloat()

            val k = if (abs(dx) < eps) {
                (9999999999).toFloat()
            } else {
                dy / dx
            }

            val m = this[it].y - k * this[it].x
            val y2 = k * point.x + m

            if (point.y <= y2) {
                crossings += 1
            }
        }
    }

    return crossings % 2 == 1
}

sealed interface Direction {
    val x: Int
    val y: Int

    data object Up : Direction {
        override val x = 0
        override val y = -1
    }

    data object Left : Direction {
        override val x = -1
        override val y = 0
    }

    data object Right : Direction {
        override val x = 1
        override val y = 0
    }

    data object Down : Direction {
        override val x = 0
        override val y = 1
    }
}