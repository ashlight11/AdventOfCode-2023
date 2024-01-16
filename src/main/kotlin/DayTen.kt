class DayTen {


    open class PipePart(val value: Char, val listOfConnections: List<Directions>) {
        fun toSubclass(): PipePart {
            return when (value) {
                '|' -> VerticalPipe(value)
                '-' -> HorizontalPipe(value)
                'L' -> LPipe(value)
                'J' -> JPipe(value)
                '7' -> SevenPipe(value)
                'F' -> FPipe(value)
                'S' -> SPipe(value)
                else -> Ground(value)
            }
        }
    }

    /**
     * Represents | and connects north and south
     */
    class VerticalPipe(value: Char) : PipePart(value, listOf(Directions.NORTH, Directions.SOUTH))

    /**
     * Represents - and connects east and west
     */
    class HorizontalPipe(value: Char) : PipePart(value, listOf(Directions.EAST, Directions.WEST))

    /**
     * Represents L and connects north and east
     */
    class LPipe(value: Char) : PipePart(value, listOf(Directions.NORTH, Directions.EAST))

    /**
     * Represents J and connects north and east
     */
    class JPipe(value: Char) : PipePart(value, listOf(Directions.NORTH, Directions.WEST))

    /**
     * Represents 7 and connects north and east
     */
    class SevenPipe(value: Char) : PipePart(value, listOf(Directions.SOUTH, Directions.WEST))

    /**
     * Represents F and connects north and east
     */
    class FPipe(value: Char) : PipePart(value, listOf(Directions.SOUTH, Directions.EAST))

    /**
     * Represents S and is the starting point => Connects anything
     */
    class SPipe(value: Char) :
        PipePart(value, listOf(Directions.NORTH, Directions.SOUTH, Directions.EAST, Directions.WEST))

    /**
     * Represents . and connects nothing
     */
    class Ground(value: Char) : PipePart(value, emptyList())
    enum class Directions {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
}