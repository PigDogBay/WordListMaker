package com.mpdbailey.nabu

import com.mpdbailey.scowl.containsIllegalChar
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MorphologyTest {
    @Test
    fun loadAllExceptions1(){
        val morphology = Morphology()
        val exceptions = morphology.loadAllExceptions()
        assertEquals(5931,exceptions.count())
        val ex1 = exceptions.first { it.inflected == "masses" }
        assertEquals("mass",ex1.baseForms[0])
        assertEquals("masse",ex1.baseForms[1])
    }

    private fun isIllegal(item : ExceptionItem) : Boolean {
        val strings = listOf(item.inflected) + item.baseForms
        return strings
            .map { it.replace(" ","") }
            .map{containsIllegalChar(it)}
            .contains(true)
    }

    /**
     * Check that strings only contain a-z (or space) chars only
     */
    @Test
    fun illegalChars1(){
        val illegals = Morphology()
            .loadAllExceptions()
            .filter{isIllegal(it)}
        assertTrue(illegals.isEmpty())
    }

    private fun listDuplicates(){
        val duplicates = Morphology()
            .loadAllExceptions()
            .groupBy { it.inflected }
            .filter { it.value.count()>1 }
        duplicates.keys.forEach { key ->
            duplicates[key]?.forEach{value ->
                println("${value.partOfSpeech} ${value.inflected} - ${value.baseForms[0]}")
            }
        }

    }

    @Test
    fun noDuplicates1() {
        val duplicates = Morphology()
            .loadAllExceptions()
            .groupingBy { it.inflected }
            .eachCount()
            .filter { it.value>1 }
        assertTrue(duplicates.isEmpty())
    }

    @Test
    fun getWordBases1(){
        val morph = Morphology()
        val bases = morph.getWordBases("acest")
        assertEquals(2,bases.count())
        assertEquals("ac",bases[0])
        assertEquals("ace",bases[1])
    }
    @Test
    fun getWordBases2(){
        val morph = Morphology()
        val bases = morph.getWordBases("programming")
        assertEquals(2,bases.count())
        assertEquals("programme",bases[0])
        assertEquals("programm",bases[1])
    }

    @Test
    fun getWordBasesFul1(){
        val morph = Morphology()
        val bases = morph.getWordBases("boxesful")
        assertEquals(2,bases.count())
        assertEquals("boxeful",bases[0])
        assertEquals("boxful",bases[1])
    }

    @Test
    fun getWordBasesFail1(){
        val morph = Morphology()
        val bases = morph.getWordBases("nosuchendqwt")
        assertTrue(bases.isEmpty())
    }
    @Test
    fun getWordBasesFail2(){
        val morph = Morphology()
        val bases = morph.getWordBases("")
        assertTrue(bases.isEmpty())
    }
    @Test
    fun getWordBasesFail3(){
        val morph = Morphology()
        val bases = morph.getWordBases("ing")
        assertTrue(bases.isEmpty())
    }
    @Test
    fun getWordBasesFail4(){
        val morph = Morphology()
        val bases = morph.getWordBases("es")
        assertTrue(bases.isEmpty())
    }

}