package year2023

import LongPoint
import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day11.txt")
    val file = File(url!!.toURI())
    val sky = Sky(file.readText().findGalaxies(), 999999)
    println(sky.computeSumOfAllPaths())

}

fun String.findGalaxies(): List<Galaxy> {
    val lines = this.lines()
    val width = lines.size

    return lines
        .joinToString("")
        .asSequence()
        .mapIndexed { index, char ->
            if (char == '#') {
                Galaxy(LongPoint((index % width).toLong(), (index / width).toLong()))
            } else {
                null
            }
        }.filterNotNull()
        .toList()
}

fun LongPoint.computeShortestPathTo(other: LongPoint): Long {
    return abs(other.x - this.x) + abs(other.y - this.y)
}

data class Galaxy(val position: LongPoint) {
    var adjustedPosition = position
}

data class Sky(val galaxies: List<Galaxy>, val multiplicationFactor : Long) {
    // Columns with no galaxies are the one where there is no- galaxy at x for x between x and width
    val columns =
        (0..galaxies.maxOf { it.position.x })
            .filterNot { intValue -> intValue in galaxies.map { it.position.x } }

    // Same for lines with y
    val lines = (0..galaxies.maxOf { it.position.y })
        .filterNot { intValue -> intValue in galaxies.map { it.position.y } }

    val adjustedGalaxies = getAdjustedPositions()

    // We need to add as many columns to x as there are before position.x
    // and as columns as there are before y
    private fun getAdjustedPositions(): List<LongPoint> {
        galaxies.forEach {
            it.adjustedPosition = LongPoint(
                x = it.position.x + multiplicationFactor * (columns.count { intValue -> intValue < it.position.x }),
                y = it.position.y + multiplicationFactor * (lines.count { intValue -> intValue < it.position.y })
            )
        }
        return galaxies.map { it.adjustedPosition }
    }

    /**
     * G1, G2, G3 -> G1-G2 G1-G3 G2-G3
     * G2-G1 G2-G3 G3-G1 --> So div 2
     */
    fun computeSumOfAllPaths(): Long {
        val galaxiesIterator = adjustedGalaxies.iterator()
        var pairs: List<Pair<LongPoint, LongPoint>> = listOf()
        while (galaxiesIterator.hasNext()) {
            val current = galaxiesIterator.next()
            val additionalList = adjustedGalaxies
                .filterNot { it == current }
                .map {
                    current to it
                }
            pairs = pairs + additionalList
        }
        return pairs.sumOf { it.second.computeShortestPathTo(it.first) }.div(2)
    }
}