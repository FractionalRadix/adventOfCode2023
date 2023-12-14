package com.cormontia.adventOfCode.year2023

import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day14Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay14_sample1.txt""")
            .readLines()

        val totalLoad = solvePart1(input)
        println("Total load: $totalLoad") // 108641
        val totalLoadAfterManyTurns = solvePart2(input)
    }

    fun solvePart2(input: List<String>) {
        val turn1 = transposeStringList(input)
        val turn1Shifted1 = moveRoundedRocksToLeft(turn1)
        val turn1Shifted2 = transposeStringList(turn1Shifted1)

        println(turn1Shifted2)
        val turn2 = turn1Shifted2.map { it.reversed() }
        println(turn2)
        //TODO!+ Two cycles, three cycles
        // Two cycles should be a matter of reversing the list...
        // Three should be reversing and transposing...


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