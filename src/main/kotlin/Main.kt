import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day7Input.txt")
    val file = File(url!!.toURI())
    val day = DaySeven(file)
    day.computePartTwo()
}



