package com.mpdbailey.nabu

import org.junit.Test

import org.junit.Assert.*
import wordnet.common.Definition
import wordnet.common.Index
import java.lang.IllegalArgumentException

class AdapterTest {
    @Test
    fun convertIndex1() {
        val target = Adapter()
        val index = Index("close a 15 4 ! & = + 15 7 00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916  ", 148117)
        val actual = target.convert(index)
        assertEquals("close", actual.word)
        assertEquals(actual.indices.count(),15)
        assertEquals(actual.indices[0],"106d9bb")
    }

    @Test
    fun convertDefinition1() {
        val target = Adapter()
        val definition = Definition("close", "00448955 00 a 01 close 1 020 = 05091408 n 0000 + 05092779 n 0102 ! 00447582 a 0101 & 00449506 a 0000 & 00449787 a 0000 & 00449943 a 0000 & 00450164 a 0000 & 00450348 a 0000 & 00450678 a 0000 & 00450811 a 0000 & 00450958 a 0000 & 00451113 a 0000 & 00451366 a 0000 & 00451559 a 0000 & 00451696 a 0000 & 00451803 a 0000 & 00451923 a 0000 & 00452181 a 0000 & 00452303 a 0000 & 00452436 a 0000 | at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"  ")
        val synonymSet = target.convert(definition)
        val words = arrayOf("close")
        assertArrayEquals(words,synonymSet.words.toTypedArray())
        assertEquals(synonymSet.index,"106d9bb")
        assertEquals(synonymSet.associatedIndices.count(),20)
        assertEquals(synonymSet.associatedIndices[0],"84db050")
        assertEquals(PartOfSpeech.ADJECTIVE, synonymSet.partOfSpeech)
        assertEquals("at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"",synonymSet.definitions)
    }

    @Test(expected = IllegalArgumentException::class)
    fun convertDefinition2() {
        val target = Adapter()
        val definition = Definition(
            "close",
            "16777216 00 a 01 close 1 020 = 05091408 n 0000 + 05092779 n 0102 ! 00447582 a 0101 & 00449506 a 0000 & 00449787 a 0000 & 00449943 a 0000 & 00450164 a 0000 & 00450348 a 0000 & 00450678 a 0000 & 00450811 a 0000 & 00450958 a 0000 & 00451113 a 0000 & 00451366 a 0000 & 00451559 a 0000 & 00451696 a 0000 & 00451803 a 0000 & 00451923 a 0000 & 00452181 a 0000 & 00452303 a 0000 & 00452436 a 0000 | at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"  "
        )
        target.convert(definition)
    }
}