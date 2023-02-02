package wordnet.common

import org.junit.Test
import java.io.RandomAccessFile
import kotlin.test.assertEquals

/**
 * Checks the integrity of the data in the Word Net index and data files
 *
 * Note if these index/data files are ever edited then the offsets will no longer be valid unless
 * they are recalculated.  The offsets are only useful for RandomAccessFile searches, so it the files are edited
 * then make a note about it, delete the RndAccFile code and delete the offsets tests.
 */
class DataIntegrityTest {

    /**
     * Checks the data file to ensure definition offsets match the file offsets
     * Returns the number of line entries that passed.
     *
     * Note the return count should be equal to the line count of the file minus the lines in the copyright notice
     * To get line count and then subtract 29 for the copyright:
     * wc -l data.noun
     * expr 82221 - 29
     * 82192  <- Test for this
     */
    private fun checkOffsets(dbPartOfSpeech: DbPartOfSpeech) : Int {
        val dbFileHelper = DbFileHelper()
        val filename = dbFileHelper.getDbFilePath(DbType.Data, dbPartOfSpeech)
        var count = 0
        RandomAccessFile(filename,"r").use { fs ->
            for (i in 1..dbFileHelper.copyrightLineCount){
                fs.readLine()
            }
            do {
                val offset = fs.filePointer.toInt()
                val line = fs.readLine() ?: break
                val tokens = line.split(' ')
                count++
                if (tokens[0].toInt()!= offset) break
            } while (true)
        }
        return count
    }

    @Test
    fun checkNounOffsets1(){
        val count = checkOffsets(DbPartOfSpeech.Noun)
        assertEquals(82192,count)
    }

    @Test
    fun checkVerbOffsets1(){
        val count = checkOffsets(DbPartOfSpeech.Verb)
        assertEquals(13789,count)
    }

    @Test
    fun checkAdjectiveOffsets1(){
        val count = checkOffsets(DbPartOfSpeech.Adj)
        assertEquals(18185,count)
    }

    @Test
    fun checkAdverbOffsets1(){
        val count = checkOffsets(DbPartOfSpeech.Adv)
        assertEquals(3625,count)
    }
}