package wordnet.common

import org.junit.Test
import kotlin.test.assertEquals

class AdditionsTest {

    val expected = "01540042 00 a 03 Holly 0 Henry 0 Da_Da 0 004 @ 00000042 n 0000 @ 00000900 v 0000 @ 00001066 a 0000 @ 90001000 r 0000 | Little sods; \"How naughty!\""

    @Test
    fun generate1(){
        val target = Additions()
        target.position = 1540042
        target.words.add("Holly")
        target.words.add("Henry")
        target.words.add("Da Da")
        target.partOfSpeech = DbPartOfSpeech.Adj
        target.definitions = "Little sods; \"How naughty!\""

        target.associates.add(AssociatedIndex(42,DbPartOfSpeech.Noun))
        target.associates.add(AssociatedIndex(900,DbPartOfSpeech.Verb))
        target.associates.add(AssociatedIndex(1066,DbPartOfSpeech.Adj))
        target.associates.add(AssociatedIndex(90001000,DbPartOfSpeech.Adv))

        val actual = target.generate()
        assertEquals(expected,actual)
    }

    /**
     * Check that the expected string is of the correct format by parsing it.
     */
    @Test
    fun expectedTest1(){
        val definition = Definition("expected", expected,0)
        assertEquals(3,definition.wordCount)
        assertEquals("a",definition.partOfSpeech)
        assertEquals(4,definition.associatedIndices.size)
        assertEquals(1540042, definition.position)
        assertEquals("Henry",definition.words[1])
        assertEquals(1066,definition.associatedIndices[2].offset)
        assertEquals(DbPartOfSpeech.Verb,definition.associatedIndices[1].partOfSpeech)
    }

}