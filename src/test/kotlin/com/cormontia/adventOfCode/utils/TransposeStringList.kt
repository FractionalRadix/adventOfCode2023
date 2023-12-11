package com.cormontia.adventOfCode.utils

import org.junit.jupiter.api.Test
import utils.transposeStringList
import kotlin.test.assertEquals

//TODO!+ Set up unit testing properly.
class TransposeStringList {
    @Test
    fun testTransposeStringList() {
        val list = listOf("123","456","789")

        val transposedList = transposeStringList(list)

        assertEquals("147", transposedList[0])
        assertEquals("258", transposedList[1])
        assertEquals("369", transposedList[2])
    }
}