package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day19Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay19_sample1.txt""")
            .readLines()

        val instructions = parseInstructions(input)

        val machineParts = parseMachineParts(input)
    }

    private fun parseInstructions(input: List<String>) {
        val instructions = input.takeWhile { it.isNotEmpty() }
        for (instruction in instructions) {
            println(instruction)
            val ruleName = instruction.takeWhile { it != '{' }
            val ruleParts = instruction.dropWhile { it != '{' }.drop(1).dropLast(1).split(",")
            println("$ruleName ... $ruleParts")
        }
    }

    class Rule(val ruleComponents: List<RuleComponent>)

    abstract class RuleComponent

    class ComparingStep(val name: String, val ch: Char, val greaterThan: Boolean, val value: Int, val next: RuleComponent?) : RuleComponent() {
        fun apply(part: MachinePart): RuleComponent? {
            val param = when (ch) {
                'x' -> part.x
                'm' -> part.m
                'a' -> part.a
                's' -> part.s
                else -> { println("ERROR!"); part.x }
            }
            val result = if (greaterThan) {
                param > value
            } else {
                param < value
            }
            return if (result) {
                next
            } else {
                null
            }

        }
    }

    class Accept(): RuleComponent()

    class Reject(): RuleComponent()



    private fun parseMachineParts(input: List<String>): List<MachinePart> {
        val result = mutableListOf<MachinePart>()
        val machinePartDescriptions = input.dropWhile { it.isNotEmpty() }.drop(1)
        for (machinePartDescription in machinePartDescriptions) {
            println(machinePartDescription)
            val partValues = machinePartDescription.drop(1).dropLast(1).split(",")

            val partX = partValues[0]
            val valueX = partX.drop(2).toInt()

            val partM = partValues[1]
            val valueM = partM.drop(2).toInt()

            val partA = partValues[2]
            val valueA = partA.drop(2).toInt()

            val partS = partValues[3]
            val valueS = partS.drop(2).toInt()

            val total = MachinePart(valueX, valueM, valueA, valueS)
            result.add(total)
        }

        return result
    }

    data class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int)
}

