import java.io.File

val ERROR_PAIR = Pair(Point(-1, -1), PipePart('?'))

fun main() {
    val url = object {}.javaClass.getResource("day10.txt")
    val file = File(url!!.toURI())
    val field = file.readText().parse()
    val loopSize = field.findPath()
    println("size $loopSize")


}

data class PipeField(val pipes: MutableMap<Point, PipePart> = mutableMapOf()) {


    fun addPipe(point: Point, pipePart: PipePart): PipeField {
        pipes[point] = pipePart
        return this
    }

    fun getStartingPoint(): Pair<Point, PipePart> {
        val result = pipes.firstNotNullOfOrNull { it.takeIf { it.value.value == 'S' } }
        result?.let {
            val possibleDirections = getPossibleDirectionForStartingPoint(it.key)
            it.value.listOfConnections = possibleDirections
            pipes[it.key] = it.value
            return it.toPair()
        }
            ?: return ERROR_PAIR
    }

    /**
     * S can have different directions depending on its neighbours
     * [0,1] : if contains NORTH -> SOUTH is possible
     * [0,-1] : if contains SOUTH -> NORTH is possible
     * [1,0] : if contains WEST -> EAST is possible
     * [-1,0] : if contains EAST -> WEST is possible
     */
    fun getPossibleDirectionForStartingPoint(startingPoint: Point): List<Directions> {
        var directions: List<Directions> = emptyList()
        val x = startingPoint.x
        val y = startingPoint.y
        pipes[Point(x, y - 1)]?.let {
            if (it.listOfConnections.contains(Directions.SOUTH)) {
                directions = directions.plus(Directions.NORTH)
            }
        }
        pipes[Point(x, y + 1)]?.let {
            if (it.listOfConnections.contains(Directions.NORTH)) {
                directions = directions.plus(Directions.SOUTH)
            }
        }
        pipes[Point(x - 1, y)]?.let {
            if (it.listOfConnections.contains(Directions.EAST)) {
                directions = directions.plus(Directions.WEST)
            }
        }
        pipes[Point(x + 1, y)]?.let {
            if (it.listOfConnections.contains(Directions.WEST)) {
                directions = directions.plus(Directions.EAST)
            }
        }
        return directions

    }

    fun findPath() : Int {
        val startingPoint = getStartingPoint()
        val adjacentPoints = getAdjacentPoints(startingPoint)
        var current = adjacentPoints[0]
        val invertedDirectionsForStartingPoints = startingPoint.second.listOfConnections.map {
            when(it){
                Directions.NORTH -> Directions.SOUTH
                Directions.SOUTH -> Directions.NORTH
                Directions.EAST -> Directions.WEST
                Directions.WEST -> Directions.EAST
            }
        }
        var incomingDirection = pipes[current]!!.listOfConnections.intersect(invertedDirectionsForStartingPoints.toSet()).first()
        var loopSize = 0
        while (current != startingPoint.first) {
            val result = goToNext(current, incomingDirection)
            current = result.first
            incomingDirection = result.second
            loopSize++
        }
        return loopSize + 1
    }

    private fun goToNext(currentPoint: Point, incomingDirection: Directions): Pair<Point, Directions> {
        println("current Pipe " + pipes[currentPoint]!! + "came from $incomingDirection" )
        val x = currentPoint.x
        val y = currentPoint.y

        val remainingDirection = pipes[currentPoint]!!.listOfConnections.minus(incomingDirection).first()
        println("let's go $remainingDirection" )
        return when (remainingDirection) {
            Directions.NORTH -> {
                Point(x, y - 1) to Directions.SOUTH
            }

            Directions.SOUTH -> {
                Point(x, y + 1) to Directions.NORTH
            }

            Directions.EAST -> {
                Point(x + 1, y) to Directions.WEST
            }

            Directions.WEST -> {
                Point(x - 1, y) to Directions.EAST
            }
        }
    }

