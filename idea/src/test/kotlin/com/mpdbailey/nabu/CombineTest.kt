package com.mpdbailey.nabu

import org.junit.Test
import org.junit.Assert.*

class CombineTest {
    @Test
    fun indices1(){
        val target = Combine()
        val indices = target.indices()
        assertEquals(147478, indices.count())
    }

    @Test
    fun synonyms1(){
        val target = Combine()
        val synonyms = target.synonyms()
        assertEquals(117791, synonyms.count())
    }

}