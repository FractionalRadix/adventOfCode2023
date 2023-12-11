package com.cormontia.adventOfCode.year2023

import utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day05Solver {

    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay05_sample1.txt""")
            .readLines()

        val seedsStr = input[0].split(":")
        println(seedsStr)
        val seeds = parseListOfLongs(seedsStr[1])

        println(seeds)

        val transformers = parseTransformationFunctions(input)

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


        var sourceRanges = mutableListOf<LongRange>()
        for (i in seeds.indices step 2) {
            sourceRanges.add(LongRange(seeds[i], seeds[i] + seeds[i+1] - 1))
        }
        println(sourceRanges)

        // First, the naive solution:
        val finalMapping = mutableMapOf<Long,Long>()
        for (sourceRange in sourceRanges) {
            for (source in sourceRange) {

                var tmp = source
                for (mapper in transformers) {
                    tmp = mapper.transform(tmp)
                }
                finalMapping[source] = tmp

            }
        }
        val answer = finalMapping.map { it.value }.min()
        println("Lowest location number: $answer")


    }

    /**
     * Given a range, and a list of non-overlapping ranges, split the range according to these non-overlapping ranges.
     * For example, given [12..22] and the list [10..12], [15..17], [24..29]:
     * We'd get [12..12], [13..14], [15..17], [18..22].
     */
    private fun split(source: LongRange, ranges: List<LongRange>): List<LongRange> {
        TODO()
    }

    /**
     * Given a number and a set of non-overlapping ranges, determine if the number is inside any of these ranges.
     * If so, return the applicable range; otherwise return <code>null</code>.
     * @param number The number for which we want to know if it is in any of the ranges.
     * @param ranges A list of non-overlapping ranges.
     * @return The range that the number is in, if any; <code>null</code> otherwise.
     */
    private fun insideRange(number: Long, ranges: List<LongRange>): LongRange? = ranges.firstOrNull { it.contains(number) }


    private fun parseTransformationFunctions(input: List<String>): MutableList<Transformer> {
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
        return transformers
    }

    data class TransformRange(
        val destination: Long,
        val source: Long,
        val range: Long,
    )

    /**
     * A set of mappings, from seed-to-soil, or soil-to-fertilizer, or fertilizer-to-water, etc.
     */
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



}