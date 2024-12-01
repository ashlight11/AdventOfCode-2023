package year2023

import java.io.File
import kotlin.math.pow

class DayFour(file: File) {
    val baseFile = file

    fun computePartOne() {
        val baseGames = baseFile.readLines()
            .mapIndexed { index, line ->
                index to line.split(":")[1]
            }.toMap()
        //baseGames is a Map<Int,String>. the key is the number of the game starting at 0.
        //The value is a string containing the winning numbers and the drawn numbers

        val games = baseGames.mapValues { Game(it.value) } // Transform the value to a "Game" object

        val pointsPerGame = games.mapValues { it.value.calculatePoints() }
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

    /**
     * Each game awards one point per matching card between the winning cards and the draw one.
     * And each match after the first doubles the point value of that card.
     * Meaning 1 * 2 * 2 * 2... => 1 * 2.power(number of matches minus the first one)
     */
    private fun Game.calculatePoints(): Double {
        // The numbers in common is the intersection of the two lists
        val intersection = this.drawnNumbers.intersect(this.winningNumbers.toSet())

        return if (intersection.isEmpty()) {
            0.0 // No point is awarded if there is no match
        } else {
            2.0.pow(intersection.size - 1)  // 1 * 2.power(number of matches minus the first one)
        }
    }

    /**
     * Each game awards one card for each match between winning and drawn numbers
     */
    private fun Game.additionalCards(): Int {
        val intersection = this.drawnNumbers.intersect(this.winningNumbers.toSet())
        return intersection.size
    }


    /**
     * For a set of games, calculates how many cards the player is left with, included those won.
     */
    private fun countCards(games: Map<Int, Game>): Int {
        // The player starts with one card for each game
        val cardsOwned: MutableList<Int> = MutableList(games.size) { 1 }
        for (game in games) { // for each card
            val cardsWon = game.value.additionalCards() // the player wins some more

            println("game " + (game.key) + " won $cardsWon cards ")

            val start = game.key + 1 // cards won will add up to the cards for the next games
            val end = game.key + cardsWon // e.g. if they win 3 cards, they win cards for the 3 next games
            val numberOfCurrentCard =
                cardsOwned[game.key] // they may already have more than one card for the current game
            for (i in start..end) {
                // ensures we don't add cards to non-existing games
                cardsOwned[i.coerceAtMost(cardsOwned.size)] += numberOfCurrentCard // e.g. if they owned 2 cards for the current game, they gain 2 for each game eligible
            }
            //println("After game, cards : $cardsOwned")
        }
        return cardsOwned.sumOf { it }
    }


    /**
     * The winning numbers are before the pipe, the drawn ones, after
     */
    data class Game(val string: String) {
        val winningNumbers = string.split("|")[0].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val drawnNumbers = string.split("|")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
    }
}