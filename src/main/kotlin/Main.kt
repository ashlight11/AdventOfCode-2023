import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day6Input.txt")
    val file = File(url!!.toURI())
    val day = DaySix(file)
    day.computePartTwo()
}



