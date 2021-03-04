package wordnet.common

import org.junit.Test
import org.junit.Assert.*

class IndexTest {

    /*
        Data from VSCode Debugger
        Offset: 148117 file: Index.adj
        "close a 15 4 ! & = + 15 7 00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916  "
        148117
        close
        a
        15
        15
        7
        00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916
        4
        1 5 18 20
     */
    @Test
    fun init1() {
        val target = Index("close a 15 4 ! & = + 15 7 00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916  ", 148117)
        assertEquals(148117,target.idxOffset)
        assertEquals("close", target.word)
        assertEquals("a",target.partOfSpeech)
        assertEquals(15,target.senseCount)
        assertEquals(15,target.offsetCount)
        assertEquals(7,target.taggedSensesCount)
        val synSetsOffsets = arrayOf(448955,453543,446553,310743,22256, 893915,560964,2519542,2240484,2007394, 1865308,1450828,1439442,1116967,502916)
        assertArrayEquals(synSetsOffsets,target.synSetsOffsets.toArray())
        assertEquals(4,target.pointersUsedCount)
        val pointersUsed = arrayOf(1,5,18,20)
        assertArrayEquals(pointersUsed,target.pointersUsed.toArray())
    }
}