    /**
     * Find adjacent nodes :
     * Given a node A
     *
     * for each element of directions
     *  when (direction)
     *      NORTH : [0,-1] has SOUTH ? add node [0,-1]
     *      SOUTH : [0,1] has NORTH ? add node [0,1]
     *      EAST : [1,0] has WEST ? add node [1,0]
     *      WEST : [-1,0] has EAST ? add node [-1,0]
     */
    fun getAdjacentPipes(pipePart: Pair<Point, PipePart>): Map<Point, PipePart> {
        val adjacentPipes: Map<Point, PipePart> =
            pipePart.second.listOfConnections.fold(emptyMap()) { currentMap, direction ->
                val x = pipePart.first.x
                val y = pipePart.first.y
                var map: Map<Point, PipePart> = emptyMap()
                when (direction) {
                    Directions.NORTH -> {
                        val northPoint = Point(x, y - 1)
                        val northPipePart = this.pipes[northPoint]
                        if (northPipePart != null) {
                            if (northPipePart.listOfConnections.contains(Directions.SOUTH)) {
                                map = mapOf(Pair(northPoint, northPipePart))
                            }
                        }
                    }

                    Directions.SOUTH -> {
                        val southPoint = Point(x, y + 1)
                        val southPipePart = this.pipes[southPoint]
                        if (southPipePart != null) {
                            if (southPipePart.listOfConnections.contains(Directions.NORTH)) {
                                map = mapOf(southPoint to southPipePart)
                            }
                        }
                    }

                    Directions.EAST -> {
                        val eastPoint = Point(x + 1, y)
                        val eastPipePart = this.pipes[eastPoint]
                        if (eastPipePart != null) {
                            if (eastPipePart.listOfConnections.contains(Directions.WEST)) {
                                map = mapOf(eastPoint to eastPipePart)
                            }
                        }
                    }

                    else -> {
                        val westPoint = Point(x - 1, y)
                        val westPipePart = this.pipes[westPoint]
                        if (westPipePart != null) {
                            if (westPipePart.listOfConnections.contains(Directions.EAST)) {
                                map = mapOf(westPoint to westPipePart)
                            }
                        }
                    }
                }
                currentMap.plus(map)
            }
        return adjacentPipes
    }

    fun getAdjacentPoints(pipePart: Pair<Point, PipePart>): List<Point> {
        val x = pipePart.first.x
        val y = pipePart.first.y
        val resultingList: MutableList<Point> = emptyList<Point>().toMutableList()
        pipePart.second.listOfConnections.forEach { direction ->
            when (direction) {
                Directions.NORTH -> {
                    val northPoint = Point(x, y - 1)
                    val northPipePart = this.pipes[northPoint]
                    if (northPipePart != null) {
                        if (northPipePart.listOfConnections.contains(Directions.SOUTH)) {
                            resultingList.add(northPoint)
                        }
                    }
                }

                Directions.SOUTH -> {
                    val southPoint = Point(x, y + 1)
                    val southPipePart = this.pipes[southPoint]
                    if (southPipePart != null) {
                        if (southPipePart.listOfConnections.contains(Directions.NORTH)) {
                            resultingList.add(southPoint)
                        }
                    }
                }

                Directions.EAST -> {
                    val eastPoint = Point(x + 1, y)
                    val eastPipePart = this.pipes[eastPoint]
                    if (eastPipePart != null) {
                        if (eastPipePart.listOfConnections.contains(Directions.WEST)) {
                            resultingList.add(eastPoint)
                        }
                    }
                }

                else -> {
                    val westPoint = Point(x - 1, y)
                    val westPipePart = this.pipes[westPoint]
                    if (westPipePart != null) {
                        if (westPipePart.listOfConnections.contains(Directions.EAST)) {
                            resultingList.add(westPoint)
                        }
                    }
                }
            }
        }
        return resultingList
    }


    /**
     * Depth First Traversal Algorithm
     *
     * Visit all adjacent nodes recursively until you are back to the starting node
     *
     * For a given node
     *  Mark the node as visited
     *  Find all adjacent nodes
     *
     * For each adjacent node
     *  If not visited
     *      Call function recursively
     *
     *  If current is first
     *     return true
     *
     */

    fun findLoopSize(): Int {
        val startingPipe = getStartingPoint()
        val visitedPoints = emptyList<Point>().toMutableList()
        var loopSizes: IntArray = intArrayOf()
        visitedPoints.add(startingPipe.first)

        val adjacentPoints = getAdjacentPoints(startingPipe)
        for (currentPoint in adjacentPoints) {
            //visitedPoints.add(currentPoint.key)
            println("going for $currentPoint")
            val loopSize = exploreMap(startingPipe.first, currentPoint, visitedPoints, 0)
            //println("adding $loopSize")
            if (loopSize != 0) {
                loopSizes = loopSizes.plus(loopSize)
                //visitedPoints.remove(currentPoint)
            } else {
                //visitedPoints.remove(currentPoint)
            }
        }
        return loopSizes.max()
    }
    /*
            while (adjacentPoints.hasNext()) {
            val currentPoint = adjacentPoints.next()
            //visitedPoints.add(currentPoint.key)
            println("going for $currentPoint")
            val loopSize = exploreMap(startingPipe.first, currentPoint, visitedPoints, 0)
            //println("adding $loopSize")
            if (loopSize != 0) {
                loopSizes = loopSizes.plus(loopSize)
                //visitedPoints.remove(currentPoint)
            } else {
                //visitedPoints.remove(currentPoint)
            }
        }
     */

