import java.io.File
import kotlin.math.*


class DaySix (file : File) {
    private val records = file.readText().generateRecords()

    fun computePartOne(){
        val waysToWin = records.map { it.countWaysToWin() }
        val multiplication = waysToWin.reduce { acc, num -> acc * num }
        for(value in waysToWin){
            println(value)
        }
        println(multiplication)

    }
    fun computePartTwo(){

    }

    private fun Record.countWaysToWin() : Long {
        val duration = this.duration
        val sqrtDeterminant = sqrt(duration.pow(2) - 4 * this.distance)
        val firstRoot = (duration - sqrtDeterminant) / 2
        val secondRoot = (duration + sqrtDeterminant) / 2
        val minTime = ceil(firstRoot + 0.0001).toLong()
        val maxTime = floor(secondRoot - 0.0001).toLong()

        return (maxTime - minTime) + 1

    }

    private fun String.generateRecords() : List<Record> {
        val lines = this.split("\r\n")
        val times = lines[0].split("[:\\s]+".toRegex()).filter { it.toIntOrNull() != null }
        val distances = lines[1].split("[:\\s]+".toRegex()).filter { it.toIntOrNull() != null }
        val records = times.zip(distances).map { Record(duration = it.first.toDouble(), distance = it.second.toDouble()) }
        return records
    }

    data class Record(val duration : Double, val distance : Double)
}