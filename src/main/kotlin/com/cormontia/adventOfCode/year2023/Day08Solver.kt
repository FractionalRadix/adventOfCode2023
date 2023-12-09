package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day08Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay08.txt""")
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

        val nrOfSteps = solvePart1(network, instructions)
        println("Nr of steps to reach ZZZ: $nrOfSteps")
    }

    fun solvePart1(network: Map<String, Pair<String, String>>, instructions: String): Int {
        var ip = 0 // instruction pointer
        var pos = "AAA"
        var count = 0

        while (pos != "ZZZ") {
            val next = network[pos]
            val actualIp = ip % instructions.length
            if (instructions[actualIp] == 'L') {
                pos = next!!.first
            } else /* instructions[actualIp] == 'R' */ {
                pos = next!!.second
            }
            println("New position: $pos")
            ip++
            count++
        }

        return count
    }
}