    private fun exploreMap(
        parentPoint: Point,
        currentPoint: Point,
        visitedPoints: MutableList<Point>,
        loopSize: Int
    ): Int {
        val adjacentPoints = getAdjacentPoints(currentPoint to pipes[currentPoint]!!)
        visitedPoints.add(currentPoint)
        //adjacentPoints.forEach { println(it) }
        for (current in adjacentPoints) {
            val wasVisited = visitedPoints.contains(current)
            //println("step $loopSize, current $current, is contained : $wasVisited")
            if (!wasVisited) {
                exploreMap(parentPoint, current, visitedPoints, loopSize + 1)
                println("step $loopSize, $current")
                //println(visitedPoints)
                break;
            } else if (current != currentPoint && current == parentPoint && loopSize > 2) {
                //println("found start and visited : $visitedPoints")
                return visitedPoints.size
            }
        }
        return visitedPoints.size
    }
}

/*
while (adjacentPoints.hasNext()) {
            val current = adjacentPoints.next()
            val wasVisited = visitedPoints.contains(current)
            //println("step $loopSize, current $current, is contained : $wasVisited")
            if (!wasVisited) {
                exploreMap(parentPoint, current, visitedPoints, loopSize + 1)
                println("step $loopSize, $current")
                //println(visitedPoints)
                break;
            } else if (current != currentPoint && current == parentPoint && loopSize>2) {
                //println("found start and visited : $visitedPoints")
                return visitedPoints.size
            }
        }
 */

/*
Version Marianne
val allPipes = mutableMapOf<Point, PipePart>()
lines.forEachIndexed { index, line ->
        allPipes.putAll(line.toPipePartLocated(index))
    }
 */

/*
Version https://github.com/legzo/advent-of-code/blob/master/src/main/kotlin/v2023/Day03.kt
 */
fun String.parse(): PipeField {
    val lines = this.split("\\s+".toRegex())
    val width = lines[0].length

    return lines
        .joinToString("")
        .foldIndexed(PipeField()) { column, currentPipe, char ->
            val x = column % width
            val y = column / width
            currentPipe.addPipe(Point(x, y), PipePart(char).toSubclass())
        }
}

data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

open class PipePart(val value: Char) {
    open var listOfConnections: List<Directions> = emptyList()
    fun toSubclass(): PipePart {
        return when (value) {
            '|' -> VerticalPipe(value)
            '-' -> HorizontalPipe(value)
            'L' -> LPipe(value)
            'J' -> JPipe(value)
            '7' -> SevenPipe(value)
            'F' -> FPipe(value)
            'S' -> SPipe(value)
            else -> Ground(value)
        }
    }

    override fun toString(): String {
        return "'$value'"
    }
}

/**
 * Represents | and connects north and south
 */
class VerticalPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.NORTH, Directions.SOUTH)
}

/**
 * Represents - and connects east and west
 */
class HorizontalPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.EAST, Directions.WEST)
}

/**
 * Represents L and connects north and east
 */
class LPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.NORTH, Directions.EAST)
}

/**
 * Represents J and connects north and west
 */
class JPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.NORTH, Directions.WEST)
}

/**
 * Represents 7 and connects north and east
 */
class SevenPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.SOUTH, Directions.WEST)
}

/**
 * Represents F and connects north and east
 */
class FPipe(value: Char) : PipePart(value) {
    override var listOfConnections: List<Directions> = listOf(Directions.SOUTH, Directions.EAST)
}

/**
 * Represents S and is the starting point => Connects anything
 */
class SPipe(value: Char) :
    PipePart(value) {
    override var listOfConnections: List<Directions> =
        listOf(Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST)
}

/**
 * Represents . and connects nothing
 */
class Ground(value: Char) : PipePart(value)

enum class Directions {
    NORTH,
    SOUTH,
    EAST,
    WEST
}
