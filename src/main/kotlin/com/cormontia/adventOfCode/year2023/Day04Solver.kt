package com.cormontia.adventOfCode.year2023

import utils.parseListOfIntegers
import utils.pow
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day04Solver {
    fun solve() {
        val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay04.txt""")
            .readLines()
            .filter { str -> str.isNotEmpty() }

        val allCards = mutableListOf<Card>()

        var sum = 0
        var cardNum = 1 // Ideally we'd take this from the input string as well, instead of initializing it this way...
        firstInputAsStrings.forEach {
            val cardNameNumbersSplit = it.split(":")
            val numbersSplit = cardNameNumbersSplit[1].split("|")
            val numbers = parseListOfIntegers(numbersSplit[0])
            val winningNumbers = parseListOfIntegers(numbersSplit[1])
            val card = Card(cardNum, numbers, winningNumbers)
            cardNum++
            sum += card.value()

            allCards.add(card)
        }
        println("The sum of winning values is: $sum")

        var total = 0
        val cards = mutableListOf<Card>()
        allCards.forEach { cards.add(it) }
        total += cards.size

        do {
            val currentCard = cards.firstOrNull()
            if (currentCard == null) {
                //TODO?+
            } else {
                val win = currentCard.getNrOfWinningNumbers()
                for (i in currentCard.nr + 1..currentCard.nr + win) {
                    cards.add(allCards.first { it.nr == i })
                    total++
                }
                cards.remove(currentCard)
            }
            println(cards.size)
        } while (cards.isNotEmpty())
        println(total)

    }

    //TODO!+ Working on a less naive solution for day 4, part 2.
    private fun day4part2(allCards: List<Card>) {
        val firstMap = mutableMapOf<Card, Int>() // map card nrs to amounts

        allCards.forEach { firstMap[it] = 1 }

        val nextMap = mutableMapOf<Card, Int>()
        for (card in firstMap.keys) {
            val nrOfWinningNumbers = card.getNrOfWinningNumbers()
            for (i in card.nr + 1 .. card.nr + nrOfWinningNumbers) {
                val newCard = allCards.first { it.nr == i }
                if (nextMap.containsKey(newCard)) {
                    nextMap[newCard] = nextMap[newCard]!! + 1
                } else {
                    nextMap[newCard] = 1
                }
            }
        }

        TODO()

    }
    class Card(val nr: Int, private val numbers: List<Int>, private val winningNumbers: List<Int>) {
        fun value(): Int {
            val nrOfWinningNumbers = getNrOfWinningNumbers()
            return if (nrOfWinningNumbers == 0) {
                0
            } else {
                pow(2, nrOfWinningNumbers - 1)
            }
        }

        fun getNrOfWinningNumbers() = numbers.intersect(winningNumbers).size
    }

}