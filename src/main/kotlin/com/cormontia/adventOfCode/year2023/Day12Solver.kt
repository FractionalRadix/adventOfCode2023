package com.cormontia.adventOfCode.year2023

import utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day12Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay12_sample1.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        //NOTE!
        // This input might show we have an error in our algorithm:
        // #?.???????#????#???. [1, 1, 12]
        // [#?, ???????#????#???]
        // blocks==[???????#????#???] values==[12]

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

            //TODO!~  Instead of repeating an arbitrary number of times, repeat until nothing changes anymore.
            for (i in 0..3) {
                if (blocks.isEmpty() || values.isEmpty())
                    continue //TODO!+ This means there was only ONE way to put the stuff in.

                val (blocks1, values1) = removeFixedHead(blocks, values)
                this.blocks = blocks1
                this.values = values1.toMutableList()

                if (blocks.isEmpty() || values.isEmpty())
                    continue //TODO!+ This means there was only ONE way to put the stuff in.

                val (blocks2, values2) = removeFixedHead(
                    blocks.reversed().toMutableList(),
                    values.reversed().toMutableList()
                )
                this.blocks = blocks2.reversed().toMutableList()
                this.values = values2.reversed().toMutableList()

                if (blocks.isEmpty() || values.isEmpty())
                    continue //TODO!+ This means there was only ONE way to put the stuff in.

                val (blocks3, values3) = removeHeadWithExactLength(blocks, values)
                this.blocks = blocks3
                this.values = values3.toMutableList()

                if (blocks.isEmpty() || values.isEmpty())
                    continue //TODO!+ This means there was only ONE way to put the stuff in.

                val (blocks4, values4) = removeHeadWithExactLength(
                    blocks.reversed().toMutableList(),
                    values.reversed().toMutableList()
                )
                this.blocks = blocks4.reversed().toMutableList()
                this.values = values4.reversed().toMutableList()
            }
            println("blocks==$blocks values==$values")

        }

        /** Rule #1.
         * If a line starts with '#', then the first contiguous chain is found, and should be removed.
         * Note that we can apply the same rule for the last contiguous block: simply reverse the input, do hhe calculation, and revert the output back.
         * @param blocks The list of blocks and question marks.
         * @param values The lengths of contiguous blocks.
         * @return A new pair of blocks and values, where the first contiguous chain is removed.
         */
        private fun removeFixedHead(blocks: MutableList<String>, values: MutableList<Long>): Pair<MutableList<String>, List<Long>> {
            if (blocks[0].startsWith('#')) {
                val block = blocks[0].substring(values[0].toInt())
                if (block.isEmpty()) {
                    blocks.removeFirst()
                } else {
                    blocks[0] = block
                }
                values.removeFirst()
                //println("New blocks: $blocks $values")
            }
            return Pair(blocks, values)
        }

        /**
         * Rule #2.
         * If the first block has exactly the same length as the first value, remove the first block.
         * Again, this rule can be applied to the end by reversing the input, applying this function, then reverting the output back.
         * @param blocks The list of blocks and question marks.
         * @param values The lengths of contiguous blocks.
         * @return A new pair of blocks and values, where the first contiguous chain is removed.
         */
        private fun removeHeadWithExactLength(blocks: MutableList<String>, values: MutableList<Long>): Pair<MutableList<String>, List<Long>> {
            if (blocks[0].length.toLong() == values[0]) {
                blocks.removeFirst()
                values.removeFirst()
            }
            return Pair(blocks, values)
        }
    }
}