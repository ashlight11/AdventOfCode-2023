import java.io.File

class DayThree(file: File) {
    val baseFile = file

    fun computePartOne() {
        val lines = baseFile.readLines()
        val charArrayArray = lines.map {
            it.toCharArray()
        } // I chose to see the input as a matrix of characters

        val allNumbers = charArrayArray.findNumbers() // find all the numbers and their positions in the matrix
        val partNumbers = allNumbers.filter {
            it.numberIsPart(charArrayArray)
        }// extract only partNumbers

        partNumbers.forEach {
            println("Valeur : " + it.value + " at  line " + it.rank + " position (" + it.lowestIndex + ", " + it.highestIndex + ")")
        }
        val result = partNumbers.sumOf {
            it.value
        }
        println(result)
    }

    fun computePartTwo() {
        val lines = baseFile.readLines()
        val charArrayArray = lines.map {
            it.toCharArray()
        } // I chose to see the input as a matrix of characters

        val allNumbers = charArrayArray.findNumbers() // find all the numbers and their positions in the matrix
        val partNumbersWithChars = allNumbers.mapNotNull {
            it.numberIsPartWith(charArrayArray)
        } // extract only partNumbers

        val gears = findGears(partNumbersWithChars)
        gears.forEach {
            println("gear : " + it.key + "; values : " + it.value.size)
        }
        val result = sumOfGears(gears)
        println(result)

    }

    /**
     * A number is part if it is surrounded by a special character
     * My approach is to go over all the characters in the neighbourhood, hence the CharArray
     */
    private fun NumberWithIndexesAndRank.numberIsPart(charArray: List<CharArray>): Boolean {
        val minIndex = this.lowestIndex
        val maxIndex = this.highestIndex
        val size = charArray.size
        val lineSize = charArray[0].size
        val rank = this.rank
        /*
        * j-1 : de imin - 1 à imax +1
        * j : imin -1 imin + 1
        * j + 1 : de imin - 1 à imax +1
        */
        for (j in rank - 1..rank + 1) {
            if (j in 0..<size) {
                for (i in minIndex - 1..maxIndex + 1) {
                    if (i in 0..<lineSize) {
                        //println("ligne $j, colonne $i, valeur " + charArray[j][i])
                        if (charArray[j][i].isSpecialCharacter()) {
                            //println(charArray[j][i])
                            return true
                        }
                    }

                }
            }
        }
        return false
    }

    /**
     * A number is part if it is surrounded by a special character
     * My approach is to go over all the characters in the neighbourhood, hence the CharArray
     * This function also groups a part number with which character makes it be a "part number"
     */
    private fun NumberWithIndexesAndRank.numberIsPartWith(
        charArray: List<CharArray>
    ): NumberWithItsSpecialChar? {
        val minIndex = this.lowestIndex
        val maxIndex = this.highestIndex
        val size = charArray.size
        val lineSize = charArray[0].size
        val rank = this.rank
        /*
        * j-1 : de imin - 1 à imax +1
        * j : imin -1 imin + 1
        * j + 1 : de imin - 1 à imax +1
        */
        for (j in rank - 1..rank + 1) {
            if (j in 0..<size) {
                for (i in minIndex - 1..maxIndex + 1) {
                    if (i in 0..<lineSize) {
                        //println("ligne $j, colonne $i, valeur " + charArray[j][i])
                        if (charArray[j][i].isSpecialCharacter()) {
                            //println(charArray[j][i])
                            val character =
                                CharacterWithIndexesAndRank(character = charArray[j][i], lineRank = j, index = i)
                            return NumberWithItsSpecialChar(this, character)
                        }
                    }

                }
            }
        }
        return null
    }

    private fun Char.isSpecialCharacter(): Boolean {
        return !this.isLetterOrDigit() && this != '.'
    }

    /**
     * Goes over each line in the global matrix to find numbers in it
     * @return all the numbers in the string as well as their absolute positions in the global matrix
     */
    private fun List<CharArray>.findNumbers(): List<NumberWithIndexesAndRank> {
        val result = emptyList<NumberWithIndexesAndRank>().toMutableList()
        for ((index, charArray) in this.withIndex()) {
            result += charArray.findNumbers(rank = index)
        }
        return result
    }

    /**
     * For a given string described as a CharArray, finds the numbers in the string
     * as well as their absolute positions in the global matrix.
     * @param rank is the number of the line in the matrix
     */
    private fun CharArray.findNumbers(rank: Int): List<NumberWithIndexesAndRank> {
        var result: List<NumberWithIndexesAndRank> = emptyList<NumberWithIndexesAndRank>().toMutableList()
        var temp = String()
        //println("line $rank")
        for ((i, c) in this.withIndex()) {
            //println("column $i")
            if (c.isDigit()) {
                //println("character is digit $c, temp = $temp")
                temp += c.toString()
                //println("now, temp is $temp")
            } else {
                //println("character is not digit $c, temp = $temp")
                if (temp.isNotEmpty()) {
                    //println("now, temp is $temp and i is $i")
                    result =
                        result.plus(
                            NumberWithIndexesAndRank(
                                value = temp.toInt(),
                                lowestIndex = i - temp.length,
                                highestIndex = i - 1,
                                rank = rank
                            )
                        )
                    //println("result has been updated $result")
                    temp = String()
                }
            }
        }
        if (temp.isNotEmpty()) {
            //println("ready to end it here, temp is $temp")
            result =
                result.plus(
                    NumberWithIndexesAndRank(
                        value = temp.toInt(),
                        lowestIndex = this.size - temp.length,
                        highestIndex = this.size - 1,
                        rank = rank
                    )
                )
        }
        return result
    }

    /**
     * Gears are special characters with exactly two part numbers
     */
    private fun findGears(list: List<NumberWithItsSpecialChar>): Map<CharacterWithIndexesAndRank, List<NumberWithItsSpecialChar>> {
        return list.groupBy { it.character }.filterValues { it.size == 2 }
    }

    private fun sumOfGears(gears: Map<CharacterWithIndexesAndRank, List<NumberWithItsSpecialChar>>): Int {
        val gearValues = gears.values.map { it[0].value.value * it[1].value.value }
        return gearValues.sumOf { it }
    }

    data class NumberWithIndexesAndRank(val value: Int, val lowestIndex: Int, val highestIndex: Int, val rank: Int)

    data class CharacterWithIndexesAndRank(val character: Char, val index: Int, val lineRank: Int)

    data class NumberWithItsSpecialChar(val value: NumberWithIndexesAndRank, val character: CharacterWithIndexesAndRank)

}