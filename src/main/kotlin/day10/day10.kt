package day10

import utils.Direction
import utils.readInput
import utils.Point
import utils.pointInPolygon

fun main() {
    val lines = readInput("day10/input")

    firstStar(lines)
    secondStar(lines)
}

private fun firstStar(lines: List<String>) {
    val map = lines.toMap()
    val path = findPath(map, lines.findStartPosition())

    val maxDistance = path.size / 2

    println("First  ⭐: $maxDistance")
}

private fun secondStar(lines: List<String>) {
    val map = lines.toMap()
    val path = findPath(map, lines.findStartPosition())
    var sum = 0L

    map.forEachIndexed { row, line ->
        line.forEachIndexed { column, _ ->
            val point = Point(x = column, y = row)
            if (!path.contains(point) && path.pointInPolygon(point)) {
                sum++
            }
        }
    }

    println("Second ⭐: $sum")
}

private fun List<String>.toMap() =
    this.filter { it.isNotBlank() }.map { line -> line.toCharArray().toTypedArray() }.toTypedArray()

private fun List<String>.findStartPosition(): Point {
    val row = this.indexOfFirst { it.contains('S') }
    val column = this[row].indexOf('S')
    return Point(x = column, y = row)
}

private fun findPath(map: Array<Array<Char>>, startPosition: Point): Array<Point> {
    var currentPosition = startPosition

    val path = mutableListOf(
        currentPosition
    )

    var nextPosition = map.findInitialDirection(currentPosition)
    var nextChar = map[nextPosition.x][nextPosition.y]

    while (nextChar != 'S') {
        val xDifference = (currentPosition.x + 1) - (nextPosition.x + 1)
        val yDifference = (currentPosition.y + 1) - (nextPosition.y + 1)

        currentPosition = nextPosition

        nextPosition = currentPosition + when (nextChar) {
            'J' -> if (xDifference == 0) Direction.Left else Direction.Up
            'F' -> if (xDifference == 0) Direction.Right else Direction.Down
            'L' -> if (xDifference == 0) Direction.Right else Direction.Up
            '7' -> if (xDifference == 0) Direction.Left else Direction.Down
            '-' -> if (xDifference > 0) Direction.Left else Direction.Right
            '|' -> if (yDifference > 0) Direction.Up else Direction.Down
            else -> throw Exception("No path found $nextChar")
        }
        path.add(currentPosition)
        nextChar = map[nextPosition.y][nextPosition.x]
    }

    return path.toTypedArray()
}

private fun Array<Array<Char>>.findInitialDirection(position: Point): Point {
    return position + when{
        position.x > 0 && this[position.y][position.x - 1] in listOf('F', 'L', '-') -> Direction.Left
        position.x < this.first().size && this[position.y][position.x + 1] in listOf('7', 'J', '-') -> Direction.Right
        position.y > 0 && this[position.y -1][position.x] in listOf('|', 'F', '7') -> Direction.Up
        position.y < this.size && this[position.y +1][position.x] in listOf('L', 'J', '|') -> Direction.Down
        else -> throw Exception("No path found")
    }
}