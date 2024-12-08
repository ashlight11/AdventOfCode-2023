package year2024

import Point
import year2023.Directions
import java.io.File

val ERROR_POSITION = Point(-1, -1)

fun main() {
    val url = object {}.javaClass.getResource("/year2024/day06.txt")
    val file = File(url!!.toURI())
}

fun Char.toDirection(): Directions {
    return when (this) {
        '>' -> Directions.EAST
        '^' -> Directions.NORTH
        '<' -> Directions.WEST
        'v' -> Directions.SOUTH
        else -> Directions.ERROR
    }
}

fun String.parseAsMap(): ObstaclesAndGuardMap {
    val lines = this.split("\\s+".toRegex())
    val width = lines[0].length

    return lines
        .joinToString("")
        .foldIndexed(ObstaclesAndGuardMap(size = Pair(width, lines.size))) { column, obstacles, char ->
            val x = column % width
            val y = column / width
            val point = Point(x, y)
            when (char) {
                '#' -> obstacles.addObstacle(point)
                '>', '<', 'v', '^' -> {
                    obstacles.guard = Guard(point, char.toDirection())
                    obstacles
                }

                else -> obstacles
            }

        }
}

class ObstaclesAndGuardMap(
    val obstacles: MutableList<Obstacle> = mutableListOf(),
    var guard: Guard = Guard(ERROR_POSITION, Directions.ERROR),
    val size: Pair<Int, Int> // width, length
) {
    var distance = 0
    fun addObstacle(point: Point): ObstaclesAndGuardMap {
        obstacles.add(Obstacle(point))
        return this
    }

    fun findNextObstacle(): Obstacle? {
        val obstacle = when (guard.direction) {
            Directions.NORTH -> obstacles.find { it.position.x == guard.position.x && it.position.y < guard.position.y }
            Directions.SOUTH -> obstacles.find { it.position.x == guard.position.x && it.position.y > guard.position.y }
            Directions.WEST -> obstacles.find { it.position.x < guard.position.x && it.position.y == guard.position.y }
            Directions.EAST -> obstacles.find { it.position.x > guard.position.x && it.position.y == guard.position.y }
            Directions.ERROR -> Obstacle(ERROR_POSITION)
        }
        return obstacle
    }

    fun updateGuardAndDistance(obstacle: Obstacle?) {
        var addedDistance = 0
        if (obstacle == null) {
            addedDistance = when (guard.direction) {
                Directions.NORTH -> guard.position.y
                Directions.SOUTH -> size.second - guard.position.y
                Directions.EAST -> guard.position.x
                Directions.WEST -> size.first - guard.position.x
                Directions.ERROR -> 0

            }
            distance += addedDistance
            return
        }
        when (guard.direction) {
            Directions.NORTH -> {
                guard.direction = Directions.EAST
                addedDistance = guard.position.y - obstacle.position.y
                guard.position = Point(guard.position.x, obstacle.position.y + 1)

            }

            Directions.SOUTH -> {
                guard.direction = Directions.WEST
                addedDistance = obstacle.position.y - guard.position.y
                guard.position = Point(guard.position.x, obstacle.position.y - 1)
            }

            Directions.WEST -> {
                guard.direction = Directions.NORTH
                addedDistance = obstacle.position.x - guard.position.x
                guard.position = Point(obstacle.position.x + 1, guard.position.y)
            }

            Directions.EAST -> {
                guard.direction = Directions.SOUTH
                addedDistance = guard.position.x - obstacle.position.x
                guard.position = Point(obstacle.position.x - 1, guard.position.y)
            }

            Directions.ERROR -> Directions.ERROR
        }
        distance += addedDistance
    }


}

data class Guard(var position: Point, var direction: Directions)

data class Obstacle(var position: Point)