import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DayElevenTest {

    private val testInput =
        "...#......\n" +
                ".......#..\n" +
                "#.........\n" +
                "..........\n" +
                "......#...\n" +
                ".#........\n" +
                ".........#\n" +
                "..........\n" +
                ".......#..\n" +
                "#...#....."

    private val galaxy1 = Galaxy(LongPoint(3, 0))
    private val adjusted1 = LongPoint(4, 0)
    private val galaxy2 = Galaxy(LongPoint(7, 1))
    private val adjusted2 = LongPoint(9, 1)
    private val galaxy3 = Galaxy(LongPoint(0, 2))
    private val adjusted3 = LongPoint(0, 2)
    private val galaxy4 = Galaxy(LongPoint(6, 4))
    private val adjusted4 = LongPoint(8, 5)
    private val galaxy5 = Galaxy(LongPoint(1, 5))
    private val adjusted5 = LongPoint(1, 6)
    private val galaxy6 = Galaxy(LongPoint(9, 6))
    private val adjusted6 = LongPoint(12, 7)
    private val galaxy7 = Galaxy(LongPoint(7, 8))
    private val adjusted7 = LongPoint(9, 10)
    private val galaxy8 = Galaxy(LongPoint(0, 9))
    private val adjusted8 = LongPoint(0, 11)
    private val galaxy9 = Galaxy(LongPoint(4, 9))
    private val adjusted9 = LongPoint(5, 11)

    @Test
    fun shouldFindGalaxiesPosition() {
        testInput.findGalaxies()
            .shouldBe(listOf(galaxy1, galaxy2, galaxy3, galaxy4, galaxy5, galaxy6, galaxy7, galaxy8, galaxy9))
    }

    private val galaxies = testInput.findGalaxies()
    private val sky = Sky(galaxies, 1)

    @Test
    fun shouldFindBlankLines() {
        sky.lines.shouldBe(listOf(3, 7))
    }

    @Test
    fun shouldFindBlankColumns() {
        sky.columns.shouldBe(listOf(2, 5, 8))
    }


    @Test
    fun shouldFindCorrectedPositionsOfGalaxies() {
        sky.adjustedGalaxies.shouldBe(
            listOf(adjusted1, adjusted2, adjusted3, adjusted4, adjusted5, adjusted6, adjusted7, adjusted8, adjusted9)
        )
    }

    @Test
    fun shouldComputeShortestPathBetweenTwoLongPoints() {
        adjusted5.computeShortestPathTo(adjusted9).shouldBe(9)
    }

    @Test
    fun shouldFindTotalPath() {
        sky.computeSumOfAllPaths().div(2).shouldBe(374)
    }

    @Test
    fun shouldFindTotalPathTenTimes() {
        Sky(galaxies, 9).computeSumOfAllPaths().shouldBe(1030)
    }
}

