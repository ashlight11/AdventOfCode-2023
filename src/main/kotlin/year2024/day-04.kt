package year2024

import Point
import year2023.parse
import java.io.File

fun main(){
    val url = object {}.javaClass.getResource("/year2024/day04.txt")
    val file = File(url!!.toURI())
    val letterGrid = file.readText().parseAsLetterGrid()
    val allXs = letterGrid.getAll('X')
    val result = allXs.values.sumOf { it.countXmas(letterGrid) }
    print(result)
}

fun String.parseAsLetterGrid(): LetterGrid {
    val lines = this.split("\\s+".toRegex())
    val width = lines[0].length

    return lines
        .joinToString("")
        .foldIndexed(LetterGrid()) { column, letters, char ->
            val x = column % width
            val y = column / width
            val point = Point(x, y)
            letters.addLetter(point, Letter(char, point))
        }
}

/**
 * TODO :  rajouter la notion de direction lors du parcours
 */
fun Letter.countXmas(letterGrid: LetterGrid) : Int{
    val directions = listOf(
        Point(-1, -1), Point(-1, 0), Point(-1, 1),
        Point(0, -1), /*Point(0, 0),*/ Point(0, 1),
        Point(1, -1), Point(1, 0), Point(1, 1)
    )
    var count = 0
    for (direction in directions) {
        if (checkXmas(letterGrid, this.position, direction)) {
            count++
        }
    }
    return count
}

fun checkXmas(grid: LetterGrid, start: Point, direction: Point): Boolean {
    val word = "XMAS"
    for (i in word.indices) {
        val point = Point(start.x + i * direction.x, start.y + i * direction.y)
        if (grid.getLetterAt(point)?.value != word[i]) {
            return false
        }
    }
    return true
}

/**
 * Start at X
 * Find all connections around X
 * Find all connections around connections
 * Stop when
 */

/*

fun foundXmas(letter : Letter, letterGrid: LetterGrid) : Int {
    val connections = letter.getConnectionsInGrid(letterGrid)
    if(connections.isEmpty()){
        return 0
    }
    val numberOfS = connections.count{it.value == 'S'}
    val numberOfSInConnections = connections.sumOf { foundXmas(it, letterGrid) }

    return numberOfS + numberOfSInConnections

} */

class LetterGrid(val letters : MutableMap<Point, Letter> = mutableMapOf()){
    fun addLetter(point: Point, letter: Letter): LetterGrid {
        letters[point] = letter
        return this
    }

    fun getAll(charValue : Char) : Map<Point, Letter> {
        return letters.filter { letter -> letter.value.value == charValue }
    }

    fun getLetterAt(point : Point) : Letter? {
        return letters[point]
    }

}

data class Letter(val value : Char, val position : Point)