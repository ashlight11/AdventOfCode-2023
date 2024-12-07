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
        if(matches){
            update
        } else {
            null
        }
    }
    val result = matchingUpdates.sumOf {
        it[it.size / 2]
    }
    println("Part one : $result")


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
        if(condition){
            return false
        }
    }
    return true
}

/**fun List<Int>.forceMatching(rules: List<Rule>): List<Int>{

}**/

data class Rule(var before: Int, var after: Int) {
    fun isOpposite(other: Rule): Boolean {
        return this.before == other.after && this.after == other.before
    }

    fun swap(){
        val temp = this.before
        this.before = this.after
        this.after = temp
    }
}