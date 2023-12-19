package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day19Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay19_sample1.txt""")
            .readLines()

        val instructions = parseInstructions(input)

        val machineParts = parseMachineParts(input)

        val acceptedParts = mutableListOf<MachinePart>()
        for (machinePart in machineParts) {

            val accepted = applyWorkflows(instructions, machinePart)
            if (accepted) {
                acceptedParts.add(machinePart)
            }
        }

        for (mp in acceptedParts) {
            println(mp)
        }

    }

    private fun applyWorkflows(
        instructions: List<Workflow>,
        machinePart: MachinePart,
    ): Boolean {
        println("Apply for $machinePart")
        var currentWorkflow = instructions.first { it.name == "in" }

        var idx = 0
        var step: Rule? = currentWorkflow.workflowActivities.first()

        while (true) {

            if (step is Accept)
                return true

            if (step is Reject)
                return false

            if (step is JustWorkflowName) {
                val name = step.name
                println("Moving to other workflow: ${step.name}")
                currentWorkflow = instructions.first { it.name == name }
                step = currentWorkflow.workflowActivities.first()
                idx = 0
            } else if (step is ComparingRule) {
                println("Comparing rule: $step")
                step = step.perform(machinePart)
                println("...step==$step")
                if (step == null) {
                    // Rule did not apply, move to next rule.
                    idx++
                    step = currentWorkflow.workflowActivities[idx]
                    println("...step is now $step")
                } else {
                    // else: Just move to the next iteration!
                    idx = 0
                }
            }
        }
    }

    private fun parseInstructions(input: List<String>): List<Workflow> {
        val workflows = mutableListOf<Workflow>()
        val instructions = input.takeWhile { it.isNotEmpty() }
        for (instruction in instructions) {
            println(instruction)
            val ruleName = instruction.takeWhile { it != '{' }


            val rules = instruction.dropWhile { it != '{' }.drop(1).dropLast(1).split(",")
            //TODO?~ Make this more idiomatic Kotlin. A map would be nice.
            val ruleList = mutableListOf<Rule>()
            for (rule in rules) {
                val newRule = parseRule(rule)
                ruleList.add(newRule)
            }
            val workflow = Workflow(ruleName, ruleList)
            workflows.add(workflow)
        }
        return workflows
    }


    private fun parseRule(rule: String): Rule {
        lateinit var newRule: Rule
        if (rule.contains(':')) {
            val char = rule[0]
            val greaterThan = (rule[1] == '>')
            val value = rule.drop(2).takeWhile { it != ':' }.toInt()
            val nextStr = rule.dropWhile { it != ':' }.drop(1)
            val next = when (nextStr) {
                "A" -> Accept()
                "R" -> Reject()
                else -> JustWorkflowName(nextStr)
            }
            newRule = ComparingRule(char, greaterThan, value, next)
        } else if (rule == "A") {
            newRule = Accept()
        } else if (rule == "R") {
            newRule = Reject()
        } else { // rule is just a string, a workflow name
            newRule = JustWorkflowName(rule)
        }
        println("  $newRule")
        return newRule
    }

    class Workflow(val name: String, val workflowActivities: List<Rule>)

    abstract class Rule

    class ComparingRule(val ch: Char, val greaterThan: Boolean, val value: Int, val next: Rule?) : Rule() {
        fun perform(part: MachinePart): Rule? {
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

        override fun toString(): String {
            return "$ch${if(greaterThan){'>'}else{'<'}}$value:$next"
        }
    }

    class Accept: Rule() {
        override fun toString() = "A"
    }

    class Reject: Rule() {
        override fun toString() = "R"
    }

    class JustWorkflowName(val name: String): Rule() {
        override fun toString() = name
    }



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

    data class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun rating() = x + m + a + s
    }
}

