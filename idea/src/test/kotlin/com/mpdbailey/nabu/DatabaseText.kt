package com.mpdbailey.nabu

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DatabaseText {

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


}