package com.cormontia.adventOfCode.year2023

import utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day24Solver {

    // First start reading assignment: 08.45 (few minutes earlier?)


    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay24_sample1.txt""")
            .readLines()

        val hailstones = input.map {
            val parts = it.split("@")
            println("< ${parts[0]}>  <${parts[1]}>")
            val part0trimmed = parts[0].filter { ch -> !ch.isWhitespace() }
            val position = parseListOfLongs(part0trimmed, ",")
            val part1trimmed = parts[1].filter { ch -> !ch.isWhitespace() }
            val velocity = parseListOfLongs(part1trimmed, ",")
            Hailstone(position[0], position[1], position[2], velocity[0], velocity[1], velocity[2])
        }

        //hailstones.forEach {
        //    print(it)
        //}

        val crossings = solvePart1(hailstones, 7,27, 7, 27)

    }

    fun solvePart1(hailstones: List<Hailstone>, minX: Long, maxX: Long, minY: Long, maxY: Long) {
        for (i1 in hailstones.indices) {
            for (i2 in i1 + 1 ..< hailstones.size) {

                println("Comparing: $i1, $i2.")

                val h1 = hailstones[i1]
                val h2 = hailstones[i2]

                // Determine if the lines cross. Call the solution point S.
                // So now we need to solve S1 = S2 in
                //
                //  S1.x(t) = p1.x + t * v1.x
                //  S1.y(t) = p1.y + t * v1.y
                //  S2.x(s) = p2.x + s * v2.x
                //  S2.y(s) = p2.y + s * v2.y
                //
                // Note that we DON'T care about the values for s and t!
                // We only care IF the lines cross, not if the two items described reach that crossing point at the same time!
                //
                // First let's re-write the parameterized lines to y = a * x + b notation.
                // For the first hailstone:
                //
                //   a = dy / dx = (v1.y / v1.x)
                //
                //   S1.y(t) = a * S1.x(t) + b
                //   S1.y(t) - a * S1.x(t) = b, for any given t.
                //   b = S1.y(t) - a * S1.x(t)
                //     = p1.y + t * v1.y - a * (p1.x + t * v1.x)    ...choosing t=0...
                //     = p1.y + 0 * v1.y - a * (p1.x + 0 * v1.x)
                //     = p1.y  - a * (p1.x)
                //   Therefore:
                //       a1 = v1.y / v1.x
                //       b1 = p1.y - a1 * p1.x
                //   Analogous for a2, b2.
                //  Now all that's left is to solve for a1 * x + b1 = a2 * x + b2
                //    a1 * x + b1 = a2 * x + b2
                //    a1 * x - a2 * x + b1 = b2
                //    a1 * x - a2 * x  = b2 - b1
                //    x * (a1 - a2) = b2 - b1
                //    x = (b2 - b1) / (a2 - a1)
                //  Given that, if necessary we can still determine the corresponding value of y.


                val a1 = h1.vy.toDouble() / h1.vx.toDouble()
                val b1 = h1.py - a1 * h1.px

                val a2 = h2.vy.toDouble() / h2.vx.toDouble()
                val b2 = h2.py - a2 * h2.px

                println("$a1 * x + $b1   $a2 * x + $b2")

                val x = (b2 - b1) / (a2 - a1)
                //if (x >= minX && x <= maxX) {
                    val y = a1 * x + b1
                    //if (y >= minY && y <= maxY) {
                        println("Crossing within boundaries: ($x, $y).")
                    //}
                //}


            }
        }

    }

    data class Hailstone(
        val px: Long, val py: Long, val pz: Long,
        val vx: Long, val vy: Long, val vz: Long,
    )
}