package com.cormontia.adventOfCode.year2023

import utils.Direction
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day18Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay18_sample1.txt""")
            .readLines()

        val commands = mutableListOf<Command>()
        for (line in input) {
            val components = line.split(" ")

            val dirChar = components[0]
            val cmdDirection = when (dirChar[0]) {
                'U' -> Direction.UP
                'D' -> Direction.DOWN
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                else -> { println("Error! Unknown direction!"); Direction.UP }
            }
            val cmdLength = components[1].toInt()
            val hexStr = components[2].substring(2,8)
            //println(hexStr)
            val hex = hexStr.toInt(16)

            val cmd = Command(cmdDirection, cmdLength, hex)
            commands.add(cmd)
            //cmd.print()
        }

        val map = mutableMapOf<Coor, Char>()
        var x = 0L
        var y = 0L
        for (cmd in commands) {
            when (cmd.direction) {
                Direction.UP -> {
                    for (i in 0 ..< cmd.meters) {
                        val next = Coor(x, y + i)
                        map[next] = '#'
                    }
                    y += cmd.meters
                }
                Direction.DOWN -> {
                    for (i in 0 ..< cmd.meters) {
                        val next = Coor(x, y - i)
                        map[next] = '#'
                    }
                    y -= cmd.meters
                }
                Direction.RIGHT -> {
                    for (i in 0 ..< cmd.meters) {
                        val next = Coor(x + i, y)
                        map[next] = '#'
                    }
                    x += cmd.meters
                }
                Direction.LEFT -> {
                    for (i in 0 ..< cmd.meters) {
                        val next = Coor(x - i, y)
                        map[next] = '#'
                    }
                    x -= cmd.meters
                }
            }
        }

        println("Map:")
        printMap(map)

        println("Map after floodFill:")
        floodFill(map, Coor(1, 1))
        printMap(map)

        val size = countInside(map)
        println("The trench has size $size.")


    }

    private fun printMap(map: Map<Coor, Char>) {
        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        for (y in maxY downTo  minY ) {
            println()
            for (x in minX .. maxX) {
                if (map.containsKey(Coor(x,y))) {
                    print('#')
                } else {
                    print('.')
                }
            }
        }
    }

    private fun countInside(map: MutableMap<Coor, Char>): Int {
        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        for (y in maxY downTo  minY ) {
            println()
            for (x in minX .. maxX) {
                if (map.containsKey(Coor(x,y))) {
                    //print('#')
                } else {
                    //print('.')
                }
            }
        }

        return 0 //TODO!~
    }

    private fun floodFill(map: MutableMap<Coor, Char>, start: Coor) {
        if (map[start] == '#')
            return

        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        if (start.x < minX || start.x > maxX)
            return

        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y
        if (start.y < minY || start.y > maxY)
            return

        map[start] = '#'

        floodFill(map, Coor(start.x-1, start.y))
        floodFill(map, Coor(start.x+1, start.y))
        floodFill(map, Coor(start.x, start.y-1))
        floodFill(map, Coor(start.x, start.y+1))
    }

    class Command(val direction: Direction, val meters: Int, val color: Int) {
        fun print() {
            println("$direction $meters $color")
        }
    }
    data class Coor(val x: Long, val y: Long)
    class Cube(val pos: Coor, val color: Int)
}