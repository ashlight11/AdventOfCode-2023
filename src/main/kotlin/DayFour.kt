import java.io.File
import kotlin.math.pow

class DayFour(file: File) {
    val baseFile = file

    fun computePartOne() {
        val baseGames = baseFile.readLines().mapIndexed { index, line ->
            index to line.split(":")[1]
        }.toMap()
        //println(baseGames)
        val games = baseGames.mapValues { Game(it.value) }
        /*games.forEach {
            println(it.value.winningNumbers)
        }*/
        val pointsPerGame = games.mapValues { calculatePointsPerGame(it.value) }
        println(pointsPerGame)
        val sumOfPoints = pointsPerGame.map { it.value }.sumOf { it }
        println(sumOfPoints)
    }

    fun computePartTwo() {
        val baseGames = baseFile.readLines().mapIndexed { index, line ->
            index to line.split(":")[1]
        }.toMap()
        //println(baseGames)
        val games = baseGames.mapValues { Game(it.value) }
        val numberOfCards = countCards(games)
        println(numberOfCards)

    }

    private fun calculatePointsPerGame(game: Game): Double {
        val two: Double = 2.0
        val intersection = game.drawnNumbers.intersect(game.winningNumbers.toSet())

        return if (intersection.isEmpty()) {
            0.0
        } else {
            two.pow(intersection.size - 1)
        }
    }

    private fun additionalCardsPerGame(game: Game): Int {
        val two: Double = 2.0
        val intersection = game.drawnNumbers.intersect(game.winningNumbers.toSet())
        return intersection.size
    }

    private fun countCards(games: Map<Int, DayFour.Game>): Int {
        val cardsOwned: MutableList<Int> = MutableList(games.size) { 1 }
        for (game in games) {
            val cardsWon = additionalCardsPerGame(game.value)
            println("game " + (game.key) + " won $cardsWon cards ")

            val start = game.key + 1
            val end = game.key + cardsWon
            val numberOfCurrentCard = cardsOwned[game.key]
            for (i in start..end) {
                if (i < cardsOwned.size) {
                    //println(i)
                    cardsOwned[i] += numberOfCurrentCard
                }
            }
            //println("After game, cards : $cardsOwned")

        }
        return cardsOwned.sumOf { it }
    }


    data class Game(val string: String) {
        val winningNumbers = string.split("|")[0].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val drawnNumbers = string.split("|")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
    }
}