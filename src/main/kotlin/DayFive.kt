import java.io.File

class DayFive(file: File) {
    val baseFile = file

    fun computePartOne() {
        val content = baseFile.readText().split("\n\r")
        // extract seeds
        val seeds = content[0].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toLong() }
        // create a sublist of maps => take away the seeds
        val contentWithoutSeeds = content.subList(1, content.size)
        // separate maps into groups
        val groups = contentWithoutSeeds.map { string ->
            // separate groups into lines
            string.split(":")[1].split("\r")
                .filter { it.isNotBlank() }
        }
            .map { toTransformers(it) } // map each line to a "Transformer" object

        // compute location for each seed, based on all the maps (=groups)
        val locations = seeds.map { computeLocation(it, groups) }
        println(locations)

        val minimumLocation = locations.min()
        println("Minimum location is $minimumLocation")

    }

    data class Transformer(val sourceStart: Long, val destinationStart: Long, val range: Long)

    /**
     * Convert a string with a known format of "XXXXX XXX XXXX" with X : Digit to a Transformer
     */
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


    /*
    Unused in final solution
     */
    private fun computeDestinationMap(source: Long, map: Map<Long, Long>): Long {
        return if (map.containsKey(source)) {
            map[source]!!
        } else {
            source
        }
    }

    /**
     * Calculate output with, as inputs, a source value and a list of transformations possible
     * @param source : The initial value to transform
     * @param transformers : List of transformations applicable
     */
    private fun computeDestination(source: Long, transformers: List<Transformer>): Long {
        // set result to initial value. If we can't transform it, the result is the value
        var result = source
        // for each transformation possible
        for (transformer in transformers) {
            val sourceStart = transformer.sourceStart
            // if the value input is in the range of the current transformer
            if (source in sourceStart..<sourceStart + transformer.range) {
                // map the source to destination
                result = transformer.destinationStart + (source - sourceStart)
            }
        }
        return result
    }

    /*
    Unused in final solution
     */
    private fun computeLocationMap(source: Long, mappers: List<Map<Long, Long>>): Long {
        println("compute location for seed $source")
        var temp = source
        for (mapper in mappers) {
            temp = computeDestinationMap(temp, mapper)
        }
        return temp
    }

    /**
     * Chain all the destination calculations for a given source = seed.
     * @param source : The initial value to transform
     * @param transformers : Each element in transformers is a list of transformations.
     * e.g. transformers[0] lists all the transformations possible for seed to soil
     * @return The result of all the transformations
     */
    private fun computeLocation(source: Long, transformers: List<List<Transformer>>): Long {
        println("compute location for seed $source")
        var temp = source
        for (transformer in transformers) {
            temp = computeDestination(temp, transformer)
        }
        return temp
    }
}