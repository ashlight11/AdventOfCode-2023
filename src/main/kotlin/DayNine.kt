import java.io.File

class DayNine(file: File) {
    private val baseLines = file.readLines()
        .map { it.split("\\s+".toRegex()) }
        .map { it.map { string -> string.toLong() } }

    private val  baseLinesReversed = baseLines.map { line -> line.reversed() }


    fun computePartOne(){
        val allEnds = baseLines.map { it.findNextValue() }
        println("SUM : ".plus(allEnds.sum()))
    }

    fun computePartTwo(){
        val allEnds = baseLinesReversed.map { it.findNextValue() }
        println("SUM : ".plus(allEnds.sum()))
    }
}
fun List<Long>.findNextValue(): Long {
    val listOfLastLongs = emptyList<Long>().toMutableList()
    var currentList = this
    listOfLastLongs.add(currentList.last())
    while (currentList.isNotAllZeroes()) {
        currentList = currentList.getSubtractionList()
        listOfLastLongs.add(currentList.last())
    }
    return listOfLastLongs.sum()
}

private fun List<Long>.isNotAllZeroes(): Boolean {
    return this.any { it != 0L }
}
fun List<Long>.getSubtractionList(): List<Long> {
    val listSize = this.size
    val result = List<Long>(listSize.minus(1)) { 1 }.toMutableList()
    for (i in 1..<listSize) {
        val index = i.minus(1)
        result[index] = this[i].minus(this[index])
    }
    return result
}
