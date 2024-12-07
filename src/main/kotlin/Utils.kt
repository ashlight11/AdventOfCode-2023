import kotlin.math.pow


/**
 * Linear regression :
 * y = a * x + b
 */
fun List<String>.computeOutputValue(value: Long): Long {
    val longList = this.map { it.toLong() }
    val indices = longList.indices.map { it.toLong() }
    val multiplicationFactor = computeA(indices, longList)
    val b = computeB(indices, longList, multiplicationFactor)
    return (multiplicationFactor.times(value)).plus(b)
}

/**
 * a = cov(X,Y) / var(X)
 */
private fun computeA(xValues: List<Long>, yValues: List<Long>): Long {
    return covariance(xValues, yValues).div(xValues.variance())
}

/**
 * mean(x) - multiplicationFactor * mean(y)
 */
private fun computeB(xValues: List<Long>, yValues: List<Long>, multiplicationFactor : Long): Long{
    val meanX = xValues.mean()
    val meanY = yValues.mean()
    return meanY.minus(multiplicationFactor*meanX)
}

/**
 * (Sum of [(Xi - mean(X))*(Yi-mean(Y)])/(n)
 * with n the number of points
 */
private fun covariance(xValues: List<Long>, yValues: List<Long>): Long {
    val n = yValues.size
    val meanX = xValues.mean()
    val meanY = yValues.mean()
    val top = yValues.reduceIndexed { index, acc, current ->
        acc.plus(
            (xValues[index].minus(meanX))
                .times(current.minus(meanY))
        )
    }
    return top.div(n)
}

/**
 * (Sum of (Xi-mean(X))²)/n
 */
private fun List<Long>.variance(): Long {
    val n = this.size
    val mean = this.mean()
    val top = this.reduce { acc, long ->
        acc.plus(
            (long.minus(mean)).toDouble()
                .pow(2).toLong()
        )
    }
    return top.div(n)

}

private fun List<Long>.mean(): Long {
    val n = this.size
    return this.sum().div(n)
}

data class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

data class LongPoint(val x: Long, val y: Long) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

tailrec fun factorialTailRecursive(n: Int, accumulator: Int = 1): Int {
    return if (n <= 1) {
        accumulator
    } else {
        factorialTailRecursive(n - 1, n * accumulator)
    }
}

fun String.parseAsIntList(regex: Regex): List<Int> {
    return this.split(regex).map { it.trim().toInt() }
}

fun String.readLines() : List<String>{
    return this.split("\n")
}
