package wordnet.common

import org.junit.Test

import org.junit.Assert.*

class DictionaryHelperTest {
    @Test
    fun fastSearch1(){
        val actual = fastSearch("close", getDbFilePath(DbType.Index,DbPartOfSpeech.Adj))
        assertEquals(148117,actual)
    }
    @Test
    fun fastSearch2(){
        val actual = fastSearch("nosnackingbetweenmeals", getDbFilePath(DbType.Index,DbPartOfSpeech.Adj))
        assertEquals(0,actual)
    }

    @Test
    fun readRecord1() {
        val actual = readRecord(148117, getDbFilePath(DbType.Index, DbPartOfSpeech.Adj))
        assertEquals("close a 15 4 ! & = + 15 7 00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916  ", actual)
    }
}