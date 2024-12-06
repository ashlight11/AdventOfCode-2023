package year2024

import Point
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day04Test {
    val testInput =
        "MMMSXXMASM\n" +
                "MSAMXMSMSA\n" +
                "AMXSXMAAMM\n" +
                "MSAMASMSMX\n" +
                "XMASAMXAMM\n" +
                "XXAMMXXAMA\n" +
                "SMSMSASXSS\n" +
                "SAXAMASAAA\n" +
                "MAMMMXMMMM\n" +
                "MXMXAXMASX"

    val masTestInput =
        ".M.S......\n" +
            "..A..MSMS.\n" +
            ".M.S.MAA..\n" +
            "..A.ASMSM.\n" +
            ".M.S.M....\n" +
            "..........\n" +
            "S.S.S.S.S.\n" +
            ".A.A.A.A..\n" +
            "M.M.M.M.M.\n" +
            ".........."

    val letterGrid = testInput.parseAsLetterGrid()
    val xLetter = Letter('X', Point(5, 9))
    val mLetter = Letter('M', Point(6, 9))
    val mLetter2 = Letter('M', Point(4, 8))
    val mLetter3 = Letter('M', Point(6, 8))

    @Test
    fun shouldParseAsLetterGrid() {
        letterGrid.letters.forEach { println(it) }

    }

    /*@Test
    fun shouldFindConnectionsInGrid(){
        xLetter.getConnectionsInGrid(letterGrid).shouldBe(listOf(mLetter2, mLetter3, mLetter))
    }*/

    @Test
    fun shouldFind3Xmas() {
        xLetter.countXmas(letterGrid, XmasWord.XMAS).shouldBe(3)
    }

    @Test
    fun countAllXmas(){
        val listOfXs = letterGrid.getAll('X')
        listOfXs.values.sumOf { it.countXmas(letterGrid, XmasWord.XMAS) }.shouldBe(18)
    }

    @Test
    fun countAllMas(){
        val masLetterGrid = masTestInput.parseAsLetterGrid()
        val listOfAs = masLetterGrid.getAll('A')
        listOfAs.values.sumOf { it.countXmas(masLetterGrid, XmasWord.MAS) }.shouldBe(9)
    }
}

