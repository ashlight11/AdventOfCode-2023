import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test


class DayEightTest {
    private val inputAsText = "" +
            "RL\n" +
            "\n" +
            "AAA = (BBB, CCC)\r\n" +
            "BBB = (DDD, EEE)\r\n" +
            "CCC = (ZZZ, GGG)\r\n" +
            "DDD = (DDD, DDD)\r\n" +
            "EEE = (EEE, EEE)\r\n" +
            "GGG = (GGG, GGG)\r\n" +
            "ZZZ = (ZZZ, ZZZ)"

    val input = Input(inputAsText)

    private val secondInputAsText =
        "LLR\n" +
                "\n" +
                "AAA = (BBB, BBB)\r\n" +
                "BBB = (AAA, ZZZ)\r\n" +
                "ZZZ = (ZZZ, ZZZ)"

    val secondInput = Input(secondInputAsText)

    @Test
    fun `should parse nodes as a map`() {
        val nodesMap = input.nodesAsText.parseAsNodesMap()
        nodesMap.forEach {
            println(it)
        }
    }

    @Test
    fun `should find number of iterations`(){
        val nodesMap = input.nodesAsText.parseAsNodesMap()
        input.readInstructions(nodesMap).shouldBeEqual(2)
    }

    @Test
    fun `should find number of iterations for second input`(){
        val nodesMap = secondInput.nodesAsText.parseAsNodesMap()
        secondInput.readInstructions(nodesMap).shouldBeEqual(6)
    }

}