package com.mpdbailey.nabu

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CompressorTest {

    /**
     * Check that every index entry has a word and at least 1 index
     */
    @Test
    fun consistency1(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val indices = compressor.compressedIndices

        assertTrue(indices.isNotEmpty())
        indices.forEach { index ->
            assertTrue(index.word.isNotEmpty())
            assertTrue { index.indices.isNotEmpty() }
        }
    }

    /**
     * Check that each index id in each synonymSet entry is unique
     */
    @Test
    fun consistency2(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val synonyms = compressor.compressedSynonymSets

        val ids = synonyms.map { it.index }
        assertEquals(ids.count(), ids.distinct().count())
    }

    /**
     * Check that indices in each index entry
     * has a match in the synSets
     */
    @Test
    fun consistency3(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val indices = compressor.compressedIndices
        val synonyms = compressor.compressedSynonymSets

        val idList = indices
            .flatMap { it.indices }
            .distinct()
        //Convert synIds toSet to speed up containsAll from 104s to 2.6s
        val synIds = synonyms.map { it.index }.toSet()
        assertTrue(synIds.containsAll(idList))
    }

    /**
     * Check that each associated index in has an entry
     */
    @Test
    fun consistency4(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val synonyms = compressor.compressedSynonymSets

        val assList = synonyms
            .flatMap { it.associatedIndices }
            .distinct()
        //Convert synIds toSet to speed up containsAll from 104s to 2.6s
        val synIds = synonyms.map { it.index }.toSet()
        assertTrue(synIds.containsAll(assList))
    }

    /**
     * There are only 5 orphaned entries, it's not worth the extra code to remove them, but I leave the code here to find them.
     * There are also 67 empty sets, which are displayed here
     */
    @Test
    fun orphanedEntries1(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val indices = compressor.compressedIndices
        val synonyms = compressor.compressedSynonymSets

        val idList = indices
            .flatMap { it.indices }
        val assList = synonyms
            .flatMap { it.associatedIndices }
        val allList = (idList + assList)
            .distinct()
            .toSet()
        val synIds = synonyms.map { it.index }.toSet()

        assertTrue(synIds.containsAll(allList))

        val orphans = (synIds - allList)
            .map {o -> synonyms.first { it.index == o }}
        assertEquals(orphans.count(),5)

        //Display the orphans
        orphans.forEach{
                println(it.words + " : "+it.definitions)
            }

        //Display empty synonym sets
        synonyms.filter { it.words.isEmpty() }.forEach {
            println(it.words + " : " + it.definitions)
        }
    }

    /**
     * IN CSK type in ZION
     * Thesaurus list, shows results for
     *     popular_front_for_the_liberation_of_palestine-general_command
     *     iz_al-din_al-qassam_battalions
     *
     * Clicking on these does a browser search
     *
     * These tests are to check if the data has been processed correctly
     */
    @Test
    fun zionBug1(){
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        val indices = compressor.compressedIndices

        val popFrontJudea = indices.first { it.word == "popular front for the liberation of palestine general command"}
        assertNotNull(popFrontJudea)

        val synIndex = popFrontJudea.indices[0]
        val sets = compressor.compressedSynonymSets.first { it.index == synIndex }
        assertNotNull(sets)
    }


}