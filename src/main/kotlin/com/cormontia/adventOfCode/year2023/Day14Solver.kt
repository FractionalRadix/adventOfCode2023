package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day14Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay14.txt""")
            .readLines()

        /*
        val test1 = "OO..###.O.#.."
        val test2 = "#..##OO.OO..."
        println(splitByDashes(test1))
        println(splitByDashes(test2))
        */

        val transposed = transposeStringList(input)
        val transposedAndShifted = mutableListOf<String>()
        var shiftedGrid = mutableListOf<String>()
        for (line in transposed) {
            val shifted = mutableListOf<String>()
            val split = splitByDashes(line)
            for (elt in split) {
                val nrOfRoundRocks = elt.filter { it == 'O' }.length
                val nrOfDots = elt.filter { it == '.' }.length
                val nrOfSquareRocks = elt.filter { it == '#' }.length

                val newElt = "O".repeat(nrOfRoundRocks) + ".".repeat(nrOfDots) + "#".repeat(nrOfSquareRocks)
                shifted.add(newElt)
            }
            val lineWithMovedRocks = shifted.joinToString("")
            transposedAndShifted.add(lineWithMovedRocks)
            //println(lineWithMovedRocks)


            /*
            //println(split)
            val newLine = split.joinToString("")
            shifted.add(newLine)
            println(shifted)

             */
        }

        shiftedGrid = transposeStringList(transposedAndShifted).toMutableList()
        for (l in shiftedGrid) {
            println(l)
        }

        var sum = 0
        var n = shiftedGrid.size
        for (l in shiftedGrid) {
            val nrOfRoundedRocks = l.filter { it == 'O' }.length
            val load = n * nrOfRoundedRocks
            sum += load
            n--
        }
        println("Total load: $sum")


        //val result1 = transposeStringList(shifted)
        //for (line in result1) {
        //    println(line)
        //}

        /*
        val map = buildGridMap(input)
        val minCol = 0L
        val maxCol = map.map { it.key.col }.max()
        val minRow = 0L
        val maxRow = map.map { it.key.row }.max()

        val map2 = mutableMapOf<Coor,Char>()
        for (col in LongRange(minCol, maxCol)) {

            //TODO!+
            // In this column, count all rounded rocks until the next '#'  or until the end of the column, whichever comes first.
            val column = map.filter { it.key.col == col }.toList().sortedBy { it.first.row }
            val roundRocks = column.takeWhile { it.second != '#' }

        }
         */

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

    private fun shiftRocks(str: String) {

    }


}