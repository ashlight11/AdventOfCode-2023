
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DaySevenTest {

    private val hand1J = Hand("JKKK2", true) // weaker
    private val hand1 = Hand("QQQQ2", true)
    val input = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
        """.trimIndent()
    private val h1 = Hand("32T3K", false).toSubClass()
    private val b1 = 765
    private val h2 = Hand("T55J5", false).toSubClass()
    private val b2 = 684
    private val h3 = Hand("KK677", false).toSubClass()
    private val b3 = 28
    private val h4 = Hand("KTJJT", false).toSubClass()
    private val b4 = 220
    private val h5 = Hand("QQQJA", false).toSubClass()
    private val b5 = 483


    @Test
    fun `P1 should find card type`() {
        hand1.toSubClass().shouldBeInstanceOf<FourOfAKind>()
    }

    @Test
    fun `P1 should transform to map of hands`(){
        val lines = input.split("\n").toList()
        val mapOfHands = lines.toMapOfHands(false)
        val expectedResult = mapOf(Pair(h1,b1), Pair(h2,b2), Pair(h3,b3), Pair(h4, b4), Pair(h5, b5))
        println(mapOfHands)
        println(expectedResult)
        mapOfHands.shouldContainExactly(expectedResult)
    }

    @Test
    fun `P1 should compute bids`(){
        val lines = input.split("\n").toList()
        val mapOfHands = lines.toMapOfHands(withJokers = false)
        val bidWon = AllHands(mapOfHands).totalBids()
        bidWon.shouldBeEqual(6440L)
    }

    @Test
    fun `P2 should find card type`() {
        hand1J.toSubClass().shouldBeInstanceOf<FourOfAKind>()
    }

    @Test
    fun `P2 should transform to map of hands`(){
        val lines = input.split("\n").toList()
        val mapOfHands = lines.toMapOfHands(true)
        val expectedResult = mapOf(Pair(h1,b1), Pair(h2,b2), Pair(h3,b3), Pair(h4, b4), Pair(h5, b5))

        val sortedMap = AllHands(mapOfHands).globalSortedMap
        println(sortedMap)
        //println(expectedResult)
        //mapOfHands.shouldContainExactly(expectedResult)
    }

    @Test
    fun `P2 should compute bids`(){
        val lines = input.split("\n").toList()
        val mapOfHands = lines.toMapOfHands(withJokers = true)
        val bidWon = AllHands(mapOfHands).totalBids()
        bidWon.shouldBeEqual(5905L)
    }

    @Test
    fun `should compare with Jokers`(){
        val comparison = handComparator.compare(hand1, hand1J).shouldBeEqual(1)
    }


}