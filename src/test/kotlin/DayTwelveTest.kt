import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DayTwelveTest {
    val corruptedInput =
            "???.### 1,1,3\n" +
            ".??..??...?##. 1,1,3\n" +
            "?#?#?#?#?#?#?#? 1,3,1,6\n" +
            "????.#...#... 4,1,1\n" +
            "????.######..#####. 1,6,5\n" +
            "?###???????? 3,2,1"

    @Test
    fun shouldMapRows() {
        val expectedMap = mapOf(
            "???.###" to listOf(1, 1, 3),
            ".??..??...?##." to listOf(1, 1, 3),
            "?#?#?#?#?#?#?#?" to listOf(1, 3, 1, 6),
            "????.#...#..." to listOf(4, 1, 1),
            "????.######..#####." to listOf(1, 6, 5),
            "?###????????" to listOf(3, 2, 1)
        )
        corruptedInput.lines().parseToMap().shouldBe(expectedMap)
    }

    @Test
    fun shouldFindNumberOfPermutationsInNonRestrictedGroupWithFixedElements() {
        val testGroup1 = SpringGroup("?##".toSprings())
        val testGroup2 = SpringGroup("?###".toSprings())
        testGroup1.permutationsNonRestricted(1).shouldBe(1)
        testGroup2.permutationsNonRestricted(3).shouldBe(1)
    }

    @Test
    fun shouldFindNumberOfPermutationsInNonRestrictedGroup() {
        val testGroup1 = SpringGroup("????".toSprings())
        val testGroup2 = SpringGroup("??".toSprings())
        testGroup1.permutationsNonRestricted(1).shouldBe(4)
        testGroup2.permutationsNonRestricted(1).shouldBe(2)
    }

    @Test
    fun shouldFindNumberOfPermutationsInGroupToRestrict() {
        val testGroup1 = SpringGroup("???????".toSprings())
        // have to place 2 THEN 1
        // first is 2 among n (=7) - [1 (number left to place) + 1 (reserved for '.', because not last to place)]
        // second is 1 among n - [2 - 1 if not last to place, else 0]
        val first = testGroup1.permutationsWithRestriction(1, 3)
        val second = testGroup1.permutationsWithRestriction(1, 3)
        println("$first, $second")

    }

    @Test
    fun shouldCountGroupsOfN() {
        "????.######..#####.".findGroupsOfSize(5).shouldBe(1)
    }

    @Test
    fun shouldGetNumberOfPermutationsPossible() {
        val line = "?###????????" // 3, 2 , 1
        line.getTotalPermutationsForGroup(2, listOf(1)).shouldBe(4)
    }

    @Test
    fun shouldCountArrangements() {
        countArrangements(
            ".?###????????.".toCharArray(),
            booleanArrayOf(false, true, true, true, false, true, true, false, true, false)
        ).shouldBe(10)
    }

    @Test
    fun intListToBooleanArray() {
        listOf(3, 2, 1).toBooleanArray()
            .shouldBe(booleanArrayOf(false, true, true, true, false, true, true, false, true, false))
    }

    @Test
    fun shouldFindTotal() {
        corruptedInput.lines().parseToCharArrayAndBooleanArray(1, null, null)
            .map { countArrangements(it.key, it.value) }
            .sum().shouldBe(21)
    }

    @Test
    fun shouldReplicateStringNTimes(){
        "test".repeatNTimesAndAddInBetween(3, "?.").shouldBe("test?.test?.test")
    }

    @Test
    fun shouldFindTotalFor5TimesLonger(){
        corruptedInput.lines().parseToCharArrayAndBooleanArray(5, "?", ",")
            .map { countArrangements(it.key, it.value) }
            .sum().shouldBe(525152)
    }

}