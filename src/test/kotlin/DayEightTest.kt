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

    private val inputForGhostsAsText =
        "LR\n" +
            "\n" +
            "11A = (11B, XXX)\r\n" +
            "11B = (XXX, 11Z)\r\n" +
            "11Z = (11B, XXX)\r\n" +
            "22A = (22B, XXX)\r\n" +
            "22B = (22C, 22C)\r\n" +
            "22C = (22Z, 22Z)\r\n" +
            "22Z = (22B, 22B)\r\n" +
            "XXX = (XXX, XXX)"

    private val inputForGhosts = Input(inputForGhostsAsText)

    @Test
    fun `should parse nodes as a map`() {
        input.nodesMap.forEach {
            println(it)
        }
    }

    @Test
    fun `should find number of iterations`(){
        input.readInstructions("AAA").shouldBeEqual(2)
    }

    @Test
    fun `should find number of iterations for second input`(){
        secondInput.readInstructions("AAA").shouldBeEqual(6)
    }

    @Test
    fun countNodesThatStartWithA(){
        println(input.nodesMap.count { it.key.startsWith('A') })
    }

    @Test
    fun `should count iterations for ghosts`(){
        inputForGhosts.readInstructionsForGhosts().shouldBeEqual(6)
    }

    @Test
    fun `should count iterations for ghosts, but better`(){
        inputForGhosts.readInstructionsForGhostsButBetter().shouldBeEqual(6)
    }

}