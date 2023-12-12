import java.io.File

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day5Input.txt")
    val file = File(url!!.toURI())
    val day = DayFive(file)
    day.computePartOne()
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    //println("Program arguments: ${args.joinToString()}")
}



