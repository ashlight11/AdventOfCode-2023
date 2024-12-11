package year2024

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day06Test{
    val testInput =
            "....#.....\n" +
            ".........#\n" +
            "..........\n" +
            "..#.......\n" +
            ".......#..\n" +
            "..........\n" +
            ".#..^.....\n" +
            "........#.\n" +
            "#.........\n" +
            "......#..."


    /*
    @Test
    fun shouldParseAsMap(){
        map.obstacles.forEach {
            println(it)
        }
        println(map.guard)
    }

    @Test
    fun shouldReplaceWithXs() {
        while(map.findNextObstacle() != null){
            map.findNextObstacle()
        }
        val points = map.visitedPoints
        val grid = Array(10) { CharArray(10) { '.' } }
        //points.forEach { println(it) }

        points.forEach { point ->
            if (point.x in 0..9 && point.y in 0..9) {
                grid[point.y][point.x] = 'X'
            }
        }
        grid.forEach { row ->
            println(row.joinToString(""))

        }
    }

    @Test
    fun shouldCountSteps(){
        while(map.findNextObstacle() != null){
            map.findNextObstacle()
        }
        (map.visitedPoints.size).shouldBe(41)
    }

    */

    @Test
    fun shouldCountUniquePoints(){
        val (grid, guard) = parseGrid(input = testInput)
        val uniquePointsCount = moveGuard(grid, guard)
        uniquePointsCount.shouldBe(41)
    }
}