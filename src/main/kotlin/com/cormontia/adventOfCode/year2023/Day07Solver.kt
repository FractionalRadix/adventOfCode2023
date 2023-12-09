package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day07Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay07.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        val map = mutableMapOf<String,Int>()
        for (line in input) {
            val hand = line.take(5)
            val bid = line.drop(6).toInt()
            map[hand] = bid
        }

        val result1 = solvePart1(map)
        println("The sum is $result1")
        val result2 = solvePart2(map)
        println("The sum when using the Joker rule is $result2")
    }

    class CardTypes {
        val fiveOfAKind = mutableMapOf<String, Int>()
        val fourOfAKind = mutableMapOf<String, Int>()
        val threeOfAKind = mutableMapOf<String, Int>()
        val fullHouse = mutableMapOf<String, Int>()
        val twoPair = mutableMapOf<String, Int>()
        val onePair = mutableMapOf<String, Int>()
        val highCard = mutableMapOf<String, Int>()
    }


    private fun solvePart1(map: MutableMap<String, Int>): Int {

        val cardsByType = determineCardTypesForPart1(map)

        val handsOrderedByType = arrayOf(
            cardsByType.highCard, cardsByType.onePair, cardsByType.twoPair, cardsByType.threeOfAKind, cardsByType.fullHouse, cardsByType.fourOfAKind, cardsByType.fiveOfAKind)

        var rank = 1
        var sum = 0
        for (handsOfAType in handsOrderedByType) {
            val orderedHands = handsOfAType.keys.sortedWith(HandComparator("AKQJT98765432".reversed()))
            for (card in orderedHands) {
                val bid = map[card]!!
                sum += rank * bid
                rank++
            }
        }

        return sum
    }

    private fun determineCardTypesForPart1(map: Map<String, Int>): CardTypes {
        val cardsByType = CardTypes()

        for (hand in map.keys) {
            val handType = handType(hand)
            when (handType) {
                HandType.FiveOfAKind -> cardsByType.fiveOfAKind[hand] = map[hand]!!
                HandType.FourOfAKind -> cardsByType.fourOfAKind[hand] = map[hand]!!
                HandType.FullHouse -> cardsByType.fullHouse[hand] = map[hand]!!
                HandType.ThreeOfAKind -> cardsByType.threeOfAKind[hand] = map[hand]!!
                HandType.TwoPairs -> cardsByType.twoPair[hand] = map[hand]!!
                HandType.OnePair -> cardsByType.onePair[hand] = map[hand]!!
                HandType.HighCard -> cardsByType.highCard[hand] = map[hand]!!
            }
        }

        return cardsByType
    }

    private fun determineCardTypesForPart2(map: Map<String, Int>): CardTypes {
        val cardsByType = CardTypes()

        for (hand in map.keys) {
            val handType = handTypeWithJokerRule(hand)
            when (handType) {
                HandType.FiveOfAKind -> cardsByType.fiveOfAKind[hand] = map[hand]!!
                HandType.FourOfAKind -> cardsByType.fourOfAKind[hand] = map[hand]!!
                HandType.FullHouse -> cardsByType.fullHouse[hand] = map[hand]!!
                HandType.ThreeOfAKind -> cardsByType.threeOfAKind[hand] = map[hand]!!
                HandType.TwoPairs -> cardsByType.twoPair[hand] = map[hand]!!
                HandType.OnePair -> cardsByType.onePair[hand] = map[hand]!!
                HandType.HighCard -> cardsByType.highCard[hand] = map[hand]!!
            }
        }

        return cardsByType
    }

    enum class HandType {
        FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPairs, OnePair, HighCard
    }

    private fun handType(hand: String): HandType {
        val occurrences = countOccurrences(hand)
        if (occurrences.size == 1)
            return HandType.FiveOfAKind
        if (occurrences.size == 2) {
            // This can be: {1,4}, {2,3}
            if (occurrences.values.contains(1))
                return HandType.FourOfAKind
            return HandType.FullHouse
        }
        if (occurrences.size == 3) {
            // This can be: {1,2,2}, {3,1,1}
            if (occurrences.containsValue(3))
                return HandType.ThreeOfAKind
            return HandType.TwoPairs
        }
        if (occurrences.size == 4) {
            // This can be: {1,1,1,2}
            return HandType.OnePair
        }
        // if (occurrences.size == 5)
        return HandType.HighCard
    }

    private fun handTypeWithJokerRule(hand: String): HandType {
        val occurrences = countOccurrences(hand)
        var newHand = hand

        if (occurrences.size == 1)
            return HandType.FiveOfAKind
        if (occurrences.containsKey('J')) {
            val mostOccurringNonJoker = occurrences
                .filter { it.key != 'J' }
                .maxBy { it.value }
                .key
            newHand = hand.replace('J', mostOccurringNonJoker)
        }

        return handType(newHand)
    }


    class HandComparator(private val strengths: String) : Comparator<String> {
        private val radix = strengths.length

        private fun value(hand: String): Int {
            var sum = 0
            for (i in hand.indices) {
                val char = hand[i]
                val value = strengths.indexOf(char)
                sum *= radix
                sum += value
            }
            return sum
        }

        override fun compare(hand1: String, hand2: String) = value(hand1) - value(hand2)
    }

    private fun solvePart2(map: Map<String, Int>): Long {
        val comparator = HandComparator("AKQT98765432J".reversed())

        val cardsByType = determineCardTypesForPart2(map)


        val handsOrderedByType = arrayOf(
            cardsByType.highCard, cardsByType.onePair, cardsByType.twoPair, cardsByType.threeOfAKind, cardsByType.fullHouse, cardsByType.fourOfAKind, cardsByType.fiveOfAKind)

        var rank = 1
        var sum = 0L
        for (handsOfAType in handsOrderedByType) {
            val orderedHands = handsOfAType.keys.sortedWith(comparator)
            for (card in orderedHands) {
                val bid = map[card]!!
                sum += rank * bid
                rank++
            }
        }

        return sum
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