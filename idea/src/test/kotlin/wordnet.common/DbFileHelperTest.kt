package wordnet.common

import org.junit.Test

import org.junit.Assert.*
import java.io.File

class DbFileHelperTest {

    @Test
    fun getAllIndexFiles1() {
        getAllIndexFiles().forEach {
            println(it)
            assertTrue(File(it).exists())
        }
    }

    @Test
    fun getAllDataFiles1() {
        getAllDataFiles().forEach {
            println(it)
            assertTrue(File(it).exists())
        }
    }

    @Test
    fun getDbFilePath1() {
        val file = getDbFilePath(DbType.Data,DbPartOfSpeech.Adj)
        println(file)
        assertTrue(File(file).exists())
    }
}