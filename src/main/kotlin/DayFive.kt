import java.io.File

class DayFive (file : File) {
    val baseFile = file

    fun computePartOne(){
        val content = baseFile.readText().split("\n\r")
        val seeds = content[0].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val seedToSoilLines = content[1].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val soilToFertilizerLines = content[2].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val fertilizerToWaterLines = content[3].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val waterToLightLines = content[4].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val lightToTemperatureLines = content[5].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val temperatureToHumidityLines = content[6].split(":")[1].split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
        val humidityToLocationLines = content[7].split(":")[1].split("\r").filter { it.isNotBlank() }


        val humidityMap = humidityToLocationLines.map { toTransformer(it) }



    }

    data class Transformer(val sourceStart : Int, val destinationStart : Int, val range : Int)

    private fun toTransformer(string: String) : Transformer{
        val elements = string.split("\\s+".toRegex()).filter{it.isNotEmpty()}.map { it.toInt() }
        return Transformer(sourceStart = elements[1], destinationStart = elements[0], range = elements[2])
    }
}