package com.mpdbailey.nabu

import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class CompressedIndexTest {

    private val indices = listOf("12345678","a7654321","42424242")

    @Test
    fun compress1() {
        val target = CompressedIndex()
        target.createMap(indices)
        val actual = target.compress("12345678")
        assertEquals("001",actual)
    }

    @Test
    fun compress2() {
        val target = CompressedIndex()
        target.createMap(indices)
        val actual = target.compress(listOf("12345678", "12345678", "42424242"))
        assertEquals(listOf("001","001","003"),actual)
    }

    @Test
    fun compress3() {
        val target = CompressedIndex()
        target.createMap(indices+indices)
        val actual = target.compress("12345678")
        assertEquals("001",actual)
    }

    @Test
    fun compress4() {
        val target = CompressedIndex()
        target.createMap(listOf("a","a","a"))
        val actual = target.compress("a")
        assertEquals("001",actual)
    }

    @Test(expected = NullPointerException::class)
    fun compress5() {
        val target = CompressedIndex()
        target.createMap(indices)
        target.compress("XXX")
    }

    @Test
    fun compress6() {
        val target = CompressedIndex()
        val bigList = (1..150000).map { it.toString() }
        target.createMap(bigList)
        val compBigList = bigList
            .map { target.compress(it) }
            .distinct()
        assertEquals(150000,compBigList.count())
    }
}