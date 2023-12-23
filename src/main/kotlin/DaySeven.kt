import java.io.File

class DaySeven(file: File) {
    private val initialInput = file.readLines()

    fun computePartOne(){
        compute(false)
    }
    fun computePartTwo(){
        compute(true)
    }

    private fun compute(withJokers: Boolean) {
        val mapOfHands = initialInput.toMapOfHands(withJokers = withJokers)
        val handsWithBids = AllHands(mapOfHands)
        handsWithBids.debug()
        val bidWon = handsWithBids.totalBids()
        println("total won : $bidWon")
    }
}

fun List<String>.toMapOfHands(withJokers: Boolean): Map<Hand, Int> {
    return this.associate { Hand(it.split(" ")[0], withJokers).toSubClass() to it.split(" ")[1].toInt() }
}

/**
 * -1 if firstHand < secondHand
 * 0 if firstHand == secondHand /!\ should not happen
 * 1 if firstHand > secondHand
 */
val handComparator = object : Comparator<Hand> {
    override fun compare(o1: Hand?, o2: Hand?): Int {
        for (index in o1!!.baseCharArray.indices) {
            if (o1.baseCharArray[index] != o2!!.baseCharArray[index]) {
                return o1.baseCharArray[index].getValue(o1.withJokers).compareTo(o2.baseCharArray[index].getValue(o2.withJokers))
            }
        }
        return 0
    }
}

open class Hand(val input: String, val withJokers: Boolean) {

    val baseCharArray = input.toCharArray()
    val charMap: Map<Char, Int> = this.getContent()

    init {

    }

    override fun toString(): String {
        return this.javaClass.toString() + ": " + this.input
    }

    fun toSubClass(): Hand {
        val shouldLevelUp = withJokers && this.input.contains('J')
        when (charMap.size) {
            1 -> return FiveOfAKind(input, withJokers)
            2 -> {
                return if (shouldLevelUp) {
                    FiveOfAKind(input, true)
                } else if(charMap.containsValue(4)){
                    FourOfAKind(input, withJokers)
                } else {
                    FullHouse(input, withJokers)
                }
            }
            3 -> {
                val countJs = input.count { it == 'J' }
                return if (shouldLevelUp) {
                    if(countJs == 1){
                        FullHouse(input, true)
                    } else {
                        FourOfAKind(input, true)
                    }
                } else if(charMap.containsValue(3)) {
                    ThreeOfAKind(input, withJokers)
                }
                else {
                    TwoPair(input, withJokers)
                }
            }
            4 -> return if(shouldLevelUp){
                ThreeOfAKind(input, true)
            } else {
                OnePair(input, withJokers)
            }

        }
        return if (shouldLevelUp){
            OnePair(input, true)
        } else {
            HighCard(input, withJokers)
        }
    }

    /**
     * Count the occurrences of characters in the given string
     * @return a map with key : Char and value : Int = the number of occurrences of said char
     */
    private fun getContent(): Map<Char, Int> {
        val charMap = mutableMapOf<Char, Int>()
        for (c in this.baseCharArray) {
            if (charMap.containsKey(c)) {
                charMap[c] = charMap[c]?.plus(1)!!
            } else {
                charMap[c] = 1
            }
        }
        return charMap.toMap()
    }
}

/**
 * Where all five cards have the same label: AAAAA
 */
class FiveOfAKind(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * Four of a kind, where four cards have the same label and one card has a different label: AA8AA
 */
class FourOfAKind(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
 */
class FullHouse(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
 */
class ThreeOfAKind(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
 */
class TwoPair(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
 */
class OnePair(input: String, withJokers: Boolean) : Hand(input, withJokers)

/**
 * High card, where all cards' labels are distinct: 23456
 */
class HighCard(input: String, withJokers: Boolean) : Hand(input, withJokers)

fun Char.getValue(withJokers : Boolean): Int {
    val characterKeys = if (withJokers){
        listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
    } else {
        listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    }
    val characterValues = (characterKeys.size.downTo(1)).toList()
    val charactersMap = characterKeys.zip(characterValues).toMap()
    return if (charactersMap[this] != null) charactersMap[this]!! else 0
}

class AllHands(handMap: Map<Hand, Int>) {
    private val fiveOfAKinds = handMap.filter { it.key is FiveOfAKind }.toSortedMap(handComparator)
    private val fourOfAKinds = handMap.filter { it.key is FourOfAKind }.toSortedMap(handComparator)
    private val fullHouses = handMap.filter { it.key is FullHouse }.toSortedMap(handComparator)
    private val threeOfAKinds = handMap.filter { it.key is ThreeOfAKind }.toSortedMap(handComparator)
    private val twoPairs = handMap.filter { it.key is TwoPair }.toSortedMap(handComparator)
    private val onePairs = handMap.filter { it.key is OnePair }.toSortedMap(handComparator)
    private val highCards = handMap.filter { it.key is HighCard }.toSortedMap(handComparator)

    val globalSortedMap = highCards + onePairs + twoPairs + threeOfAKinds + fullHouses + fourOfAKinds + fiveOfAKinds

    fun debug(){
        fourOfAKinds.forEach { println(it) }
    }

    fun totalBids(): Long {
        return globalSortedMap.values.foldIndexed(initial = 0L,
            operation = { index, acc, i -> acc + ((index+1) * i) })
    }

}
