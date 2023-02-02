package com.mpdbailey.nabu

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.AfterTest
import kotlin.test.assertEquals

class BuildNabuTest {

    //On MacOS creates a folder such as:
    //    /var/folders/5x/2_cr44vs0zl5b2v353n2nfz80000gn/T/junit17362152814298797681
    //Note on MacOS, /var/folders are per-user temporary files and caches
    //and are cleaned up by the OS
    @JvmField
    @Rule
    val tmpFolder = TemporaryFolder()

    @AfterTest
    fun cleanup(){
        //Shouldn't be any need for this, but hey check the tmp folders to see if anything has been left from months ago
        tmpFolder.delete()
    }

    @Test
    fun buildDb1(){
        //Build DB and store in a temporary test location
        val path: String = tmpFolder.newFile("nabutest.db").absolutePath
        val target = BuildNabu()
        target.build(path)

        //Run a query on the database
        val nabu = Database(path)
        val indices = nabu.query("close")
        assertEquals(37, indices.count())
    }
}