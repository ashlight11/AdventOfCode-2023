import java.io.File

class DayThree(file: File) {
    val baseFile = file

    fun computePartOne() {
        val lines = baseFile.readLines()
        val charArrayArray = lines.map {
            it.toCharArray()
        }
        //findNumbers(charArrayArray[0])
        val allNumbers = findNumbersInCharArrayArray(charArrayArray)
        val partNumbers = allNumbers.filter {
            numberIsPart(it, charArrayArray)
        }
        partNumbers.forEach {
            println("Valeur : " + it.value + " at  line " + it.rank + " position (" + it.lowestIndex + ", " + it.highestIndex + ")")
        }
        val result = partNumbers.sumOf {
            it.value
        }
        println(result)
        /*val test1 = NumberWithIndexes(862, 56, 59)
        val test2 = NumberWithIndexes(453, 86, 88)
        println(numberIsPart(0, test1, charArrayArray))
        println(numberIsPart(0, test2, charArrayArray))*/
    }

    fun computePartTwo() {
        val lines = baseFile.readLines()
        val charArrayArray = lines.map {
            it.toCharArray()
        }

        val allNumbers = findNumbersInCharArrayArray(charArrayArray)
        val partNumbersWithChars = allNumbers.mapNotNull {
            numberIsPartWith(it, charArrayArray)
        }

        val gears = findGears(partNumbersWithChars)
        gears.forEach {
            println("gear : " + it.key + "; values : " + it.value.size)
        }
        val result = sumOfGears(gears)
        println(result)

    }

    private fun numberIsPart(number: NumberWithIndexesAndRank, charArray: List<CharArray>): Boolean {
        val minIndex = number.lowestIndex
        val maxIndex = number.highestIndex
        val size = charArray.size
        val lineSize = charArray[0].size
        val rank = number.rank
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
                        if (isSpecialCharacter(charArray[j][i])) {
                            //println(charArray[j][i])
                            return true
                        }
                    }

                }
            }
        }
        return false
    }

    private fun numberIsPartWith(
        number: NumberWithIndexesAndRank,
        charArray: List<CharArray>
    ): NumberWithItsSpecialChar? {
        val minIndex = number.lowestIndex
        val maxIndex = number.highestIndex
        val size = charArray.size
        val lineSize = charArray[0].size
        val rank = number.rank
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
                        if (isSpecialCharacter(charArray[j][i])) {
                            //println(charArray[j][i])
                            val character =
                                CharacterWithIndexesAndRank(character = charArray[j][i], lineRank = j, index = i)
                            return NumberWithItsSpecialChar(number, character)
                        }
                    }

                }
            }
        }
        return null
    }

    private fun isSpecialCharacter(c: Char): Boolean {
        return !c.isLetterOrDigit() && c != '.'
    }

    private fun findNumbersInCharArrayArray(charCharArray: List<CharArray>): List<NumberWithIndexesAndRank> {
        val result = emptyList<NumberWithIndexesAndRank>().toMutableList()
        for ((index, charArray) in charCharArray.withIndex()) {
            result += findNumbers(charArray, rank = index)
        }
        return result
    }

    private fun findNumbers(charArray: CharArray, rank: Int): List<NumberWithIndexesAndRank> {
        var result: List<NumberWithIndexesAndRank> = emptyList<NumberWithIndexesAndRank>().toMutableList()
        var temp = String()
        //println("line $rank")
        for ((i, c) in charArray.withIndex()) {
            //println("column $i")
            if (c.isDigit()) {
                //println("character is digit $c, temp = $temp")
                if (temp.isEmpty()) {
                    temp = c.toString()
                } else {
                    temp += c.toString()
                }
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
                        lowestIndex = charArray.size - temp.length,
                        highestIndex = charArray.size - 1,
                        rank = rank
                    )
                )
            //println("result has been updated $result")
            temp = String()
        }
        //println(result)
        return result
    }

    private fun findGears(list: List<NumberWithItsSpecialChar>): Map<CharacterWithIndexesAndRank, List<NumberWithItsSpecialChar>> {
        return list.groupBy { it.character }.filterValues { it.size == 2 }
    }

    private fun sumOfGears(gears : Map<CharacterWithIndexesAndRank, List<NumberWithItsSpecialChar>>) : Int {
        val gearValues = gears.values.map { it[0].value.value * it[1].value.value }
        return gearValues.sumOf { it }
    }

    data class NumberWithIndexesAndRank(val value: Int, val lowestIndex: Int, val highestIndex: Int, val rank: Int)

    data class CharacterWithIndexesAndRank(val character: Char, val index: Int, val lineRank: Int)

    data class NumberWithItsSpecialChar(val value: NumberWithIndexesAndRank, val character: CharacterWithIndexesAndRank)

}