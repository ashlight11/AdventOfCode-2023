package year2024

import parseAsIntList
import readLines
import java.io.File

fun main() {
    val url = object {}.javaClass.getResource("/year2024/day05.txt")
    val file = File(url!!.toURI())
    val parts = file.readText().split(Regex("(\r\n|\n\n|\r\r){2,}"))
    val rules = parts[0].readLines().parseToRules()
    val updates = parts[1].readLines().map { it.parseAsIntList(Regex(",")) }
    val matchingUpdates = updates.mapNotNull { update ->
        val matches = update.matchesRules(rules)
        if (matches) {
            update
        } else {
            null
        }
    }
    val result = matchingUpdates.sumOf {
        it[it.size / 2]
    }
    println("Part one : $result")

    val nonMatchingUpdates = updates.filterNot { it in matchingUpdates }
    val forcedUpdates = nonMatchingUpdates.map { it.forceMatching(rules) }
    val resultPartTwo = forcedUpdates.sumOf {
        it[it.size / 2]
    }
    println("Part two : $resultPartTwo")
}

fun List<String>.parseToRules(): List<Rule> {
    /**
     * Format is "int|int"
     */
    return this.mapNotNull { line ->
        val splitLine = line.split("|")
        if (splitLine.size == 2) {
            Rule(splitLine[0].trim().toInt(), splitLine[1].trim().toInt())
        } else null
    }
}

fun List<Int>.matchesRules(rules: List<Rule>): Boolean {
    val windowedRules = this.windowed(2, 1).map { Rule(it[0], it[1]) }
    rules.forEach { rule ->
        val condition = windowedRules.any { it.isOpposite(rule) }
        if (condition) {
            return false
        }
    }
    return true
}

/**
 * If doesn't match, swap
 */
fun List<Int>.forceMatching(rules: List<Rule>): List<Int> {
    val windowedRules = this.windowed(2, 1).map { Rule(it[0], it[1]) }
    val resultingList = this.toMutableList()
    var oppositeIndices: List<Int>
    rules.forEach { rule ->
        oppositeIndices =
            windowedRules.mapIndexedNotNull { currentIndex, it -> if (it.isOpposite(rule)) currentIndex else null }
        oppositeIndices.forEach {
            val temp = resultingList[it]
            resultingList[it] = resultingList[it + 1]
            resultingList[it + 1] = temp
        }
    }
    return if (resultingList.matchesRules(rules)) {
        resultingList
    } else {
        resultingList.forceMatching(rules)
    }

}

data class Rule(var before: Int, var after: Int) {
    fun isOpposite(other: Rule): Boolean {
        return this.before == other.after && this.after == other.before
    }
}