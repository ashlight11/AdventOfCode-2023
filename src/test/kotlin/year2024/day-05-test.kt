package year2024

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import parseAsIntList
import readLines

class Day05Test {
    private val testInput =
        "47|53\n" +
                "97|13\n" +
                "97|61\n" +
                "97|47\n" +
                "75|29\n" +
                "61|13\n" +
                "75|53\n" +
                "29|13\n" +
                "97|29\n" +
                "53|29\n" +
                "61|53\n" +
                "97|53\n" +
                "61|29\n" +
                "47|13\n" +
                "75|47\n" +
                "97|75\n" +
                "47|61\n" +
                "75|61\n" +
                "47|29\n" +
                "75|13\n" +
                "53|13\n" +
                "\n" +
                "75,47,61,53,29\n" +
                "97,61,53,29,13\n" +
                "75,29,13\n" +
                "75,97,47,61,53\n" +
                "61,13,29\n" +
                "97,13,75,29,47"

    val parts = testInput.split(Regex("\\n{2,}"))
    val rules = parts[0].readLines().parseToRules()
    val updates = parts[1].readLines().map { it.parseAsIntList(Regex(",")) }

    @Test
    fun shouldPrintRules(){
        rules.forEach { println(it) }
    }

    @Test
    fun shouldPrintUpdates(){
        updates.forEach { println(it) }
    }

    @Test
    fun firstUpdateShouldMatchRules(){
        updates[0].matchesRules(rules).shouldBe(true)
    }

    @Test
    fun fourthUpdateShouldNotMatchRules(){
        updates[3].matchesRules(rules).shouldBe(false)
    }

    @Test
    fun shouldSumMiddleNumberOfAllMatchingUpdates(){
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
        result.shouldBe(143)
    }
}

