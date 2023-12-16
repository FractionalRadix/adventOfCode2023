package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day15Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay15.txt""")
            .readLines()

        //println(hash("HASH"))
        //var sum = solvePart1(input)
        //println("The sum of hashes is $sum.")
        val sum2 = solvePart2_alt(input)
        println("The total focal strength is $sum2") // 466110 : Too high
    }

    private fun solvePart2_alt(lines: List<String>): Int {
        val boxes = fillBoxes2(lines)
        println(boxes)

        var sum = 0
        for (box in boxes) {
            print("Nr of lenses: ${box.value.lenses.size}")
            val boxValue = 1 + box.key
            for (lensPos in box.value.lenses.indices) {
                val slotNr = 1 + lensPos
                val focal = box.value.lenses[lensPos].focal
                val product = boxValue * slotNr * focal
                println("$boxValue * slotNr * focal == $product")
                sum += product
            }
        }
        println(sum)
        return sum
    }

    class Lens(val label: String, var focal: Int) {

    }

    class Box(val lenses: MutableList<Lens>) {

        fun print() {
            for (lens in lenses) {
                print("[${lens.label} ${lens.focal}] ")
            }
            println()
        }
    }

    private fun solvePart2(input: List<String>): Int {
        var sum = 0

        val regex = Regex("\\[(.*?)\\]")

        val boxes = fillBoxes(input)
        for (boxNr in boxes.keys) {
            val lenses = boxes[boxNr] ?: continue
            println(lenses)

            val allMatches = regex.findAll(lenses)
            var boxSum = 0
            var slotNr = 1
            for (match in allMatches) {
                val capture = match.value
                //println("  capture: $capture")
                val contents = capture.split(" ")
                val label = contents[0].drop(1)
                val focalLength = contents[1].take(1).toInt()
                //println("...label=$label strength=$focalLength")

                val lensStrength = (boxNr + 1) * slotNr * focalLength
                println("Strength: ${boxNr+1} * $slotNr * $focalLength = $lensStrength")

                boxSum += lensStrength
                slotNr++
            }
            println("...Box $boxNr: $boxSum")
            sum += boxSum
        }

        return sum
    }
    private fun fillBoxes2(input: List<String>): Map<Int, Box> {
        val boxes = mutableMapOf<Int,Box>()
        for (line in input) {
            val subLines = line.split(",")
            for (subLine in subLines) {
                println("Instruction: $subLine")
                val splitSubLine = subLine.split("-", "=")

                val label = splitSubLine[0]
                val boxNr = hash(label)
                var lenses = mutableListOf<Lens>()
                if (boxes.containsKey(boxNr)) {
                    lenses = boxes[boxNr]!!.lenses
                }

                if (subLine.contains("=")) {
                    // Parse subLine, add/replace lens
                    val components = subLine.split("=")

                    val labelOfReplaceable = components[0]
                    println("...label of lens to add/replace: $labelOfReplaceable")
                    val focal = components[1].toInt()
                    println("...focal length: $focal")

                    val filtered = lenses.filter { it.label == labelOfReplaceable }
                    if (filtered.isEmpty()) {
                        lenses.addLast(Lens(labelOfReplaceable, focal))
                        println("Added lens. New list size: ${lenses.size}")
                    } else {
                        val newList = lenses.takeWhile { it.label != labelOfReplaceable }.toMutableList()
                        newList.add(Lens(labelOfReplaceable, focal))
                        val lastHalf = lenses.dropWhile {  it.label != labelOfReplaceable  }.drop(1)
                        for (elt in lastHalf) {
                            newList.add(elt)
                        }
                        lenses = newList
                        println("Replaced lens. New list size: ${lenses.size}")
                    }
                    boxes[boxNr] = Box(lenses)
                    println("boxes[$boxNr] contains Box ${boxes[boxNr]!!.print()}")

                } else {
                    println("subLine: $subLine" )
                    val labelOfRemovableLens = subLine.dropLast(1)
                    println(labelOfRemovableLens)
                    lenses.removeIf { it.label == labelOfRemovableLens }
                    boxes[boxNr] = Box(lenses)
                }


            }
        }

        return boxes
    }

    private fun fillBoxes(input: List<String>): Map<Int, String> {
        val boxes = mutableMapOf<Int, String>()
        for (line in input) {
            val subLines = line.split(",")
            for (subLine in subLines) {
                println("subLine: $subLine")
                val splitSubLine = subLine.split("-", "=")

                val label = splitSubLine[0]
                val boxNr = hash(label)
                var lenses = ""
                if (boxes.containsKey(boxNr)) {
                    lenses = boxes[boxNr]!!
                }
                println("Box $boxNr ($label) contains lenses: $lenses")

                if (subLine.indexOf('=') > 0) {
                    // Add or replace a lens
                    val strength = splitSubLine[1].toInt()
                    //println("Box $boxNr ($label) gets lens with strength $strength")

                    val idx = lenses.indexOf("[$label")
                    if (idx > 0) {
                        println("Substring: ${lenses.substring(idx)}")
                        val positionOfSpace = lenses.indexOf(' ', idx)
                        println("Current strength: ${lenses[positionOfSpace + 1]}")
                        val beforePart = lenses.take(positionOfSpace)
                        val afterPart = lenses.substring(positionOfSpace + 2)
                        println(" before/after: $beforePart/$afterPart")

                        //println("newValue:{$strength}")
                        val newString = "$beforePart $strength$afterPart"
                        println("NEW string for box $boxNr: $newString")
                        //boxes[boxNr] = newString
                        lenses = newString
                    } else {
                        lenses += "[${label} ${strength}] "
                    }
                    boxes[boxNr] = lenses
                    println("Box $boxNr contains $lenses")

                } else { // "-"
                    // Remove a lens.
                    val idx = lenses.indexOf("[$label")
                    println("idx: $idx")
                    if (idx >= 0) {
                        val endIdx = lenses.indexOf("]", idx) + 1
                        println("Substring: ${lenses.substring(idx, endIdx)}")
                        val firstHalf = lenses.substring(0, idx)
                        val secondHalf = lenses.substring(endIdx)
                        val newString = firstHalf + secondHalf
                        println("New value for $lenses: $newString")
                        //boxes[boxNr] = newString
                        boxes.remove(boxNr)
                        boxes[boxNr] = newString
                    }
                }
            }
        }

        return boxes
    }

    private fun solvePart1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val subLines = line.split(",")
            for (subLine in subLines) {
                //println(hash(subLine))
                sum += hash(subLine)
            }
        }
        return sum
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