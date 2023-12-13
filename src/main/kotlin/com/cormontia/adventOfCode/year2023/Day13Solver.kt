package com.cormontia.adventOfCode.year2023

import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day13Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay13.txt""")
            .readLines()

        val maps = mutableListOf<Map>()
        var current = mutableListOf<String>()
        for (line in input) {
            if (line.isBlank()) {
                val map = Map(current)
                maps.add(map)
                current = mutableListOf<String>()
            } else {
                current.add(line)
            }
        }
        val map = Map(current)
        maps.add(map)

        var horizontalSum = 0
        var verticalSum = 0
        for (m in maps) {
            println()
            m.print()
            println("Horizontal:")
            val horizontalValues = m.findHorizontalMirror(m.list)
            horizontalSum += horizontalValues.sum()
            println("Vertical:")
            val verticalValues = m.findVerticalMirror(m.list)
            verticalSum += verticalValues.sum()
        }
        println("Horizontal sum: $horizontalSum")
        println("Vertical sum: $verticalSum")
        println("Result: ${100 * horizontalSum + verticalSum}")
    }

    class Map(val list: MutableList<String>) {
        fun print() {
            println("NEW MAP:")
            for (l in list) {
                println(l)
            }
        }

        fun findHorizontalMirror(list: List<String>): List<Int> {
            val result = mutableListOf<Int>()
            for (i in list.indices.drop(1)) {
                if (list[i] == list[i - 1]) {
                    if (confirmHorizontalMirroring(list,i-1, i)) {
                      println("Found: $i ${i+1}") // Note the off-by-one correction.
                        result.add(i)
                    }
                }
            }
            if (result.size > 1) {
                println("Warning! Multiple mirroring axes!")
            }
            return result
        }

        private fun confirmHorizontalMirroring(list: List<String>, i: Int, j: Int): Boolean {
            var d = 1
            while (i - d >= 0 && j + d < list.size) {
                val strI = list[i - d]
                val strJ = list[j + d]
                if (strI != strJ) {
                    return false
                }
                d++
            }
            return true
        }

        fun findVerticalMirror(list: List<String>): List<Int> {
            val transposed = transposeStringList(list)
            return findHorizontalMirror(transposed)
        }
    }
}