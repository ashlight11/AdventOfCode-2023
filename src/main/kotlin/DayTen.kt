import java.io.File

val ERROR_PAIR = Pair(Point(-1,-1), PipePart('?'))

fun main() {
    val url = object {}.javaClass.getResource("day09.txt")
    val file = File(url!!.toURI())
    val field = file.readText().parse()

}

data class PipeField(val pipes: MutableMap<Point, PipePart> = mutableMapOf()) {

    fun addPipe(point: Point, pipePart: PipePart): PipeField {
        pipes[point] = pipePart
        return this
    }

    fun getStartingPoint(): Pair<Point, PipePart> {
        val result = pipes.firstNotNullOfOrNull { it.takeIf { it.value.value == 'S' }}?.toPair()
        result?.let { return it } ?: return ERROR_PAIR
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
        val adjacentPipes : Map<Point, PipePart> = pipePart.second.listOfConnections.fold(emptyMap()) { currentMap, direction ->
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

    fun findLoop() {

    }
}

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

fun String.toPipePartLocated(lineIndex: Int): Map<Point, PipePart> {
    return this.mapIndexed { index, char -> Point(index, lineIndex) to PipePart(value = char) }.toMap()
}

data class PipePartLocated(val pipePart: PipePart, val point: Point) {
    override fun toString(): String {
        return "$pipePart at $point"
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
