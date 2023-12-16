package com.cormontia.adventOfCode.year2023

import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day14Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay14.txt""")
            .readLines()

        val totalLoad = solvePart1(input)
        println("Total load: $totalLoad") // 108641
        val totalLoadAfterManyTurns = solvePart2(input) // Too low: 84239
    }

    private fun solvePart2(input: List<String>): Int {
        // Keep track of the maps.
        // The moment we see the same map twice, we have a cycle.

        val repetitions = 1000000000
        val mapHistory = mutableListOf<List<String>>()
        var afterCycle = input
        mapHistory.add(afterCycle)

        for (i in 1..repetitions) {
            afterCycle = cycle(afterCycle)

            val load = calculateLoad(afterCycle)
            println("Round $i: $load")

            val prev = mapHistory.indexOfFirst { equalMaps(it, afterCycle) }
            if (prev > -1) {
                println("Found previously at position: $prev")
                val offset = prev
                val cycle = mapHistory.size - offset
                println("Offset: $offset Cycle: $cycle")
                val remainder = (repetitions - offset) % cycle
                println("Should be the value at $offset + $remainder, i.e. at ${offset+remainder}")
                val oldLoad = calculateLoad(mapHistory[offset + remainder])

                println(oldLoad)

                //println("At: ${mapHistory[offset + remainder]}")


                return oldLoad
            }
            mapHistory.add(afterCycle)
        }

        return 0 //TODO!~
    }



    fun equalMaps(map1: List<String>, map2: List<String>): Boolean {
        if (map1.size != map2.size)
            return false
        for (i in map1.indices) {
            if (map1[i] != map2[i])
                return false
        }
        return true
    }



    private fun cycle(input: List<String>): List<String> {
        // Turn to make the rocks go north (up)
        val turn1 = transposeStringList(input)
        val turn1Shifted1 = moveRoundedRocksToLeft(turn1)
        val turn1Shifted2 = transposeStringList(turn1Shifted1)

        // Turn to make the rocks go west (left)
        val turn2Shifted = moveRoundedRocksToLeft(turn1Shifted2)

        // Turn to make the rocks go south (down)
        val turn3 = transposeStringList(turn2Shifted).map { it.reversed() }
        val turn3shifted1 = moveRoundedRocksToLeft(turn3)
        val turn3Shifted2 =
            transposeStringList(turn3shifted1.map { it.reversed() }) //TODO?~  transpose first and revert after?

        // Turn to make the rocks go east (right)
        val turn4 = moveRoundedRocksToLeft(turn3Shifted2.map { it.reversed() }).map { it.reversed() }
        return turn4
    }


    private fun solvePart1(input: List<String>): Int {
        val transposed = transposeStringList(input)
        val transposedAndShifted = moveRoundedRocksToLeft(transposed)
        val shiftedGrid = transposeStringList(transposedAndShifted)
        println(shiftedGrid)
        return calculateLoad(shiftedGrid)
    }

    private fun calculateLoad(shiftedGrid: List<String>): Int {
        var totalLoad = 0
        var n = shiftedGrid.size
        for (l in shiftedGrid) {
            val nrOfRoundedRocks = l.filter { it == 'O' }.length
            val load = n * nrOfRoundedRocks
            totalLoad += load
            n--
        }
        return totalLoad
    }

    private fun moveRoundedRocksToLeft(grid: List<String>): MutableList<String> {
        val result = mutableListOf<String>()
        for (line in grid) {
            val shifted = mutableListOf<String>()
            val split = splitByDashes(line)
            for (elt in split) {
                val nrOfRoundRocks = elt.filter { it == 'O' }.length
                val nrOfDots = elt.filter { it == '.' }.length
                val nrOfCubeShapedRocks = elt.filter { it == '#' }.length

                val newElt = "O".repeat(nrOfRoundRocks) + ".".repeat(nrOfDots) + "#".repeat(nrOfCubeShapedRocks)
                shifted.add(newElt)
            }
            val lineWithMovedRocks = shifted.joinToString("")
            result.add(lineWithMovedRocks)
        }
        return result
    }

    private fun splitByDashes(str: String): List<String> {
        val res = mutableListOf<String>()
        var hashing = false
        var cur = ""

        for (ch in str) {
            if (ch == '#') {
                if (!hashing) {
                    hashing = true
                    res.add(cur)
                    cur = "#"
                } else {
                    cur += ch
                }
            } else {
                if (hashing) {
                    hashing = false
                    res.add(cur)
                    cur = "$ch"
                } else {
                    cur += ch
                }
            }
        }
        res.add(cur)
        return res.filter { it.isNotBlank() }
    }
}