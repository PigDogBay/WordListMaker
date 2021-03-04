package wordnet.common

import org.junit.Test

import org.junit.Assert.*

class DefinitionTest {
    /*
        Data.Adj
        448955
        close
        "00448955 00 a 01 close 1 020 = 05091408 n 0000 + 05092779 n 0102 ! 00447582 a 0101 & 00449506 a 0000 & 00449787 a 0000 & 00449943 a 0000 & 00450164 a 0000 & 00450348 a 0000 & 00450678 a 0000 & 00450811 a 0000 & 00450958 a 0000 & 00451113 a 0000 & 00451366 a 0000 & 00451559 a 0000 & 00451696 a 0000 & 00451803 a 0000 & 00451923 a 0000 & 00452181 a 0000 & 00452303 a 0000 & 00452436 a 0000 | at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"  "
    */
    @Test
    fun init1() {
        val target = Definition("close", "00448955 00 a 01 close 1 020 = 05091408 n 0000 + 05092779 n 0102 ! 00447582 a 0101 & 00449506 a 0000 & 00449787 a 0000 & 00449943 a 0000 & 00450164 a 0000 & 00450348 a 0000 & 00450678 a 0000 & 00450811 a 0000 & 00450958 a 0000 & 00451113 a 0000 & 00451366 a 0000 & 00451559 a 0000 & 00451696 a 0000 & 00451803 a 0000 & 00451923 a 0000 & 00452181 a 0000 & 00452303 a 0000 & 00452436 a 0000 | at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"  ")

        assertEquals("at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"", target.definitionText)
        assertEquals(1, target.definitionType)
        assertEquals(0,target.fileNumber)
        assertEquals(null,target.nextDefinition)
        assertEquals(null,target.nextForm)
        assertEquals("a",target.partOfSpeech)
        assertEquals(448955,target.position)
        assertEquals(20,target.ptrCount)
        assertEquals(null,target.ptrList)
        assertEquals(1,target.wordCount)
        val ptrFromFields = arrayOf(0,1,1,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0)
        assertArrayEquals(ptrFromFields,target.ptrFromFields.toArray())
        val ptrOffsets = arrayOf(5091408,5092779,447582,449506,449787, 449943,450164,450348,450678,450811, 450958,451113,451366,451559,451696, 451803,451923,452181,452303,452436)
        assertArrayEquals(ptrOffsets, target.ptrOffsets.toArray())
        val partOfSpeech = arrayOf(DbPartOfSpeech.Noun, DbPartOfSpeech.Noun, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj,  DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj, DbPartOfSpeech.Adj)
        assertArrayEquals(partOfSpeech,target.ptrPartOfSpeech.toArray())
        val ptrToFields = arrayOf(0,2,1,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0)
        assertArrayEquals(ptrToFields,target.ptrToFields.toArray())
        val ptrTypes = arrayOf(18,20,1,5,5, 5,5,5,5,5, 5,5,5,5,5, 5,5,5,5,5)
        assertArrayEquals(ptrTypes,target.ptrTypes.toArray())
        val words = arrayOf("close")
        assertArrayEquals(words,target.words.toArray())
        assertTrue(target.frameIds.isEmpty())
        assertTrue(target.frameToFields.isEmpty())

    }
}