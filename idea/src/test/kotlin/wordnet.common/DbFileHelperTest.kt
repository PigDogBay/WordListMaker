package wordnet.common

import org.junit.Test

import org.junit.Assert.*
import java.io.File

class DbFileHelperTest {

    @Test
    fun loadIndices1() {
        val target = DbFileHelper()
        val indices = target.loadIndices(DbPartOfSpeech.Noun)
        assertEquals(117953, indices.count())
    }

    @Test
    fun loadDefinitions1() {
        val target = DbFileHelper()
        val definitions = target.loadDefinitions(DbPartOfSpeech.Adj)
        assertEquals(18185, definitions.count())
    }

    @Test
    fun getAllIndexFiles1() {
        val target = DbFileHelper()
        target.getAllIndexFiles().forEach {
            println(it)
            assertTrue(File(it).exists())
        }
    }

    @Test
    fun getAllDataFiles1() {
        val target = DbFileHelper()
        target.getAllDataFiles().forEach {
            println(it)
            assertTrue(File(it).exists())
        }
    }

    @Test
    fun getDbFilePath1() {
        val target = DbFileHelper()
        val path =  target.getDbFilePath(DbType.Data,DbPartOfSpeech.Adj)
        assertTrue(File(path).exists())
    }

    /**
    Check that the indices have the correct part of speech
     */
    private fun checkPartOfSpeech(dbPartOfSpeech: DbPartOfSpeech) : Boolean {
        val indices = DbFileHelper().loadIndices(dbPartOfSpeech)
        return indices.none { it.partOfSpeech != dbPartOfSpeech.letter }
    }

    /**
    Check that the definitions have the correct part of speech
     */
    private fun checkDefPartOfSpeech(dbPartOfSpeech: DbPartOfSpeech) : Boolean {
        val definitions = DbFileHelper().loadDefinitions(dbPartOfSpeech)
        return definitions
            .none { it.partOfSpeech != dbPartOfSpeech.letter}
    }

    @Test
    fun partOfSpeechNoun(){
        assertTrue(checkPartOfSpeech(DbPartOfSpeech.Noun))
        assertTrue(checkDefPartOfSpeech(DbPartOfSpeech.Noun))
    }
    @Test
    fun partOfSpeechVerb(){
        assertTrue(checkPartOfSpeech(DbPartOfSpeech.Verb))
        assertTrue(checkDefPartOfSpeech(DbPartOfSpeech.Verb))
    }
    @Test
    fun partOfSpeechAdv(){
        assertTrue(checkPartOfSpeech(DbPartOfSpeech.Adv))
        assertTrue(checkDefPartOfSpeech(DbPartOfSpeech.Adv))
    }

    /**
     * Adjective letter can be a or s (satellite adjective)
     */
    @Test
    fun partOfSpeechAdj(){
        assertTrue(checkPartOfSpeech(DbPartOfSpeech.Adj))
        val definitions = DbFileHelper().loadDefinitions(DbPartOfSpeech.Adj)
        val actual = definitions
            .none { !DbPartOfSpeech.isAdjective(it.partOfSpeech)}
        assertTrue(actual)
    }
}