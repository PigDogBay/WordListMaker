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
}