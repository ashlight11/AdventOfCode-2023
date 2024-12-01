package year2024

import java.io.File
import kotlin.math.abs


fun main() {
    val url = object {}.javaClass.getResource("/year2024/day01.txt")
    val file = File(url!!.toURI())
    val initialList = file.readText().parseAsIntList()
    val rightList = initialList.filterIndexed { index, _ -> index % 2 == 0 }.sorted()
    val leftList = initialList.filterIndexed { index, _ -> index % 2 == 1 }.sorted()
    val result = rightList.foldIndexed(initial = 0,
        operation = ({ index, acc, current ->
            acc + abs(current - leftList[index])
        })
    )
    println("Difference $result")

    val similarityScore = calculateSimilarityScore(leftList, rightList)

    println("Similarity score $similarityScore")

}

fun String.parseAsIntList(): List<Int> {
    return this.split(Regex("[,\\s]+")).map { it.toInt() }
}

/**
 * Calculate a total similarity score by adding up each number in the left list
 * after multiplying it by the number of times that number appears in the right list.
 */

fun calculateSimilarityScore(leftList: List<Int>, rightList: List<Int>): Long {
    return leftList.sumOf { leftNumber ->
        rightList.count { it == leftNumber } * leftNumber
    }.toLong()
}