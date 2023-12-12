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
            val myLine = Line(parts[0], values.toMutableList())
            myLine.print()
            myLine.splitBlocks()
            println()
        }

    }

    class Line(private val str: String, private var values: MutableList<Long>) {
        private var blocks = mutableListOf<String>()

        fun print() {
            println("$str $values")
        }

        fun splitBlocks() {
            blocks = str.split(".").filter { it.isNotBlank() }.toMutableList()
            println(blocks)

            //NOTE. If the first block starts with a '#', or the last one ends with a '#', you know you've fixed that contiguous block.
            // Then there is only one way that block could fit, and you can remove that part.

            if (blocks[0].startsWith('#')) {
                val block = blocks[0].substring(values[0].toInt()) //TODO!+ Check for off-by-one error.
                if (blocks[0].isEmpty()) {
                    blocks.removeFirst()
                } else {
                    blocks[0] = block
                }
                values.removeFirst()
                println("New blocks: $blocks $values")
            }

            if (blocks.last().endsWith('#')) {
                val block = blocks.last().reversed().substring(values.last().toInt()).reversed()
                if (block.isEmpty()) {
                    blocks.removeLast()
                } else {
                    blocks[blocks.size - 1] = block

                }
                values.removeLast()
                println("New blocks: $blocks $values")
            }

        }
    }
}