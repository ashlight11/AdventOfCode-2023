import java.io.File

class DayEight(file: File) {
    private val splitText = file.readText()
    private val input = Input(splitText)

    fun computePartOne() {
        val result = input.readInstructions("AAA")
        println(result)
    }

    fun computePartTwo() {
        val result = input.readInstructionsForGhostsButBetter()
        println(result)
    }

}

class Input(inputAsText: String) {
    private val splitText = inputAsText.split("\n\\s*\n".toRegex())
    private val instructions = splitText[0].extractLetters()
    private val nodesAsText = splitText[1].split("\r\n")
    val nodesMap = nodesAsText.parseAsNodesMap()

    private fun List<String>.parseAsNodesMap(): Map<String, Node> {
        return this.associate { string ->
            val (value, leftChild, rightChild) = string.split(Regex("[=,]"))
            value.extractLetters() to
                    Node(
                        value = value.extractLetters(),
                        left = leftChild.extractLetters(),
                        right = rightChild.extractLetters()
                    )
        }
    }

    fun readInstructions(startString : String): Int {
        var temp = nodesMap[startString]!!
        var counter = 0

        while (!temp.isTheEnd(startString)) {
            val index = (counter % instructions.length)
            when (instructions[index]) {
                'L' -> {
                    temp = nodesMap[temp.left]!!
                }
                'R' -> {
                    temp = nodesMap[temp.right]!!
                }
            }
            counter++
        }
        return counter
    }

    private fun getStartingNodes(): List<Node> {
        return nodesMap.values.filter { it.value.endsWith('A') }
    }

    private fun getNodesWithInstruction(current: List<Node>, instruction: Char): List<Node> {
        when (instruction) {
            'L' -> {
                return current.map { nodesMap[it.left]!! }
            }

            'R' -> {
                return current.map { nodesMap[it.right]!! }
            }
        }
        return current
    }

    fun readInstructionsForGhosts(): Int {
        var temp = getStartingNodes()
        var counter = 0
        while (temp.shouldContinue()) {
            val index = (counter % instructions.length)
            temp = getNodesWithInstruction(temp, instructions[index])
            counter++
        }
        return counter
    }

    fun readInstructionsForGhostsButBetter(): Long {
        val startingNodes = getStartingNodes()
        val iterations = startingNodes.map { readInstructions(it.value) }
        val result = iterations.foldIndexed(
            initial = iterations[0].toLong(),
            operation = {index, acc, _ ->
            findLCM(iterations[index].toLong(), acc)}
        )
        return result
    }

}


private fun String.extractLetters(): String {
    return this.filter { it.isLetterOrDigit() }.trim()
}
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return lcm
}


data class Node(val value: String, val left: String?, val right: String?) {
    fun isTheEnd(condition : String): Boolean {
        return if(condition === "AAA") {
            value == "ZZZ"
        } else {
            value.endsWith("Z")
        }
    }
}

fun isNotEndForGhost(): (Node) -> Boolean {
    return { !it.value.endsWith("Z") }
}

fun List<Node>.shouldContinue(): Boolean {
    return any(isNotEndForGhost())
}