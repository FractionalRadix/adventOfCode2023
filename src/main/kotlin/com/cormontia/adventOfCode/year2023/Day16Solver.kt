package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day16Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay16_sample1.txt""")
            .readLines()

        for (line in input) {
            println(line)
        }

        var beams = mutableListOf<Beam>()
        beams.add(Beam(Coor(0L,0L), Direction.RIGHT, emptyList()))
        val inputMap = buildGridMap(input)
        val room = inputMap.mapValues { Tile(it.value, false) }

        //TODO!+ Check if beams run in circles. Keep a history for our beams.
        // Note that we might choose to keep a history of tiles, instead.
        while (beams.isNotEmpty()) {
            val nextGenerationBeams = mutableListOf<Beam>()

            nextGenerationBeams.clear()
            for (beam in beams) {
                val place = beam.position
                room[place]?.isEnergized = true
                when (beam.direction) {
                    Direction.DOWN -> nextGenerationBeams.addAll(moveDown(beam, room[place]!!.contents, room.keys))
                    Direction.LEFT -> nextGenerationBeams.addAll(moveLeft(beam, room[place]!!.contents, room.keys))
                    Direction.UP -> nextGenerationBeams.addAll(moveUp(beam, room[place]!!.contents, room.keys))
                    Direction.RIGHT -> nextGenerationBeams.addAll(moveRight(beam, room[place]!!.contents, room.keys))
                }
            }

            beams = nextGenerationBeams
            println("Nr of beams: ${beams.size}")
            println("Energized tiles: ${room.count { it.value.isEnergized }}")
            printEnergizedMap(room)
            println()
        }

        val energized = room.count { it.value.isEnergized }
        println("Nr of energized tiles: $energized")
    }

    private fun moveDown(beam: Beam, position: Char, availableTiles: Set<Coor>): MutableList<Beam> {
        val newSet = mutableListOf<Beam>()
        when(position) {
            '.' -> newSet.addAll(newBeamDown(beam, availableTiles))
            '/' -> newSet.addAll(newBeamLeft(beam, availableTiles))
            '\\' -> newSet.addAll(newBeamRight(beam, availableTiles))
            '|' -> newSet.addAll(newBeamDown(beam, availableTiles))
            '-' -> {
                newSet.addAll(newBeamLeft(beam, availableTiles))
                newSet.addAll(newBeamRight(beam, availableTiles))
            }
            else -> println("Unknown symbol! $position")
        }
        return newSet
    }

    private fun moveUp(beam: Beam, position: Char, availableTiles: Set<Coor>): MutableList<Beam> {
        val newSet = mutableListOf<Beam>()
        when(position) {
            '.' -> newSet.addAll(newBeamUp(beam, availableTiles))
            '/' -> newSet.addAll(newBeamRight(beam, availableTiles))
            '\\' -> newSet.addAll(newBeamLeft(beam, availableTiles))
            '|' -> newSet.addAll(newBeamUp(beam, availableTiles))
            '-' -> {
                newSet.addAll(newBeamLeft(beam, availableTiles))
                newSet.addAll(newBeamRight(beam, availableTiles))
            }
            else -> println("Unknown symbol! $position")
        }
        return newSet
    }

    private fun moveLeft(beam: Beam, position: Char, availableTiles: Set<Coor>): MutableList<Beam> {
        val newSet = mutableListOf<Beam>()
        when(position) {
            '.' -> newSet.addAll(newBeamLeft(beam, availableTiles))
            '/' -> newSet.addAll(newBeamDown(beam, availableTiles))
            '\\' -> newSet.addAll(newBeamUp(beam, availableTiles))
            '|' -> {
                newSet.addAll(newBeamUp(beam, availableTiles))
                newSet.addAll(newBeamDown(beam, availableTiles))
            }
            '-' -> newSet.addAll(newBeamLeft(beam, availableTiles))
            else -> println("Unknown symbol! $position")
        }
        return newSet
    }

    private fun moveRight(beam: Beam, position: Char, availableTiles: Set<Coor>): MutableList<Beam> {
        val newSet = mutableListOf<Beam>()
        when(position) {
            '.' -> newSet.addAll(newBeamRight(beam, availableTiles))
            '/' -> newSet.addAll(newBeamUp(beam, availableTiles))
            '\\' -> newSet.addAll(newBeamDown(beam, availableTiles))
            '|' -> {
                newSet.addAll(newBeamUp(beam, availableTiles))
                newSet.addAll(newBeamDown(beam, availableTiles))
            }
            '-' -> newSet.addAll(newBeamRight(beam, availableTiles))
            else -> println("Unknown symbol! $position")
        }
        return newSet
    }

    private fun newBeamDown(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row + 1, beam.position.col)
        if (availableTiles.contains(nextCoor)) {
            val newBeam = Beam(nextCoor, Direction.DOWN)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamUp(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row - 1, beam.position.col)
        if (availableTiles.contains(nextCoor)) {
            val newBeam = Beam(nextCoor, Direction.UP)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamLeft(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row, beam.position.col - 1)
        if (availableTiles.contains(nextCoor)) {
            val newBeam = Beam(nextCoor, Direction.LEFT)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamRight(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row, beam.position.col + 1)
        if (availableTiles.contains(nextCoor)) {
            val newBeam = Beam(nextCoor, Direction.RIGHT)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun printEnergizedMap(room: Map<Coor, Tile>) {
        val rows = room.keys.map { it.row }.toSet()
        for (row in rows) {
            println()
            val cols = room.keys.filter { it.row == row }.map { it.col }.toSet()
            for (col in cols) {
                val coor = Coor(row,col)
                if (room[coor]!!.isEnergized) {
                    print('#')
                } else {
                    print(room[coor]!!.contents)
                }
            }
        }
    }

    class Tile(val contents: Char, var isEnergized: Boolean)
    enum class Direction { RIGHT, DOWN, LEFT, UP }
    class Beam(var position: Coor, var direction: Direction, val history: List<Beam>)
}