import java.io.File

class DayFive(file: File) {
    val baseFile = file

    fun computePartOne() {
        val content = baseFile.readText().split("\n\r")
        val seeds = content[0].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toLong() }
        val contentWithoutSeeds = content.subList(1, content.size)
        val groups = contentWithoutSeeds.map { string ->
            string.split(":")[1].split("\r")
                .filter { it.isNotBlank() }
        }
            .map { toTransformers(it) }
            .map { toMapper(it) }

        val locations = seeds.map { computeLocation(it, groups) }
        println(locations)

        val minimumLocation = locations.min()
        println("Minimum location is $minimumLocation")

    }

    data class Transformer(val sourceStart: Long, val destinationStart: Long, val range: Long)

    private fun toTransformer(string: String): Transformer {
        val elements = string.split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it.toLong() }
        return Transformer(sourceStart = elements[1], destinationStart = elements[0], range = elements[2])
    }

    private fun toTransformers(strings: List<String>): List<Transformer> {
        return strings.map { toTransformer(it) }
    }

    /*
    Does not work for bigger numbers... not efficient, too much memory needed
     */
    private fun toMapper(list: List<Transformer>): Map<Long, Long> {
        println("converting to Mapper")
        var longMap: Map<Long, Long> = emptyMap<Long, Long>()
        for (element in list) {
            val source = element.sourceStart
            val range = element.range
            val destination = element.destinationStart
            for (i in source..<source + range) {
                val newValue = mapOf(i to (destination + (i - source)))
                //println(newValue)
                longMap = longMap.plus(newValue)
            }
        }
        println("done")
        return longMap
    }


    private fun computeDestination(source: Long, map: Map<Long, Long>): Long {
        return if (map.containsKey(source)) {
            map[source]!!
        } else {
            source
        }
    }

    private fun computeLocation(source: Long, mappers: List<Map<Long, Long>>): Long {
        println("compute location for seed $source")
        var temp = source
        for (mapper in mappers) {
            temp = computeDestination(temp, mapper)
        }
        return temp
    }
}