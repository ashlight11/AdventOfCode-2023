package year2023

import java.io.File

class DayFive(file: File) {
    private val baseFile = file

    fun computePartOne() {
        val content = baseFile.readText().split("\n\r")
        // extract seeds
        val seeds = content[0].split(":")[1]
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
            .map { it.toLong() }
        // create a sublist of maps => take away the seeds
        val contentWithoutSeeds = content.subList(1, content.size)
        // separate maps into groups
        val groups = contentWithoutSeeds.map { string ->
            // separate groups into lines
            string.split(":")[1]
                .split("\r")
                .filter { it.isNotBlank() }
        }
            .map { it.toTransformers() } // map each line to a "Transformer" object

        // compute location for each seed, based on all the maps (=groups)
        val locations = seeds.map { computeLocation(it, groups) }
        println(locations)

        val minimumLocation = locations.min()
        println("Minimum location is $minimumLocation")

    }

    fun computePartTwo(){
        val content = baseFile.readText().split("\n\r")
        // extract seeds
        val seeds = content[0].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toLong() }
        // create a sublist of maps => take away the seeds
        val contentWithoutSeeds = content.subList(1, content.size)
        // separate maps into groups
        val groups = contentWithoutSeeds.map { string ->
            // separate groups into lines
            string.split(":")[1]
                .split("\r")
                .filter { it.isNotBlank() }
        }
            .map { it.toTransformers() } // map each line to a "Transformer" object

        // compute location for each seed, based on all the maps (=groups)
        /*val locations = computeLocationsFromSeedRanges(seeds.toSeedRanges(), groups)
        //println(locations)

        val minimumLocation = locations.min()
        println("Minimum location is $minimumLocation")*/

        val min = seeds.toSeedRanges().minimumLocation(groups)
        println(min)
    }

    data class Transformer(val sourceStart: Long, val destinationStart: Long, val range: Long)

    data class SeedRange(val start: Long, val range : Long){
        val end = start+range-1
    }


    /**
     * Convert a string with a known format of "XXXXX XXX XXXX" with X : Digit to a Transformer
     */
    private fun String.toTransformer(): Transformer {
        val elements = this.split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        return Transformer(sourceStart = elements[1], destinationStart = elements[0], range = elements[2])
    }

    private fun List<String>.toTransformers(): List<Transformer> {
        return this.map { it.toTransformer() }
    }

    /**
     * Convert the seeds into their corresponding ranges
     */
    private fun List<Long>.toSeedRanges() : List<SeedRange>{
        var ranges = emptyList<SeedRange>()
        for (i in this.indices step(2)){
            ranges = ranges.plus(SeedRange(this[i], this[i+1]))
        }
        return ranges
    }

    private fun computeLocationsFromSeedRanges(seedRanges : List<SeedRange>, transformers: List<List<Transformer>>) : List<Long> {
        var locations = emptyList<Long>()
        for (seedRange in seedRanges){
            println("start range")
            for (seed in seedRange.start..seedRange.end){
                locations = locations.plus(computeLocation(seed, transformers))
            }
            println("stop range")
        }
        return  locations
    }

    private fun List<SeedRange>.minimumLocation(transformers: List<List<Transformer>>) : Long{
        var location = computeLocation(this[0].start, transformers)
        for (seedRange in this){
            println("start range")
            for (seed in seedRange.start..seedRange.end){
                val tempLocation = computeLocation(seed, transformers)
                if(tempLocation < location){
                    location = tempLocation
                }
            }
            println("stop range")
        }
        return  location
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


    /**
     * Chain all the destination calculations for a given source = seed.
     * @param source : The initial value to transform
     * @param transformers : Each element in transformers is a list of transformations.
     * e.g. transformers[0] lists all the transformations possible for seed to soil
     * @return The result of all the transformations
     */
    private fun computeLocation(source: Long, transformers: List<List<Transformer>>): Long {
        //println("compute location for seed $source")
        var temp = source
        for (transformer in transformers) {
            temp = computeDestination(temp, transformer)
        }
        return temp
    }
}