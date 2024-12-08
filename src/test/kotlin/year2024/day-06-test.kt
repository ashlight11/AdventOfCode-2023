package year2024

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day06Test{
    val testInput = "....#.....\n" +
            "....^....#\n" +
            "..........\n" +
            "..#.......\n" +
            ".......#..\n" +
            "..........\n" +
            ".#........\n" +
            "........#.\n" +
            "#.........\n" +
            "......#..."

    val map = testInput.parseAsMap()
    @Test
    fun shouldParseAsMap(){
        map.obstacles.forEach {
            println(it)
        }
        println(map.guard)
    }

    @Test
    fun shouldCountSteps(){
        while(map.findNextObstacle() != null){
            map.findNextObstacle()
        }
    }
}