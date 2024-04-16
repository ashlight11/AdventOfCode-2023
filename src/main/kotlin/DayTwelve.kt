import java.io.File
import kotlin.math.ceil

fun main(args: Array<String>) {
    val url = object {}.javaClass.getResource("day12.txt")
    val file = File(url!!.toURI())
    val sumOfArrangements = file.readLines()
        .parseToCharArrayAndBooleanArray(repeatingFactor = 5, addTextForString = "?", ",")
        .map { countArrangements(it.key, it.value) }
        .sum()
    println(sumOfArrangements)

}

fun List<String>.parseToMap(): Map<String, List<Int>> {
    return this.map {
        val split = it.split(" ");
        split[0] to split[1].toListOfInts()
    }.toMap()
}

fun List<String>.parseToCharArrayAndBooleanArray(repeatingFactor: Int, addTextForString: String?, addTextForNums: String?): Map<CharArray, BooleanArray> {
    return this.map {
        val split = it.split(" ");
        val text = split[0].repeatNTimesAndAddInBetween(repeatingFactor, addTextForString)
            .toCharArray()
            .appendAtFirstAndAtLast(charArrayOf('.'))
        val nums = split[1].repeatNTimesAndAddInBetween(repeatingFactor, addTextForNums)
            .toListOfInts()
            .toBooleanArray()
        text to nums
    }.toMap()
}

fun String.repeatNTimesAndAddInBetween(repeatingFactor: Int, addText: String?): String {
    return if(addText.isNullOrBlank()){
        this.repeat(repeatingFactor)
    } else {
        (this + addText).repeat(repeatingFactor).removeSuffix(addText)
    }

}

fun CharArray.appendAtFirstAndAtLast(characterArray: CharArray): CharArray {
    return characterArray + this + characterArray
}

fun String.toListOfInts(): List<Int> {
    return this.split(",").map { it.toInt() }
}

fun List<Int>.toBooleanArray(): BooleanArray {
    val result = BooleanArray(this.sum() + 2 + this.size - 1)
    var index = 1
    this.forEach {
        (index..<index + it).forEach { idx ->
            result[idx] = true
        }
        index = index + it
        result[index] = false
        index++
    }
    result[0] = false
    result[result.size - 1] = false
    return result
}

data class Spring(val value: Char) {
    val state = when (value) {
        '.' -> SpringState.FUNCTIONAL
        '#' -> SpringState.DAMAGED
        '?' -> SpringState.UNKNOWN
        else -> SpringState.ERROR

    }

    override fun toString(): String {
        return "'$value'"
    }
}

class SpringGroup(val springs: List<Spring>) {
    val numberOfDamaged = springs.count { it.state == SpringState.DAMAGED }
}

// not restricted = we don't care separating # with "."
// if k elements are already placed : elementsToPlace - k among n-k
fun SpringGroup.permutationsNonRestricted(elementsToPlace: Int): Int {
    val n = springs.size
    val num = factorialTailRecursive(n - numberOfDamaged)
    val dem = factorialTailRecursive(n - elementsToPlace - numberOfDamaged)
    return num.div(dem)
}

fun SpringGroup.permutationsWithRestriction(elementsToPlace: Int, restrictionsToPlace: Int): Int {
    val n = springs.size - restrictionsToPlace
    val num = factorialTailRecursive(n - numberOfDamaged)
    val dem = factorialTailRecursive(n - elementsToPlace - numberOfDamaged)
    return num.div(dem)
}

fun String.toSprings(): List<Spring> {
    return this.map { Spring(it) }.toList()
}

/**
 * The number of total permutations possible is :
 * nbOfGroupsOfGivenSizePossibleInWholeLine - nbOfGroupsOfSaidSizeAlreadyPresentInLine
 * - (nbOfElementsLeftToPlace / groupSize) - nbOfWorkingSpringsLeftToPlace
 *
 * groupsLeftToPlace  = (nbOfElementsLeftToPlace / groupSize)
 */
fun String.getTotalPermutationsForGroup(groupSize: Int, remainingGroupsToPlace: List<Int>): Int {
    val toSubstract = ceil((remainingGroupsToPlace.size - 1 + remainingGroupsToPlace.sum()).toFloat() / groupSize)
    val nbOfGroupsOfGivenSizePossibleInWholeLine = this.length / groupSize
    val nbOfGroupsOfSaidSizeAlreadyPresentInLine = this.findGroupsOfSize(groupSize)
    return nbOfGroupsOfGivenSizePossibleInWholeLine - nbOfGroupsOfSaidSizeAlreadyPresentInLine - toSubstract.toInt()
}

// [.?]#{$groupSize}[.?]
fun String.findGroupsOfSize(groupSize: Int): Int {
    val regex = Regex("[.?]#{$groupSize}[.?]")
    return regex.findAll(this).count()

}


enum class SpringState {
    DAMAGED,
    FUNCTIONAL,
    UNKNOWN,
    ERROR
}

fun countArrangements(chars: CharArray, springs: BooleanArray): Long {
    val n = chars.size
    val m = springs.size
    val dp = Array(n + 1) { LongArray(m + 1) }
    dp[n][m] = 1

    for (i in n - 1 downTo 0) {
        for (j in m - 1 downTo 0) {
            var damaged = false
            var operational = false
            when (chars[i]) {
                '#' -> {
                    damaged = true
                }

                '.' -> {
                    operational = true
                }

                else -> {
                    operational = true
                    damaged = true
                }
            }
            var sum: Long = 0
            if (damaged && springs[j]) {
                sum += dp[i + 1][j + 1]
            } else if (operational && !springs[j]) {
                sum += dp[i + 1][j + 1] + dp[i + 1][j]
            }
            dp[i][j] = sum
        }
    }
    return dp[0][0]
}