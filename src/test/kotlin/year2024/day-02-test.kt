package year2024

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class Day02Test {
    private val testInput =
                    "7 6 4 2 1\n" +
                    "1 2 7 8 9\n" +
                    "9 7 6 2 1\n" +
                    "1 3 2 4 5\n" +
                    "8 6 4 4 1\n" +
                    "1 3 6 7 9"

    @Test
    fun countSafeRecords(){
        val listOfRecords = testInput.split("\n").map { line -> line.split(" ").map { it.toInt() }}
        val listOfRecordValidity = listOfRecords.map { line -> line.isASafeRecord() }
        //print(listOfRecordValidity)
        listOfRecordValidity.count{it}.shouldBe(2)
    }

    @Test
    fun countSafeRecordsWithProblemDampener(){
        val listOfRecords = testInput.split("\n").map { line -> line.split(" ").map { it.toInt() }}
        val listOfRecordValidity = listOfRecords.map { line -> line.isASafeRecord(problemDampener = true) }
        print(listOfRecordValidity)
        listOfRecordValidity.count{it}.shouldBe(4)
    }
}