package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

// First opened page / read instructions: 10.01 AM

class Day25Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay25_sample1.txt""")
            .readLines()

        val graph = parseGraph(input)

        graph.print()

    }

    private fun parseGraph(input: List<String>): Graph {
        val graph = Graph()
        for (line in input) {
            val parts = line.split(":")
            val leftNode = parts[0]
            val rightNodes = parts[1].split(" ").filter { it.isNotBlank() }

            for (rightNode in rightNodes) {
                graph.addConnection(leftNode, rightNode)
            }
        }
        return graph
    }
}

/**
 * An undirected graph, where tne vertices are identified by strings.
 */
class Graph {
    val map = mutableMapOf<String, MutableList<String>>()

    fun addConnection(v1: String, v2: String) {
        addDirectionalConnection(v1, v2)
        addDirectionalConnection(v2, v1)
    }

    private fun addDirectionalConnection(v1: String, v2: String) {
        if (map.containsKey(v1)) {
            val list = map[v1]
            list!!.addLast(v2)
            map[v1] = list
        } else {
            map[v1] = mutableListOf(v2)
        }
    }

    fun removeConnection(v1: String, v2: String) {
        if (map.containsKey(v1)) {
            val list = map[v1]
            list!!.remove(v2)
            map[v1] = list
        }
        if (map.containsKey(v2)) {
            val list = map[v2]
            list!!.remove(v1)
            map[v2] = list
        }
    }

    fun print() {
        for (source in map.keys) {
            println()
            print("$source:")
            val destinations = map[source]
            destinations?.forEach {
                print(" $it")
            }
        }
    }
}