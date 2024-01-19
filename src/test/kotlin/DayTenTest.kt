import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DayTenTest {
    private val testInput =
            "..F7.\n" +
            ".FJ|.\n" +
            "SJ.L7\n" +
            "|F--J\n" +
            "LJ..."
    private val obfuscatedTestInput =
            "7-F7-\n" +
            ".FJ|7\n" +
            "SJLL7\n" +
            "|F--J\n" +
            "LJ.LJ"

    private val field = testInput.parse()
    @Test
    fun `should parse as field`(){
        print(field.toString())
    }
    @Test
    fun `should find adjacent pipes`(){
        println(field.getAdjacentPipes(Point(2,1) to PipePart('J').toSubclass()))
    }

    @Test
    fun `should find starting point`(){
        field.getStartingPoint().second.shouldBeInstanceOf<SPipe>()
    }

    @Test
    fun `should find loop size`(){
        field.findLoopSize().shouldBe(16)
    }

    @Test
    fun `should find loop size on obfuscated input`(){
        val testField = obfuscatedTestInput.parse()
        testField.findLoopSize().shouldBe(16)
    }

    @Test
    fun `should find loop size on obfuscated input, new version`(){
        val testField = obfuscatedTestInput.parse()
        testField.findPath().shouldBe(16)
    }

    @Test
    fun `should find possible directions for S`(){
        val startingPoint = field.getStartingPoint()
        field.getPossibleDirectionForStartingPoint(startingPoint.first).shouldBe(listOf(Directions.SOUTH, Directions.EAST))
    }

    @Test
    fun `should affect possible directions to S`(){
        val startingPoint = field.getStartingPoint()
        field.pipes[startingPoint.first]!!.listOfConnections.shouldBe(listOf(Directions.SOUTH, Directions.EAST))
    }

}