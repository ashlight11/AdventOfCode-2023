import java.io.File

class DayEight(file: File) {
    private val splitText = file.readText()
    private val input = Input(splitText)

    fun computePartOne() {
        val nodesMap = input.nodesAsText.parseAsNodesMap()
        val result = input.readInstructions(nodesMap)
        println(result)
    }

    fun computePartTwo() {

    }

}

class Input(inputAsText: String) {
    private val splitText = inputAsText.split("\n\\s*\n".toRegex())
    val instructions = splitText[0].extractLetters()
    val nodesAsText = splitText[1].split("\r\n")

    fun readInstructions(nodesMap: Map<String, Node>): Int {
        var temp = nodesMap["AAA"]!!
        var counter = 0

        while (!temp.isTheEnd()) {
            val index = (counter % instructions.length)
            when (instructions[index]) {
                'L' -> {
                    val left = temp.left
                    if(left == "ZZZ"){
                        break
                    } else {
                        temp = nodesMap[temp.left]!!
                    }
                }

                'R' -> {
                    val right = temp.right
                    if(right == "ZZZ"){
                        break
                    } else {
                        temp = nodesMap[temp.right]!!
                    }
                }
            }
            counter++
        }

        return counter + 1
    }
}

class BinaryNode<T>(var value: T, var leftChild: BinaryNode<T>? = null, var rightChild: BinaryNode<T>? = null) {
    override fun toString(): String {
        return "($value): (" + leftChild?.value + ", " + rightChild?.value + ")"
    }
}

fun List<String>.parseAsNodes(): Set<BinaryNode<String>> {
    return this.map { string ->
        val (value, leftChild, rightChild) = string.split(Regex("[=,]"))
        BinaryNode(
            value = value.extractLetters(),
            leftChild = BinaryNode(leftChild.extractLetters()),
            rightChild = BinaryNode(rightChild.extractLetters())
        )
    }.toSet()
}

fun List<String>.parseAsNodesMap(): Map<String, Node> {
    return this.associate { string ->
        val (value, leftChild, rightChild) = string.split(Regex("[=,]"))
        value.extractLetters() to
                Node(
                    left = leftChild.extractLetters(),
                    right = rightChild.extractLetters()
                )
    }
}

private fun String.extractLetters(): String {
    return this.filter { it.isLetter() }.trim()
}

data class Node(val left: String?, val right: String?) {
    fun isTheEnd(): Boolean {
        return left == "ZZZ" && right == "ZZZ"
    }
}