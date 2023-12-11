package com.cormontia.adventOfCode.year2023

import utils.lcm
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

        val nrOfStepsPart2 = solvePart2(network, instructions)
        println("Nr of steps to reach all Z-ending nodes from all A-ending notes simultaneously: $nrOfStepsPart2")

        // WRONG: 42733842121442 (too high).
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
    private fun solvePart2_naive(network: Map<String, Pair<String, String>>, instructions: String): Int {
        var positions = network.keys.filter { it.endsWith('A') }
        var count = 0

        while (!positions.all { it.endsWith('Z') }) {
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
            }
            positions = newPositions

            count++
        }

        return count
    }

    private fun solvePart2(network: Map<String, Pair<String, String>>, instructions: String): Long {
        val startPositions = network.keys.filter { it.endsWith('A') }

        val list = mutableListOf<Long>()
        for (startPos in startPositions) {
            val nrOfSteps = singleGhostPath(startPos, instructions, network)
            list.add(nrOfSteps)
        }

        val lcm = lcm(list.toLongArray())
        return lcm
    }

    private fun singleGhostPath(start: String, instructions: String, network: Map<String, Pair<String, String>>): Long {
        data class EndPosition(val count: Int, val pos: String, val instructionPointer: Int)
        val endPositions = mutableListOf<EndPosition>()

        println()
        var count = 0
        var pos = start
        do {
            val next = network[pos]
            val instructionPointer = count % instructions.length
            pos = if (instructions[instructionPointer] == 'L') {
                next!!.first
            } else /* instructions[actualIp] == 'R' */ {
                next!!.second
            }
            count++

            if (pos.endsWith('Z')) {
                if (endPositions.any { it.pos == pos && it.instructionPointer == instructionPointer }) {
                    break
                } else {
                    val endPos = EndPosition(count, pos, instructionPointer)
                    endPositions.add(endPos)
                }
            }
        } while (true)

        // Removing the steps before you got in a loop.
        // (Full disclosure: this was a hint from the subreddit, and only works due to how the input is structured).
        val beforeLoop = endPositions.minOf { it.count }

        return (count - beforeLoop).toLong()
    }
}