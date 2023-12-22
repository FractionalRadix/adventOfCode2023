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
        minX: Int, maxX: Int,
        minM: Int, maxM: Int,
        minA: Int, maxA: Int,
        minS: Int, maxS: Int
    ) {

        data class SetOfParts(val ch: Char, var min: Int, var max: Int)

        private var x: SetOfParts = SetOfParts('x', minX, maxX)
        private var m: SetOfParts = SetOfParts('m', minM, maxM)
        private var a: SetOfParts = SetOfParts('a', minA, maxA)
        private var s: SetOfParts = SetOfParts('s', minS, maxS)

        constructor(x: SetOfParts, m: SetOfParts, a: SetOfParts, s: SetOfParts) : this(
            x.min, x.max, m.min, m.max, a.min, a.max, s.min, s.max
        )

        fun applyComparingRule(rule: ComparingRule, parts: SetOfParts): List<Pair<SetOfMachines, Rule?>> {
            val result = mutableListOf<Pair<SetOfMachines, Rule?>>()

            //TODO!+
            if (rule.greaterThan) {
                // The rule is: if the value of a part is greater than the threshold, we move the indicated next rule (rule.next).
                // Otherwise, we move to the next rule in the current workflow (Rule? becomes null).

                // So: we split our set of machine parts in two.
                // First, there are the machine parts whose value is above the threshold. These will become (threshold, max, rule.next)
                // Second, there are the machine parts whose value is below or equal to the threshold. These will become (min, threshold, null) //TODO!~ Check that!

                // Let's assume we have the set of parts that is at least 1500 and at most 2500.
                // If the new maximum is below or equal to 1500, the new set is empty: all the machine parts that are simultaneously above and below/equal to 1500.
                // If the new minimum is above 2500, the new set is also empty: all the machine parts that are simultaneously above and below/equal to 2500.
                // If the new minimum is (for example) 1900, then we get two new sets:
                //  - the set of machine parts at [1500, 1900], which fails the rule, and the next rule must therefore be set to "null". (Filled in later as: next step in the workflow).
                //  - the set of machine parts at [1901, 2500], which passes the rule. Its next rule is set to "rule.next" .


                TODO()
            } else { // if (rule.lesserThan)
                TODO()
            }

            return result
        }

        fun applyComparingRule(rule: ComparingRule): List<Pair<SetOfMachines, Rule?>> {

            val result = mutableListOf<Pair<SetOfMachines, Rule?>>()

            when (rule.ch) {
                'x' -> result.addAll(applyComparingRule(rule, this.x))
                'm' -> result.addAll(applyComparingRule(rule, this.m))
                'a' -> result.addAll(applyComparingRule(rule, this.a))
                's' -> result.addAll(applyComparingRule(rule, this.s))
            }

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

