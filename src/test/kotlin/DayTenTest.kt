import org.junit.jupiter.api.Test

class DayTenTest {
    private val testInput =
            "..F7.\n" +
            ".FJ|.\n" +
            "SJ.L7\n" +
            "|F--J\n" +
            "LJ..."

    private val field = testInput.parse()
    @Test
    fun `should parse as field`(){
        print(field.toString())
    }
    @Test
    fun `should find adjacent pipes`(){
        println(field.getAdjacentPipes(Point(2,1) to PipePart('J').toSubclass()))
    }
}