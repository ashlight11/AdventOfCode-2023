import java.io.File

class DayOne (file : File) {
    val baseFile = file

    fun compute() {
        var tab = baseFile.readLines().map { line -> replaceByDigits(line) }
        tab = tab.map { line -> getFirstAndLastChar(line.filter{ it.isDigit()}) }
        println(tab.sumOf { it.toInt() })
    }

    fun getFirstAndLastChar(input : String) : String {
        val first = input[0].toString()
        val last = input[input.length-1].toString()
        return first.plus(last)
    }

    fun replaceByDigits(input : String): String{
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


    enum class Digits(val text : String) {
        ONE("one"){
            val value = 1
        },
        TWO("two"){
            val value = 2
        },
        THREE("three"){
            val value = 3
        },
        FOUR("four"){
            val value = 4
        },
        FIVE("five"){
            val value = 5
        },
        SIX("six"){
            val value = 6
        },
        SEVEN("seven"){
            val value = 7
        },
        EIGHT("eight"){
            val value = 8
        },
        NINE("nine"){
            val value = 9
        }
    }

}