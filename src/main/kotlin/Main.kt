import com.cormontia.adventOfCode.year2023.utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    //Day01Solver().solve() // 55447, 54706
    //Day02Solver().solve() // 2268, 63542
    //Day03Solver().solve() // 530495, 80253814
    //Day04Solver().solve() // 22674, 5747443
    solveDay05()
}

fun solveDay05() {
    val input = Path("""src/main/resources/inputFiles/AoCDay05.txt""")
        .readLines()

    val seedsStr = input[0].split(":")
    println(seedsStr)
    val seeds = parseListOfLongs(seedsStr[1])

    println(seeds)

    val transformers = mutableListOf<Transformer>()
    var transformer: Transformer? = null
    for (i in 1..<input.size) {
        if (input[i].isEmpty())
            continue
        if (input[i].endsWith("map:")) {
            if (transformer != null) {
                transformers.add(transformer)
            }
            transformer = Transformer()
        } else {
            val longs = parseListOfLongs(input[i])
            val range = TransformRange(longs[0], longs[1], longs[2])
            transformer?.ranges?.add(range)
        }
    }
    transformers.add(transformer!!)

    val soils = mutableListOf<Long>()
    for (seed in seeds) {
        var value = seed
        println("seed: $value")
        for (transformer in transformers) {
            value = transformer.transform(value)
            print(" -> $value ")
        }
        println(" -> $value")
        soils.add(value)
    }

    println("The lowest soil nr is ${soils.min()}")


    var seedRanges = mutableListOf<LongRange>()
}

data class TransformRange(
    val destination: Long,
    val source: Long,
    val range: Long,
)

class Transformer() {
    val ranges = mutableListOf<TransformRange>()

    fun transform(input: Long): Long {
        for (range in ranges) {
            if (input >= range.source && input < range.source + range.range) {
                val delta = input - range.source
                return range.destination + delta
            }
        }
        return input
    }

    fun print( ) {
        println()
        for (range in ranges) {
            println("${range.destination} ${range.source} ${range.range}")
        }
    }
}


