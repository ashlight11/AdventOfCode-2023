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
    fun shouldMapRows(){

    }

    @Test
    fun shouldFindNumberOfPermutationsInNonRestrictedGroupWithFixedElements(){
        val testGroup1 = SpringGroup("?##".toSprings())
        val testGroup2 = SpringGroup("?###".toSprings())
        testGroup1.permutationsNonRestricted(1).shouldBe(1)
        testGroup2.permutationsNonRestricted(3).shouldBe(1)
    }

    @Test
    fun shouldFindNumberOfPermutationsInNonRestrictedGroup(){
        val testGroup1 = SpringGroup("????".toSprings())
        val testGroup2 = SpringGroup("??".toSprings())
        testGroup1.permutationsNonRestricted(1).shouldBe(4)
        testGroup2.permutationsNonRestricted(1).shouldBe(2)
    }

    @Test
    fun shouldFindNumberOfPermutationsInGroupToRestrict(){
        val testGroup1 = SpringGroup("???????".toSprings())
        // have to place 2 THEN 1
        // first is 2 among n (=7) - [1 (number left to place) + 1 (reserved for '.', because not last to place)]
        // second is 1 among n - [2 - 1 if not last to place, else 0]
        val first = testGroup1.permutationsWithRestriction(1, 3)
        val second = testGroup1.permutationsWithRestriction(1, 3)
        println("$first, $second")

    }

    @Test
    fun shouldFindNbOfArrangementsForSingleLine(){
        val line = "????.######..#####." // 1,6,5

    }

}