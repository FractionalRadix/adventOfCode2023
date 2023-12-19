package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

class Day19Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay19_sample1.txt""")
            .readLines()

        val instructions = parseInstructions(input)

        val machineParts = parseMachineParts(input)

        val sum = solvePart1(machineParts, instructions)
        println(sum)

    }

    private fun solvePart2_DFA(instructions: List<Workflow>): Long {
        // Approach: build a DFA from the instructions.
        // Find all paths that result in "Accept". Assuming they are distinct.
        // The transitions are the conditions. All we need to do now is apply all conditions.
        //  For example, "a < 2006", "x > 787", "a > 310" ...

        //TODO!+
        return 0L
    }

    private fun solvePart2_quantum(instructions: List<Workflow>): Long {
        // Walk through the rules using a "ManyMachine". Like a quantum thing, it is in many states :-)
        // The "ManyMachines" just keep track of the upper and lower bounds for a given state.

        val activeWorkflows = mutableListOf<Workflow>()
        activeWorkflows.add(instructions.first { it.name == "in" })

        //TODO!+ start applying the rules..
        val firstMachines = SetOfMachines(1,4000,1,4000,1,4000,1,4000)

        //TODO!~ This should eventually become a loop to perform the stuff...
        val rule = activeWorkflows.first().workflowActivities.first()
        if (rule is ComparingRule) {
            firstMachines.applyComparingRule(rule)

            //TODO!+
        }

        return 0
    }

    class SetOfMachines(
        var minX: Int, var maxX: Int,
        var minM: Int, var maxM: Int,
        var minA: Int, var maxA: Int,
        var minS: Int, var maxS: Int,
    ) {
        fun applyComparingRule(rule: ComparingRule): List<Pair<SetOfMachines, Rule?>> {

            val result = mutableListOf<Pair<SetOfMachines, Rule?>>()

            if (rule.ch == 'x') {
                if (rule.greaterThan) {
                    // x > value.
                    // We differentiate between two cases: where this applies, and where it doesn't.

                    // If the new minimum value for x is LOWER than its current value, keep the current (i.e. highest) minimum.
                    val newMinimum = max(rule.value + 1, minX)
                    // If the new minimum value for x exceeds its current maximum, then the set of machines represented is empty (empty intersection).
                    if (newMinimum <= maxX) {
                        val nextMachine1 = SetOfMachines(newMinimum, maxX, minM, maxM, minA, maxA, minS, maxS)
                        val nextRule1 = rule.next
                        result.add(Pair(nextMachine1, nextRule1))
                    }

                    //TODO!+ Add the same conditions.
                    val nextMachine2 = SetOfMachines(minX, rule.value, minM, maxM, minA, maxA, minS, maxS)
                    val nextRule2 =
                        null //TODO!+ The caller must keep in mind that this transfers the state to the next step in the CURRENT workflow!
                    result.add(Pair(nextMachine2, nextRule2))

                } else {
                    // x < value

                    //TODO!+ Add conditions
                    val nextMachine1 = SetOfMachines(minX, rule.value - 1, minM, maxM, minA, maxA, minS, maxS)
                    val nextRule1 = rule.next
                    result.add(Pair(nextMachine1, nextRule1))

                    //TODO!+ Add conditions
                    val nextMachine2 = SetOfMachines(rule.value, maxX, minM, maxM, minA, maxA, minS, maxS)
                    val nextRule2 =
                        null //TODO!+ The caller must keep in mind that this transfers the state to the next step in the CURRENT workflow!
                    result.add(Pair(nextMachine2, nextRule2))
                }
            }

            //TODO!+ The other letters

            return result
        }
    }

    private fun solvePart1(
        machineParts: List<MachinePart>,
        instructions: List<Workflow>,
    ): Int {
        return machineParts
            .filter { applyWorkflows(instructions, it) }
            .sumOf { it.rating() }
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

