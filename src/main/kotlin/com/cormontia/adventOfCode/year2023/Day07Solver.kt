package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day07Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay07_sample1.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        val map = mutableMapOf<String,Int>()
        for (line in input) {
            val hand = line.take(5)
            val bid = line.drop(6).toInt()
            map[hand] = bid
            //println("Mapping $hand to $bid.")
        }

        val fiveOfAKind = mutableMapOf<String, Int>()
        val fourOfAKind = mutableMapOf<String, Int>()
        val threeOfAKind = mutableMapOf<String, Int>()
        val fullHouse = mutableMapOf<String, Int>()
        val twoPair = mutableMapOf<String, Int>()
        val onePair = mutableMapOf<String, Int>()
        val highCard = mutableMapOf<String, Int>()
        for (hand in map.keys) {
            val occurences = countOccurrences(hand)
            if (occurences.size == 1) {
                // 5
                println("Five of a kind: $hand .")
                fiveOfAKind[hand] = map[hand]!!
            } else if (occurences.containsValue(4)) {
                // 4-1
                println("Four of a kind: $hand .")
                fourOfAKind[hand] = map[hand]!!
            } else if  (occurences.containsValue(3)) {
                // 3-2 or 3-1-1
                if (occurences.containsValue(2)) {
                    println("Full house: $hand .")
                    fullHouse[hand] = map[hand]!!
                } else if (occurences.containsValue(1)) {
                    println("Three of a kind: $hand .")
                    threeOfAKind[hand] = map[hand]!!
                }
            } else if (occurences.containsValue(2)) {
                // 2-2-1 or 2-1-1-1 . Note that 2-3 is already handled above.
                if (occurences.size == 3) {
                    println("Two pairs: $hand")
                    twoPair[hand] = map[hand]!!
                } else { // occurrences.size == 4
                    println("Single pair: $hand")
                    onePair[hand] = map[hand]!!
                }
            } else if (occurences.size == 5) {
                println("High card: $hand")
                highCard[hand] = map[hand]!!
            }

            val total = fiveOfAKind.size + fourOfAKind.size + threeOfAKind.size + fullHouse.size + twoPair.size + onePair.size + highCard.size
            println("Check: total nr is $total")

            var rank = 1
            var sum = 0
            val handsOrderedByType = arrayOf(highCard, onePair, twoPair, threeOfAKind, fullHouse, fourOfAKind, fiveOfAKind)
            for (handsOfAType in handsOrderedByType) {
                //TODO!~  Make a custom ordering!! K > Q and stuff like that...
                val orderedHands = handsOfAType.keys.sorted()
                for (card in orderedHands) {
                    val bid = map[card]!!
                    println("$card : $rank * $bid")
                    sum += rank * bid
                    rank++
                }
            }
            println(sum)


        }
    }



    /**
     * Given a string. Determine how often each character occurs.
     * For example, "AABFFFF" would result in {A=2, B=1, F=4}.
     * @param str The string to investigate.
     * @return A mapping that maps each character in the string to the number of times it occurs in the string.
     */
    private fun countOccurrences(str: String): Map<Char, Int> {
        val result = mutableMapOf<Char, Int>()
        for (ch in str) {
            result.putIfAbsent(ch, 0)
            result[ch] = result[ch]!! + 1
        }
        return result
    }

}