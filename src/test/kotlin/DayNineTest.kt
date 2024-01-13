import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DayNineTest {
    val testInput = "10  13  16  21  30  45"
    val testInput2 = "1   3   6  10  15  21"
    val testInput3 = "10  13  16  21  30  45"
    val testInput4 = "0   2   4   6"
    val list = testInput2.split("\\s+".toRegex())
    val listPartTwo = testInput3.split("\\s+".toRegex())
    val listPartTwoTest = testInput4.split("\\s+".toRegex())

    @Test
    fun `should find output value`(){
        val longList = list.map { it.toLong() }
        longList.findNextValue().shouldBe(28L)
    }

    @Test
    fun `should find subtraction list`(){
        val longList = list.map { it.toLong() }
        val subtractionList = longList.getSubtractionList()
        subtractionList.shouldBeEqual(listOf(2L,3L,4L,5L,6L))
    }

    @Test
    fun `should find output for part two`(){
        val longList = listPartTwo.map { it.toLong() }.reversed()
        longList.findNextValue().shouldBeEqual(5L)
    }

    @Test
    fun `should find output for part two negative answer`(){
        val longList = listPartTwoTest.map { it.toLong() }.reversed()
        longList.findNextValue().shouldBeEqual(-2L)
    }
}