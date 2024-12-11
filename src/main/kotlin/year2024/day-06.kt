package year2024

import Point
import readLines
import year2023.Directions
import java.io.File

val ERROR_POSITION = Point(-1, -1)

fun main() {
    val url = object {}.javaClass.getResource("/year2024/day06.txt")
    val file = File(url!!.toURI())

    val (grid, guard) = parseGrid(file.readText())
    val uniquePointsCount = moveGuard(grid, guard)
    println("Number of unique points visited: $uniquePointsCount")
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

fun Directions.turnRight(): Directions {
    return when (this) {
        Directions.NORTH -> Directions.EAST
        Directions.EAST -> Directions.SOUTH
        Directions.SOUTH -> Directions.WEST
        Directions.WEST -> Directions.NORTH
        Directions.ERROR -> Directions.ERROR
    }
}

fun parseGrid(input: String): Pair<Array<CharArray>, Guard> {
    val lines = input.readLines()
    val grid = Array(lines.size) { CharArray(lines[0].length) }
    var guard: Guard? = null

    for (y in lines.indices) {
        for (x in lines[y].indices) {
            grid[y][x] = lines[y][x]
            if (grid[y][x] in setOf('>', '^', '<', 'v')) {
                guard = Guard(Point(x, y), grid[y][x].toDirection())
                grid[y][x] = '.'
            }
        }
    }
    return Pair(grid, guard!!)
}

fun moveGuard(grid: Array<CharArray>, guard: Guard): Int {
    val visitedPoints = mutableSetOf<Point>()
    val directions = mapOf(
        Directions.NORTH to Point(0, -1),
        Directions.EAST to Point(1, 0),
        Directions.SOUTH to Point(0, 1),
        Directions.WEST to Point(-1, 0)
    )

    while (true) {
        visitedPoints.add(guard.position)
        val move = directions[guard.direction]!!
        val newPosition = Point(guard.position.x + move.x, guard.position.y + move.y)

        if (newPosition.y !in grid.indices || newPosition.x !in grid[0].indices) {
            break
        }

        if (grid[newPosition.y][newPosition.x] == '#') {
            guard.direction = guard.direction.turnRight()
        } else {
            guard.position = newPosition
        }
    }

    return visitedPoints.size
}

data class Guard(var position: Point, var direction: Directions)


/*------------------ SHAME BIN
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


data class Obstacle(var position: Point)

class ObstaclesAndGuardMap(
    val obstacles: MutableList<Obstacle> = mutableListOf(),
    var guard: Guard = Guard(ERROR_POSITION, Directions.ERROR),
    val size: Pair<Int, Int> // width, length
) {
    var distance = 0
    var visitedPoints : MutableSet<Point> = mutableSetOf()

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
        updateGuardAndDistance(obstacle)
        return obstacle
    }

    private fun updateGuardAndDistance(obstacle: Obstacle?) {
        //var addedDistance = 0
        var points : List<Point> = listOf()
        // Reached the end
        if (obstacle == null) {
            /*addedDistance = when (guard.direction) {
                Directions.NORTH -> guard.position.y
                Directions.SOUTH -> size.second - guard.position.y
                Directions.EAST -> guard.position.x
                Directions.WEST -> size.first - guard.position.x
                Directions.ERROR -> 0

            }*/
            points = when (guard.direction) {
                Directions.NORTH -> (0..guard.position.y).map{ Point(guard.position.x, it)}
                Directions.SOUTH -> (guard.position.y..size.second).map{ Point(guard.position.x, it)}
                Directions.EAST -> (guard.position.x.. size.first).map{ Point(it, guard.position.y)}
                Directions.WEST -> (0..guard.position.x).map{ Point(it, guard.position.y)}
                Directions.ERROR -> emptyList()
            }
            //distance += addedDistance
            if (points.isNotEmpty()) {
                visitedPoints.addAll(points)
            }
            return
        }
        when (guard.direction) {
            Directions.NORTH -> {
                guard.direction = Directions.EAST
                //addedDistance = guard.position.y - obstacle.position.y
                points = (obstacle.position.y +1..guard.position.y).map{ Point(guard.position.x, it)}
                guard.position = Point(guard.position.x, obstacle.position.y + 1)

            }

            Directions.SOUTH -> {
                guard.direction = Directions.WEST
                //addedDistance = obstacle.position.y - guard.position.y
                points = (guard.position.y..<obstacle.position.y).map{ Point(guard.position.x, it)}
                guard.position = Point(guard.position.x, obstacle.position.y - 1)
            }

            Directions.WEST -> {
                guard.direction = Directions.NORTH
                //addedDistance = guard.position.x - obstacle.position.x
                points = (obstacle.position.x +1 ..guard.position.x ).map{ Point(it, guard.position.y)}
                guard.position = Point(obstacle.position.x + 1, guard.position.y)
            }

            Directions.EAST -> {
                guard.direction = Directions.SOUTH
                //addedDistance =  obstacle.position.x - guard.position.x
                points = (guard.position.x..<obstacle.position.x).map{ Point(it, guard.position.y)}
                guard.position = Point(obstacle.position.x - 1, guard.position.y)
            }

            Directions.ERROR -> Directions.ERROR
        }
        //distance += addedDistance
        visitedPoints.addAll(points)
    }

} */