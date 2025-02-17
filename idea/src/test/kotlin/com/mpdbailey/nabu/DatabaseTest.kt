package com.mpdbailey.nabu

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.sqlite.SQLiteException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DatabaseTest {

    //On MacOS creates a folder such as:
    //    /var/folders/5x/2_cr44vs0zl5b2v353n2nfz80000gn/T/junit17362152814298797681
    //Note on MacOS, /var/folders are per-user temporary files and caches
    //and are cleaned up by the OS
    @JvmField
    @Rule
    val tmpFolder = TemporaryFolder()

    private fun createDb() : Database{
        val path: String = tmpFolder.newFile("nabutest.db").absolutePath
        BuildNabu().build(path)
        return Database(path)
    }

    /**
        Check that cetchup has been removed from the index list
     */
    @Test
    fun bannedWords1(){
        val nabu = createDb()
        val indices = nabu.query("cetchup")
        assertEquals(0,indices.count())
    }

    /**
        Check that cetchup has been removed from the ketchup's synonym set
        but makesure ketchup is still there
     */
    @Test
    fun bannedWords2(){
        val nabu = createDb()
        val indices = nabu.query("ketchup")
        assertEquals(1,indices.count())
        assertEquals(3,indices[0].count())
        val synSet = nabu.querySynonymSet(indices[0])!!
        assertFalse(synSet.words.contains("cetchup") )
        assertEquals(synSet.words[0],"catsup" )
        assertEquals(synSet.words[1],"ketchup" )
        assertEquals(synSet.words[2],"tomato ketchup" )
    }

    @Test
    fun abbreviations1(){
        val nabu = createDb()
        val indices = nabu.query("g b shaw")
        assertEquals(1,indices.count())
        val synSet = nabu.querySynonymSet(indices[0])!!
        assertTrue(synSet.words.contains("g b shaw"))
    }

    @Test
    fun abbreviations2(){
        val nabu = createDb()
        val indices = nabu.query("nb")
        assertEquals(2,indices.count())
        val synSet = nabu.querySynonymSet(indices[0])!!
        assertTrue(synSet.words.contains("nota bene"))
    }

    /**
        Check that illegal synonyms are removed such as
        Lo/Ovral, A/C, read/write_head, 20/20, 24/7, 9/11, counts/minute
     */
    @Test
    fun illegalSynonyms1(){
        val nabu = createDb()
        val indices = nabu.query("twenty twenty")
        assertEquals(1,indices.count())
        val synSet = nabu.querySynonymSet(indices[0])!!
        assertEquals(1,synSet.words.count())
        assertEquals("twenty-twenty",synSet.words[0])
    }

    @Test
    fun exceptions1() {
        val nabu = createDb()
        val bases = nabu.queryExceptions("bacilli")
        assertEquals(1,bases.count())
        assertEquals("bacillus", bases[0])
    }

    @Test
    fun exceptions2() {
        val nabu = createDb()
        val bases = nabu.queryExceptions("masses")
        assertEquals(2,bases.count())
        assertEquals("mass", bases[0])
        assertEquals("masse", bases[1])
    }

    @Test
    fun update1(){
        val nabu = createDb()
        val synIndex = SynonymIndex("bail",listOf("AAA","BBB"))
        nabu.update(synIndex)
        val actualIds = nabu.query("bail").reduce{a,s->"$a $s"}
        assertEquals("AAA BBB",actualIds)
    }

    @Test(expected = SQLiteException::class)
    fun uniqueId1(){
        val nabu = createDb()
        val synSet = SynonymSet("001",listOf("not","unique", "id"), emptyList(),PartOfSpeech.NOUN,"expect an exception")
        nabu.insertSynonyms(listOf((synSet)))
    }

    /**
     * IN CSK type in ZION
     * Thesaurus list, shows results for
     *     popular_front_for_the_liberation_of_palestine-general_command
     *     iz_al-din_al-qassam_battalions
     *
     * Clicking on these does a browser search
     *
     * These tests are to check if the data has been entered correctly into the database
     */
    @Test
    fun zionBugIndex1(){
        val nabu = createDb()
        val indices = nabu.query("popular front for the liberation of palestine general command")
        assertEquals(1,indices.count())
        val synSet = nabu.querySynonymSet(indices[0])!!
        assertTrue(synSet.definitions.contains("Marxist-Leninist"))
    }
}