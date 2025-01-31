package com.mpdbailey.nabu

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.sqlite.SQLiteException

class DbAdditionsTest {
    @JvmField
    @Rule
    val tmpFolder = TemporaryFolder()

    private fun createDb():String {
        val path: String = tmpFolder.newFile("nabutest.db").absolutePath
        BuildNabu().build(path)
        return path
    }

    /**
     * Check new addition is appended to the lookup entry for sorcerer
     */
    @Test
    fun add1() {
        val path = createDb()
        val dbAdditions = DbAdditions(path)
        val database = Database(path)

        val definitionData = DefinitionData(
            "sorcerer",
            PartOfSpeech.NOUN,
            "Wizzy Tits",
            listOf("wiz","ace","tit"),
            emptyList(),
            )
        definitionData.synIndex = "F00"
        dbAdditions.add(definitionData)
        val queryResult = database.query("sorcerer")
        assertTrue(queryResult.contains("F00"))
        assertEquals(2,queryResult.size)

        //Check definition exists
        val querySyn = database.querySynonymSet("F00")!!
        assertEquals(querySyn.definitions,"Wizzy Tits")
    }

    /**
     * Check new addition is added to the lookup table
     */
    @Test
    fun add2() {
        val path = createDb()
        val dbAdditions = DbAdditions(path)
        val database = Database(path)

        val definitionData = DefinitionData(
            "deepseek",
            PartOfSpeech.NOUN,
            "Wizzy Tits",
            listOf("wiz","ace","tit"),
            emptyList(),
        )
        definitionData.synIndex = "F00"
        dbAdditions.add(definitionData)
        val queryResult = database.query("deepseek")
        assertTrue(queryResult.contains("F00"))
        assertEquals(1,queryResult.size)

        //Check definition exists
        val querySyn = database.querySynonymSet("F00")!!
        assertEquals(querySyn.definitions,"Wizzy Tits")
    }

    /**
     * Bad ID as already taken
     */
    @Test(expected = SQLiteException::class)
    fun add3() {
        val path = createDb()
        val dbAdditions = DbAdditions(path)

        val definitionData = DefinitionData(
            "deepseek",
            PartOfSpeech.NOUN,
            "Wizzy Tits",
            listOf("wiz","ace","tit"),
            emptyList(),
        )
        definitionData.synIndex = "001"
        dbAdditions.add(definitionData)
    }

}