package com.cormontia.adventOfCode.year2023

import utils.Direction
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

class Day18Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay18_sample1.txt""")
            .readLines()

        solvePart1(input)
        solvePart2(input)
    }

    private fun solvePart2(input: List<String>) {
        val commands = buildCommandListPart2(input)

        val map = buildMapFromInstructions(commands)

        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        val mapSize = abs(maxX - minX) * abs(maxY - minY)
        println("Map size: $mapSize.") // 1407374123584  for the sample input.
        // 154878206609891 for the real input.

        var sum = 0L
        for (y in minY .. maxY) {
            if (abs(y) % 500L == 0L) {
                println("Row: $y")
            }
            //val inside = countInsideInRow(map, y, minX - 1, maxX + 1)
            val inside = countInsideInRow2(map, y)
            sum += inside
        }
        println("Total nr of inside parts: $sum")
    }


    enum class Side { LeftEdge, Inside, RightEdge, Outside }

    private fun countInsideInRow2(map: Map<Coor, Char>, y: Long): Long {
        val row = map.filter { it.key.y == y }.map { it.key.x }.sortedBy { it }.asSequence().iterator()

        if (!row.hasNext())
            return 0

        val prev = row.next()
        var side = Side.LeftEdge

        var insides = 0L

        while (row.hasNext()) {
            if (row.next() > prev + 1) {
                // We encountered a gap!
                if (side == Side.LeftEdge) {
                    side = Side.Inside
                    insides++
                } else if (side == Side.Inside) {
                    side = Side.RightEdge
                    insides++
                } else if (side == Side.RightEdge) {
                    side = Side.Outside
                } else if (side == Side.Outside) {
                    side = Side.LeftEdge
                    insides++
                }
            }
            //print("count: $insides")
        }

        return insides
    }

    private fun countInsideInRow(map: Map<Coor, Char>, y: Long, minX: Long, maxX: Long): Long {
        val row = map.filter { it.key.y == y }
        // We bluntly assume that the first '#' in a row marks an edge; that any '.' before it is outside the trench.
        // We also bluntly assume that "##" will never be an "outcropping", that any group of consecutive '#' marks is a border.
        //val xCoordinates = row.filter { it.value == '#' }.map { it.key.x }
        println("Start sorting.")
        val xCoordinates = row.map { it.key.x }.sortedBy { it } // it.value == '#'  is implicit due to the way we represent the data.
        println("Sorted.")

        var sum = 0L

        var x0 : Long? = minX
        while (x0!! < maxX) { //TODO?~  Other criterion?!

            //TODO?~ Since these are sorted, shouldn't there be a fast way to find the first NON-consecutive one?

            //println("x0==$x0")
            x0 = xCoordinates.firstOrNull { it > x0!! }
            //println("x0==$x0")
            if (x0 == null)
                break
            val counter1 = numberOfConsecutiveBangs(xCoordinates, x0)
            //println("counter1==$counter1")
            // At x0 we have "counter" consecutive bangs, x0 itself included.

            val x1 = xCoordinates.firstOrNull { it > (x0!! + counter1) }
            //println("x1==$x1")
            if (x1 == null)
                break
            val counter2 = numberOfConsecutiveBangs(xCoordinates, x1)
            // At x1 we have "counter" consecutive bangs, x1+counter itself included.

            val x2 = x1 + counter2
            //println("Nr in between: ${x2 - x1}")

            sum += (x2 - x1)

            x0 = x1 + counter2 + 1
        }

        return sum
    }

    private fun numberOfConsecutiveBangs(xCoordinates: List<Long>, x0: Long): Int {
        var counter = 0
        //println("Starting to count...")
        while (xCoordinates.contains(x0 + counter)) {
            counter++
        }
        //println("...counter: $counter")
        return counter
    }

    private fun solvePart1(input: List<String>) {
        val commands = buildCommandListPart1(input)

        val map = buildMapFromInstructions(commands)

        println("Map:")
        printMap(map)

        println("Map after floodFill:")

        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        val mapSize = abs(maxX - minX) * abs(maxY - minY)
        println("Map size: $mapSize.") // 141240
        //TODO!~ Find a generic way to find the first "inside" tile.
        val startY = minY + 1
        val startX = map.filter { it.key.y == startY }.filter { it.value == '#' }.minBy { it.key.x }.key.x + 1
        val start = Coor(startX, minY + 1)

        floodFill(map, start)
        printMap(map)

        val size = map.count { it.value == '#' }
        println("The trench has size $size.")
    }

    private fun buildCommandListPart1(input: List<String>): MutableList<Command> {
        val commands = mutableListOf<Command>()
        for (line in input) {
            val components = line.split(" ")

            val dirChar = components[0]
            val cmdDirection = when (dirChar[0]) {
                'U' -> Direction.UP
                'D' -> Direction.DOWN
                'L' -> Direction.LEFT
                'R' -> Direction.RIGHT
                else -> {
                    println("Error! Unknown direction!"); Direction.UP
                }
            }
            val cmdLength = components[1].toInt()
            val hexStr = components[2].substring(2, 8)
            //println(hexStr)
            val hex = hexStr.toInt(16)

            val cmd = Command(cmdDirection, cmdLength, hex)
            commands.add(cmd)
        }
        return commands
    }

    private fun buildCommandListPart2(input: List<String>): MutableList<Command> {
        val commands = mutableListOf<Command>()
        for (line in input) {
            val components = line.split(" ")
            val hexStr = components[2].substring(2, 8)

            val dirChar = hexStr[5]
            val cmdDirection = when (dirChar) {
                '3' -> Direction.UP
                '1' -> Direction.DOWN
                '2' -> Direction.LEFT
                '0' -> Direction.RIGHT
                else -> {
                    println("Error! Unknown direction!"); Direction.UP
                }
            }
            val cmdLength = hexStr.take(5).toInt(16)

            val cmd = Command(cmdDirection, cmdLength, 0)
            commands.add(cmd)
            println("Command: ${cmd.direction} ${cmd.meters}")
        }
        return commands
    }

    private fun buildMapFromInstructions(commands: MutableList<Command>): MutableMap<Coor, Char> {
        val map = mutableMapOf<Coor, Char>()
        var x = 0L
        var y = 0L
        for (cmd in commands) {
            when (cmd.direction) {
                Direction.UP -> {
                    for (i in 0..<cmd.meters) {
                        val next = Coor(x, y + i)
                        map[next] = '#'
                    }
                    y += cmd.meters
                }

                Direction.DOWN -> {
                    for (i in 0..<cmd.meters) {
                        val next = Coor(x, y - i)
                        map[next] = '#'
                    }
                    y -= cmd.meters
                }

                Direction.RIGHT -> {
                    for (i in 0..<cmd.meters) {
                        val next = Coor(x + i, y)
                        map[next] = '#'
                    }
                    x += cmd.meters
                }

                Direction.LEFT -> {
                    for (i in 0..<cmd.meters) {
                        val next = Coor(x - i, y)
                        map[next] = '#'
                    }
                    x -= cmd.meters
                }
            }
        }
        return map
    }

    private fun printMap(map: Map<Coor, Char>) {
        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        for (y in maxY downTo  minY ) {
            //println("y==$y")
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
            //println()
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

    private fun findTopLeftCorner(map: MutableMap<Coor, Char>) {
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy {it.y}.y
        for (y in minY .. maxY) {
            //val row = map.filter { it.key.y == y }.toList().sortedBy { it.first.x }.toString()
            val row = map.filter { it.key.y == y }
            val firstHash = row.filter { it.value == '#' }.minByOrNull { it.key.x }
            if (firstHash != null) {
                //val nextPos =
            }


        }
    }

    private fun floodFill(map: MutableMap<Coor, Char>, start: Coor) {
        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y

        val list = mutableListOf<Coor>()
        list.add(start)

        while (list.isNotEmpty()) {
            //print("list size: ${list.size}")
            val coor = list.first()
            map[coor] = '#'
            list.removeFirst()

            if (coor.x - 1 >= minX) {
                val left = Coor(coor.x - 1, coor.y)
                if (map[left] == null && !list.contains(left)) {
                    list.add(left)
                }
            }

            if (coor.x + 1 <= maxX) {
                val right = Coor(coor.x + 1, coor.y)
                if (map[right] == null && !list.contains(right)) {
                    list.add(right)
                }
            }

            if (coor.y - 1 >= minY) {
                val below = Coor(coor.x, coor.y - 1)
                if (map[below] == null && !list.contains(below)) {
                    list.add(below)
                }
            }

            if (coor.y + 1 <= maxY) {
                val above = Coor(coor.x, coor.y + 1)
                if (map[above] == null && !list.contains(above)) {
                    list.add(above)
                }
            }

            //println("list size at end: ${list.size}")
        }
    }

    private fun recursiveFloodFill(map: MutableMap<Coor, Char>, start: Coor) {
        //println("Floodfill: $start")
        //println("${map[start]}")
        if (map[start] == '#')
            return

        val minX = map.keys.minBy { it.x }.x
        val maxX = map.keys.maxBy { it.x }.x
        //println("x: $minX .. $maxX")
        if (start.x < minX || start.x > maxX)
            return

        val minY = map.keys.minBy { it.y }.y
        val maxY = map.keys.maxBy { it.y }.y
        //println("y: $minY .. $maxY")
        if (start.y < minY || start.y > maxY)
            return

        //print("Filling in: $start")
        map[start] = '#'

        recursiveFloodFill(map, Coor(start.x-1, start.y))
        recursiveFloodFill(map, Coor(start.x+1, start.y))
        recursiveFloodFill(map, Coor(start.x, start.y-1))
        recursiveFloodFill(map, Coor(start.x, start.y+1))
    }

    class Command(val direction: Direction, val meters: Int, val color: Int) {
        fun print() {
            println("$direction $meters $color")
        }
    }
    data class Coor(val x: Long, val y: Long)
    class Cube(val pos: Coor, val color: Int)
}