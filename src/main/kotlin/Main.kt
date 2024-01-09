import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day08.txt")
    val file = File(url!!.toURI())
    val day = DayEight(file)
    day.computePartTwo()
}



