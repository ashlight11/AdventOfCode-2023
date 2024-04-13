fun main(args: Array<String>) {

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
fun SpringGroup.permutationsNonRestricted(elementsToPlace: Int) : Int {
    val n = springs.size
    val num = factorialTailRecursive(n - numberOfDamaged)
    val dem = factorialTailRecursive(n- elementsToPlace - numberOfDamaged)
    return num.div(dem)
}

fun SpringGroup.permutationsWithRestriction(elementsToPlace: Int, restrictionsToPlace: Int) : Int {
    val n = springs.size - restrictionsToPlace
    val num = factorialTailRecursive(n - numberOfDamaged)
    val dem = factorialTailRecursive(n- elementsToPlace - numberOfDamaged)
    return num.div(dem)
}

fun String.toSprings(): List<Spring> {
    return this.map { Spring(it) }.toList()
}

/**
 * The number of total permutations possible is :
 * nbOfGroupsOfGivenSizePossibleInWholeLine - nbOfGroupsOfSaidSizeAlreadyPresentInLine
 * - (nbOfElementsLeftToPlace / groupSize) - nbOfWorkingSpringsLeftToPlace
 */

enum class SpringState {
    DAMAGED,
    FUNCTIONAL,
    UNKNOWN,
    ERROR
}

