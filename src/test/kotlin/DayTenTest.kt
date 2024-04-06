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
    private val enclosedTilesTest =
                    "...........\n" +
                    ".S-------7.\n" +
                    ".|F-----7|.\n" +
                    ".||.....||.\n" +
                    ".||.....||.\n" +
                    ".|L-7.F-J|.\n" +
                    ".|..|.|..|.\n" +
                    ".L--J.L--J.\n" +
                    "..........."

    private val biggerInput =
                    "FF7FSF7F7F7F7F7F---7\n" +
                    "L|LJ||||||||||||F--J\n" +
                    "FL-7LJLJ||||||LJL-77\n" +
                    "F--JF--7||LJLJIF7FJ-\n" +
                    "L---JF-JLJIIIIFJLJJ7\n" +
                    "|F|F-JF---7IIIL7L|7|\n" +
                    "|FFJF7L7F-JF7IIL---7\n" +
                    "7-L-JL7||F7|L7F-7F7|\n" +
                    "L.L7LFJ|||||FJL7||LJ\n" +
                    "L7JLJL-JLJLJL--JLJ.L"

    private val otherTest =
            "OF----7F7F7F7F-7OOOO\n" +
            "O|F--7||||||||FJOOOO\n" +
            "O||OFJ||||||||L7OOOO\n" +
            "FJL7L7LJLJ||LJIL-7OO\n" +
            "L--JOL7IIILJS7F-7L7O\n" +
            "OOOOF-JIIF7FJ|L7L7L7\n" +
            "OOOOL7IF7||L7|IL7L7|\n" +
            "OOOOO|FJLJ|FJ|F7|OLJ\n" +
            "OOOOFJL-7O||O||||OOO\n" +
            "OOOOL---JOLJOLJLJOOO"

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
        testField.findPath().first.shouldBe(16)
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

    @Test
    fun `should find number of enclosed tiles`(){
        val partTwoField = enclosedTilesTest.parse()
        val listOfPoints = partTwoField.findPath().second
        println("all pipes : "+ partTwoField.pipes.size + ", points on loop : " + listOfPoints.size)
        partTwoField.findEnclosedPoints(listOfPoints).shouldBe(4)
    }

    @Test
    fun `should find number of enclosed tiles for larger input`(){
        val partTwoField = biggerInput.parse()
        val listOfPoints = partTwoField.findPath().second
        //println("all pipes : "+ partTwoField.pipes.size + ", points on loop : " + listOfPoints.size)
        partTwoField.findEnclosedPoints(listOfPoints).shouldBe(10)
    }

    @Test
    fun `should find number of enclosed tiles for other input`(){
        val partTwoField = otherTest.parse()
        val listOfPoints = partTwoField.findPath().second
        //println("all pipes : "+ partTwoField.pipes.size + ", points on loop : " + listOfPoints.size)
        partTwoField.findEnclosedPoints(listOfPoints).shouldBe(8)
    }

    @Test
    fun `should test windowed`(){
        val test = listOf(Point(3,0), Point(4,0), Point(5,0))
        println(test.windowed(2,1))
    }

    @Test
    fun `should test toStrings`(){
        val test = listOf(VerticalPipe('|'), HorizontalPipe('-'), JPipe('J'))
        test.toStrings().shouldBe("|, -, J")
    }


}