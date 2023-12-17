package com.cormontia.adventOfCode.year2023

import utils.Direction
import utils.Coor
import utils.buildGridMap
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day16Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay16.txt""")
            .readLines()

        val inputMap = buildGridMap(input)
        val room = inputMap.mapValues { Tile(it.value, false) }

        val energized = solvePart1(room)
        println("Nr of energized tiles: $energized")

        // PArt 2: 11265 is too high.
        val maxEnergized = solvePart2(room)
        println("The configuration with the maximum energized tiles, yields $maxEnergized .")
    }

    private fun solvePart1(room: Map<Coor, Tile>): Int {
        val energized = energize(room, Coor(0L, 0L), Direction.RIGHT)
        return energized
    }

    private fun solvePart2(room: Map<Coor, Tile>): Map.Entry<Pair<Coor, Direction>, Int> {
        val configurations = mutableMapOf<Pair<Coor, Direction>, Int>()

        val rows = room.keys.map { it.row }.toSet()
        val minRow = rows.min()
        val maxRow = rows.max()

        val cols = room.keys.map { it.col }.toSet()
        val minCol = cols.min()
        val maxCol = cols.max()

        for (row in rows) {
            println("Calculating for row #$row.")

            val e1 = energize(room, Coor(row, minCol), Direction.RIGHT)
            configurations[Pair(Coor(row, minCol), Direction.RIGHT)] = e1

            val e2 = energize(room, Coor(row, maxCol), Direction.LEFT)
            configurations[Pair(Coor(row, maxCol), Direction.LEFT)] = e2
        }

        for (col in cols) {
            println("Calculating for col #$col.")

            val e3 = energize(room, Coor(minRow, col), Direction.DOWN)
            configurations[Pair(Coor(minRow, col), Direction.DOWN)] = e3

            val e4 = energize(room, Coor(maxRow, col), Direction.UP)
            configurations[Pair(Coor(maxRow, col), Direction.UP)] = e4
        }

        val maxEnergized = configurations.maxBy { it.value }
        return maxEnergized
    }

    private fun energize(room: Map<Coor, Tile>, startPosition: Coor, startDirection: Direction): Int {

        for (tile in room) {
            tile.value.isEnergized = false
        }

        //NOTE. We are keeping a history of beams.
        // But it might be more efficient to keep a history of tiles.
        var beams = mutableListOf<Beam>()
        beams.add(Beam(startPosition, startDirection, mutableListOf()))

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
            //println("Nr of beams: ${beams.size}")
            //println("Energized tiles: ${room.count { it.value.isEnergized }}")
        }

        //printEnergizedMap(room)
        val energized = room.count { it.value.isEnergized }
        return energized
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
        if (availableTiles.contains(nextCoor) && !beam.contains(nextCoor, Direction.DOWN)) {
            //TODO?~ Put the "extend history" in a constructor.
            val history = beam.history
            val nextPair = Pair(nextCoor, Direction.DOWN)
            history.addLast(nextPair) //TODO?~  Might also put it in front... the purpose is to see IF the beam visited a tile at a specific direction, not WHEN.
            val newBeam = Beam(nextCoor, Direction.DOWN, history)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamUp(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row - 1, beam.position.col)
        if (availableTiles.contains(nextCoor) && !beam.contains(nextCoor, Direction.UP)) {
            val history = beam.history
            val nextPair = Pair(nextCoor, Direction.UP)
            history.addLast(nextPair)
            val newBeam = Beam(nextCoor, Direction.UP, history)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamLeft(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row, beam.position.col - 1)
        if (availableTiles.contains(nextCoor) && !beam.contains(nextCoor, Direction.LEFT)) {
            val history = beam.history
            val nextPair = Pair(nextCoor, Direction.LEFT)
            history.addLast(nextPair)
            val newBeam = Beam(nextCoor, Direction.LEFT, history)
            return listOf(newBeam)
        }
        return emptyList()
    }

    private fun newBeamRight(beam: Beam, availableTiles: Set<Coor>): List<Beam> {
        val nextCoor = Coor(beam.position.row, beam.position.col + 1)
        if (availableTiles.contains(nextCoor) && !beam.contains(nextCoor, Direction.RIGHT)) {
            val history = beam.history
            val nextPair = Pair(nextCoor, Direction.RIGHT)
            history.addLast(nextPair)
            val newBeam = Beam(nextCoor, Direction.RIGHT, history)
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
        println()
    }

    class Tile(val contents: Char, var isEnergized: Boolean)

    class Beam(var position: Coor, var direction: Direction, val history: MutableList<Pair<Coor, Direction>>) {
        fun contains(coor: Coor, direction: Direction) = history.any { it.first == coor && it.second == direction }
    }
}