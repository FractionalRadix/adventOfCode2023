package com.cormontia.adventOfCode.year2023

import utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day12Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay12_sample1.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        for (line in input) {
            val parts = line.split(" ")
            val values = parseListOfLongs(parts[1], ",")
            val myLine = Line(parts[0], values)
            myLine.print()
            myLine.splitBlocks()
            println()
        }

    }

    class Line(private val str: String, private val values: List<Long>) {
        private var blocks = mutableListOf<String>()

        fun print() {
            println("$str $values")
        }

        fun splitBlocks() {
            blocks = str.split(".").filter { it.isNotBlank() }.toMutableList()
            println(blocks)
        }
    }
}