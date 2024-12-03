package year2024

import java.io.File
import kotlin.math.abs

fun main() {
    val url = object {}.javaClass.getResource("/year2024/day02.txt")
    val file = File(url!!.toURI())
    val listOfRecords = file.readLines().map { line -> line.split(" ").map { it.toInt() } }
    val listOfRecordValidity = listOfRecords.map { line -> line.isASafeRecord(problemDampener = true) }
    print(listOfRecordValidity.count { it })
}

fun List<Int>.removeElementStartingFromIndex(n: Int, element: Int): List<Int> {
    return this.filterIndexed { index, value -> !(index == n && value == element) }
}

fun List<Int>.isASafeRecord(problemDampener: Boolean = false): Boolean {
    // Treating ascending order
    val range = if (this.first() < this.last()) {
        (1..3)
    // And descending order
    } else {
        (-3..-1)
    }
    val windowedList =
        this.windowed(2, 1).mapIndexed { index, tuple ->
            (tuple[1] - tuple[0] in range) || (
                    problemDampener && (
                            (this.removeElementStartingFromIndex(index, tuple[0])).isASafeRecord()
                                    ||
                                    this.removeElementStartingFromIndex(index + 1, tuple[1]).isASafeRecord()))
        }
    return windowedList.all { it }

    /**
    return if (this.first() < this.last()) {
    windowedList.fold(
    initial = true,
    operation = { acc, tuple ->
    acc && (tuple[1] - tuple[0]) in (1..3)
    })
    } else {
    windowedList.foldRight(
    initial = true,
    operation = {
    current, acc ->
    acc && (current[1] - current[0]) in (-3..-1)
    }
    )
    }**/
}