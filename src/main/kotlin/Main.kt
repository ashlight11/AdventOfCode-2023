import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day09.txt")
    val file = File(url!!.toURI())
    val day = DayNine(file)
    day.computePartTwo()
}



