package year2023

import java.io.File

class DayOne (file : File) {
    val baseFile = file

    fun compute() {
        var tab = baseFile.readLines().map { line -> replaceByDigits(line) }
        // convert each line to a list of numbers
        // method getFirstAndLastChar selects only first and last digit in the string
        tab = tab.map { line -> getFirstAndLastChar(line.filter{ it.isDigit()}) }
        println(tab.sumOf { it.toInt() })
    }

    /**
     * Goes over characters in a list a digit to find the first and the last one
     */
    private fun getFirstAndLastChar(input : String) : String {
        val first = input[0].toString()
        val last = input[input.length-1].toString()
        return first.plus(last)
    }

    /**
     * Replace written digits with equivalents containing the numeric version
     * Attention to keep first and last characters as "twone" must be read as 2 and 1
     */
    private fun replaceByDigits(input : String): String{
        val regexOne = """one""".toRegex()
        val regexTwo = """two""".toRegex()
        val regexThree = """three""".toRegex()
        val regexFour = """four""".toRegex()
        val regexFive = """five""".toRegex()
        val regexSix = """six""".toRegex()
        val regexSeven = """seven""".toRegex()
        val regexEight = """eight""".toRegex()
        val regexNine = """nine""".toRegex()
        val listOfRegex = listOf(regexOne, regexTwo, regexThree, regexFour, regexFive, regexSix, regexSeven, regexEight, regexNine)
        val listOfReplacements = listOf("o1e", "t2o", "thr3e","fo4r","fi5e", "s6x", "se7en", "ei8th", "n9ne")
        var output = input
        listOfRegex.forEachIndexed { index, regex ->
            output = regex.replace(output, listOfReplacements[index])
        }

        return output
    }
}