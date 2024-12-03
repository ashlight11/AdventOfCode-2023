package year2024

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day03Test{
    val testInput = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
    val listOfMuls = listOf(
        Mul(2,4, (1..8)),
        Mul(5,5, (29..36)),
        Mul(11,8, (53..61)),
        Mul(8,5, (62..69)))

    val doAndDontTestInput = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

    @Test
    fun shouldListMuls(){
        testInput.toMulList().shouldBe(listOfMuls)
    }

    @Test
    fun shouldComputeResult(){
        testInput.toMulList().sumOf { it.result }.shouldBe(161)
    }

    @Test
    fun shouldComputeResultWithDoAndDont(){
        val dontList = doAndDontTestInput.toDontList()
        val mulList = doAndDontTestInput.toMulList()
        val result = computeResultWithMulAndDont(mulList, dontList)
        result.shouldBe(48)
    }
}