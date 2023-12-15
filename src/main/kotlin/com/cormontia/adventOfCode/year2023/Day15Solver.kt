package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day15Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay15.txt""")
            .readLines()

        //println(hash("HASH"))
        var sum = 0
        for (line in input) {
            val subLines = line.split(",")
            for (subLine in subLines) {
                //println(hash(subLine))
                sum += hash(subLine)
            }
        }
        println("The sum of hashes is $sum.")
    }

    fun hash(str: String): Int {
        var currentValue = 0
        for (ch in str) {
            //println("Char: $ch")
            val asciiValue = ch.toByte()
            //println("Ascci value: $asciiValue")
            currentValue += asciiValue
            //println("Value after adding: $currentValue")
            currentValue *= 17
            //println("Value after multiplication: $currentValue")
            currentValue %= 256
            //println("Value after remainder: $currentValue")
        }
        return currentValue
    }
}