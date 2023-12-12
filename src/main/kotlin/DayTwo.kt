import java.io.File

class DayTwo (file : File) {
    val baseFile = file

    // 12 red cubes, 13 green cubes, and 14 blue cubes -> total 39 cubes
    fun computePartOne(){
        val tab = baseFile.readLines().mapIndexed{index, line ->
            line.substring(
                ("Card ".plus((index+1).toString()).plus(": ")).length)
        }
        var gamesMap : Map<Int, String> = tab.mapIndexed{index, element -> (index+1) to element}.toMap()
        gamesMap = gamesMap.filter { numbersAddUpTo(14, 13, 12, it.value)}
        val result = gamesMap.keys.sum()

        println(result)
    }

    fun computePartTwo(){
        var tab = baseFile.readLines().mapIndexed{index, line ->
            line.substring(
                ("Game ".plus((index+1).toString()).plus(": ")).length)
        }
        tab = tab.map { calculatePower(it).toString() }
        val result = tab.sumOf { it.toInt() }

        println(result)
    }

    private fun calculatePower(input : String) : Int {
        val test = minimumBlue(input) * minimumRed(input) * minimumGreen(input)
        return test
    }

    fun minimumBlue(input: String): Int {
        val blueRegex = "[0-9]{1,2} blue".toRegex()
        val blueCubes = blueRegex.findAll(input).toList()
        val test = blueCubes.maxOf { expression -> expression.value.filter { it.isDigit() }.toInt() }
        return test
    }

    private fun notTooManyBlueCubes(input : String, limit : Int): Boolean{
        val blueRegex = "[0-9]{1,2} blue".toRegex()
        val blueCubes = blueRegex.findAll(input).toList()
        val listOfCounts = matcherToIntList(blueCubes)
        for (count in listOfCounts){
            if (count > limit){
                return false
            }
        }
        return true
    }

    fun minimumRed(input : String):Int{
        val redRegex = "[0-9]{1,2} red".toRegex()
        val redCubes = redRegex.findAll(input).toList()
        return redCubes.maxOf { expression -> expression.value.filter { it.isDigit() }.toInt() }
    }

    private fun notTooManyRedCubes(input : String, limit : Int): Boolean{
        val redRegex = "[0-9]{1,2} red".toRegex()
        val redCubes = redRegex.findAll(input).toList()
        val listOfCounts = matcherToIntList(redCubes)
        for (count in listOfCounts){
            if (count > limit){
                return false
            }
        }
        return true
    }

    private fun matcherToIntList(matchResultList: List<MatchResult>) : List<Int>{
        return matchResultList.map { line -> line.value.filter { it.isDigit() }.toInt() }
    }

    fun minimumGreen(input : String):Int{
        val greenRegex = "[0-9]{1,2} green".toRegex()
        val greenCubes = greenRegex.findAll(input).toList()

        return greenCubes.maxOf { expression -> expression.value.filter { it.isDigit() }.toInt() }
    }

    private fun notTooManyGreenCubes(input : String, limit : Int): Boolean{
        val greenRegex = "[0-9]{1,2} green".toRegex()
        val greenCubes = greenRegex.findAll(input).toList()
        val listOfCounts = matcherToIntList(greenCubes)
        for (count in listOfCounts){
            if (count > limit){
                return false
            }
        }
        return true
    }

    fun numbersAddUpTo(blueCount : Int, greenCount : Int, redCount : Int, text : String):Boolean {
        return notTooManyBlueCubes(text, blueCount)
                && notTooManyGreenCubes(text, greenCount) &&
                notTooManyRedCubes(text,redCount)
    }
}