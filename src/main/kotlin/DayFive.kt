import java.io.File

class DayFive (file : File) {
    val baseFile = file

    fun computePartOne(){
        val content = baseFile.readText().split("\n\r")
        val seeds = content[0].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val contentWithoutSeeds = content.subList(1, content.size)
        val groups = contentWithoutSeeds.map { string -> string.split(":")[1].split("\r")
            .filter { it.isNotBlank() } }
            .map { toTransformers(it) }
            .map { toMapper(it) }

        println(groups)

    }

    data class Transformer(val sourceStart : Int, val destinationStart : Int, val range : Int)

    private fun toTransformer(string: String) : Transformer{
        val elements = string.split("\\s+".toRegex()).filter{it.isNotEmpty()}.map { it.toInt() }
        return Transformer(sourceStart = elements[1], destinationStart = elements[0], range = elements[2])
    }

    private fun toTransformers(strings : List<String>) : List<Transformer>{
        return strings.map { toTransformer(it) }
    }

    private fun toMapper(list : List<Transformer>) : Map<Int,Int>{
        var intMap : Map<Int, Int> = emptyMap<Int, Int>()
        for (element in list){
            val source = element.sourceStart
            val range = element.range
            val destination = element.destinationStart
            for (i in source ..< source + range){
                val newValue = mapOf(i to (destination + (i-source)))
                //println(newValue)
                intMap = intMap.plus(newValue)
            }
        }
        return intMap
    }



    private fun computeDestination(source : Int, map : Map<Int,Int>) : Int{
        return if (map.containsKey(source)){
            map[source]!!
        } else {
            source
        }
    }
}