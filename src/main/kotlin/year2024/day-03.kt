package year2024

import java.io.File
import kotlin.time.times

fun main(){
    val url = object {}.javaClass.getResource("/year2024/day03.txt")
    val file = File(url!!.toURI())
    val text = file.readText()

    val mulList = text.toMulList()
    val result = mulList.sumOf { it.result }
    println("result part 1 : $result")

    val dontList = text.toDontList()
    //println(dontList)

    val partTwoResult = computeResultWithMulAndDont(mulList, dontList)
    println("result part 2 : $partTwoResult")

}

fun IntRange.includes(other : IntRange) : Boolean {
    return this.first <= other.first && this.last >= other.last
}

fun computeResultWithMulAndDont(mulList: List<Mul>, dontList: List<Dont>) : Long {
    return mulList.sumOf { mul ->
        val overlap = dontList.any { dont -> dont.position.includes(mul.position) }
        if(!overlap){
            mul.result
        } else
            0
    }
}

fun String.toMulList() : List<Mul>{
    val mulRegex = Regex("mul\\(\\d+,\\d+\\)")
    return mulRegex.findAll(this).map { it.toMul() }.toList()
}

fun MatchResult.toMul() : Mul {
    val regex = Regex("\\d+")
    val nums = regex.findAll(this.value).map { it.value.toInt() }
    return Mul(nums.first(), nums.last(), this.range)
}

fun String.toDontList() : List<Dont>{
    // TODO : understand how to match newline in possible characters instead of "."
    val dontRegex = Regex("don't\\(\\).*?do\\(\\)")
    return dontRegex.findAll(this).map { it.toDont() }.toList()
}

fun MatchResult.toDont() : Dont{
    return Dont(this.range)
}

data class Mul(
    val left : Int,
    val right : Int,
    val position : IntRange
) {
    val result : Long = (left*right).toLong()
}

data class Dont(val position : IntRange){}