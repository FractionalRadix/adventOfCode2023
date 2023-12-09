package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day08Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay08_sample1.txt""")
            .readLines()
            .filter { it.isNotBlank() }
        val instructions = input[0]
        val network = mutableMapOf<String, Pair<String,String>>()
        for (line in input.drop(1)) {
            val key = line.substring(0,3)
            val valueLeft = line.substring(7,10)
            val valueRight = line.substring(12,15)
            network[key] = Pair(valueLeft, valueRight)
        }


    }
}