package year2024

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day01Test {
    private val testInput =
        "64256   78813\n" +
                "46941   56838\n" +
                "47111   50531\n" +
                "48819   41511\n" +
                "54871   96958\n" +
                "97276   63446"

    private val testList = listOf(64256, 78813, 46941, 56838, 47111, 50531, 48819, 41511, 54871, 96958, 97276, 63446)

    private val leftList = listOf(46941, 47111, 48819, 54871, 64256, 97276)
    private val rightList = listOf(41511, 50531, 56838, 63446, 78813, 96958)

    @Test
    fun shouldParseAsList() {
        testInput.parseAsIntList().shouldBe(testList)
    }

    @Test
    fun shouldSelectOnlyEvenIndexNumbers() {
        testList.filterIndexed { index, _ -> index % 2 == 0 }
            .sorted()
            .shouldBe(leftList)
    }

    @Test
    fun shouldSelectOnlyOddIndexNumbers() {
        testList.filterIndexed { index, _ -> index % 2 == 1 }
            .sorted()
            .shouldBe(rightList)
    }


    @Test
    fun shouldFindSumOfDifferences() {
        rightList.foldIndexed(initial = 0,
            operation = ({ index, acc, current ->
                acc + abs(current - leftList[index])
            })
        )
            .shouldBe(40319)
    }

    @Test
    fun shouldFindSimilarityScore() {
        calculateSimilarityScore(leftList, rightList).shouldBe(0)
    }
}
