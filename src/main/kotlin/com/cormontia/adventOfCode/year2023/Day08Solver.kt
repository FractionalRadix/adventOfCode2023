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
        val nrOfStepsPart2 = solvePart2(network, instructions)
        println("Nr of steps to reach all Z-ending nodes from all A-ending notes simultaneously: $nrOfStepsPart2")
    }

    private fun solvePart1(network: Map<String, Pair<String, String>>, instructions: String): Int {
        var pos = "AAA"
        var count = 0

        while (pos != "ZZZ") {
            val next = network[pos]
            val instructionPointer = count % instructions.length
            pos = if (instructions[instructionPointer] == 'L') {
                next!!.first
            } else /* instructions[actualIp] == 'R' */ {
                next!!.second
            }
            count++
        }

        return count
    }

    //TODO!~  This is a naive solution that takes a lot of time.
    // What we should be doing: determine for each individual starting point, at which points it encounters a node that ends with 'Z'.
    // For every starting point, there comes a point where this repeats.
    // So, for every starting point, we create a lazy list that repeats these values forever.
    // Then find the first point that all infinite lazy lists have in common.
    private fun solvePart2(network: Map<String, Pair<String, String>>, instructions: String): Int {
        var positions = network.keys.filter { it.endsWith('A') }
        var count = 0

        while (!positions.all { it.endsWith('Z') }) {
            //println("New round: $count")
            val newPositions = mutableListOf<String>()

            for (src in positions) {
                val next = network[src]
                val instructionPointer = count % instructions.length
                val pos = if (instructions[instructionPointer] == 'L') {
                    next!!.first
                } else /* instructions[actualIp] == 'R' */ {
                    next!!.second
                }
                newPositions.add(pos)
                //println("...$src -> $pos")
            }
            positions = newPositions

            count++
        }

        return count
    }
}