import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day05.txt")
    val file = File(url!!.toURI())
    val day = DayFive(file)
    day.computePartTwo()
}



