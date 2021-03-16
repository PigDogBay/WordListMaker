package com.mpdbailey.nabu

import org.junit.Test

import org.junit.Assert.*

class CompressedIndexTest {

    @Test
    fun reset() {
        val target = CompressedIndex()
        target.reset()
        assertEquals("001", target.next())
    }

    @Test
    fun next() {
        val target = CompressedIndex()
        for (i in 1..104997){
            target.next()
        }
        assertEquals("Bay", target.next())
    }
}