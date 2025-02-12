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

    /**
     * IN CSK type in ZION
     * Thesaurus list, shows results for
     *     popular_front_for_the_liberation_of_palestine-general_command
     *     iz_al-din_al-qassam_battalions
     *
     * Clicking on these does a browser search
     *
     * These tests are to check if the data has been loaded correctly from the raw files
     *
     * Raw Data:-
     *   zion n 3 5 @ #p %m %p - 3 0 08814203 08810400 05639839
     *     08814203 15 n 02 Zion 2 Sion 2 002 @i 09325914 n 0000 #p 08812650 n 0000 | originally a stronghold captured by David (the 2nd king of the Israelites); above it was built a temple and later the name extended to the whole hill; finally it became a synonym for the city of Jerusalem; "the inhabitants of Jerusalem are personified as `the daughter of Zion'"
     *     08810400 15 n 05 Israel 0 State_of_Israel 0 Yisrael 0 Zion 0 Sion 0 031 @i 08562388 n 0000 #p 08809019 n 0000 -r 08028255 n 0000 -r 08037538 n 0000 -r 08045150 n 0000 -r 08055500 n 0000 -r 08056064 n 0000 -r 08057009 n 0000 -r 08073625 n 0000 -r 08243759 n 0000 -r 08362293 n 0000 -r 08362540 n 0000 -r 08362697 n 0000 -r 08363213 n 0000 -r 08363417 n 0000 -r 08364384 n 0000 %p 08811162 n 0000 %p 08811341 n 0000 %p 08811766 n 0000 %p 08812218 n 0000 %p 08812426 n 0000 %p 08812650 n 0000 %p 08813344 n 0000 %p 08813506 n 0000 %p 08813826 n 0000 %p 08815692 n 0000 %p 08815914 n 0000 %p 09194457 n 0000 %p 09286525 n 0000 %p 09384713 n 0000 %m 09735229 n 0000 | Jewish republic in southwestern Asia at eastern end of Mediterranean; formerly part of Palestine
     *     05639839 09 n 03 Utopia 0 Zion 1 Sion 1 001 @ 05633662 n 0000 | an imaginary place considered to be perfect or ideal
     *
     *   popular_front_for_the_liberation_of_palestine-general_command n 1 2 @ ; 1 0 08055500
     *     08055500 14 n 02 Popular_Front_for_the_Liberation_of_Palestine-General_Command 0 PFLP-GC 0 003 @i 08409094 n 0000 ;c 00761047 n 0000 ;r 08810400 n 0000 | a Marxist-Leninist terrorist organization that conducted several attacks in western Europe
     *
     *   iz_al-din_al-qassam_battalions n 1 3 @ #p ; 1 0 08057009
     *     08057009 14 n 03 Qassam_Brigades 0 Salah_al-Din_Battalions 0 Iz_Al-Din_Al-Qassam_Battalions 0 004 @i 08409094 n 0000 #p 08039012 n 0000 ;c 00761047 n 0000 ;r 08810400 n 0000 | the military arm of Hamas responsible for suicide bombings and other attacks on Israel
     */
    @Test
    fun zionBugIndex1() {
        val target = DbFileHelper()
        val indices = target.loadIndices(DbPartOfSpeech.Noun)

        val peoplesFrontOfJudea = indices.first {
            it.word == "popular_front_for_the_liberation_of_palestine-general_command"
        }
        kotlin.test.assertNotNull(peoplesFrontOfJudea)
        kotlin.test.assertEquals(8055500, peoplesFrontOfJudea.synSetsOffsets.first())
    }

    @Test
    fun zionBugData1() {
        val target = DbFileHelper()
        val definitions = target.loadDefinitions(DbPartOfSpeech.Noun)

        val peoplesFrontOfJudea = definitions.first {
            it.position == 8055500
        }
        kotlin.test.assertNotNull(peoplesFrontOfJudea)
        kotlin.test.assertTrue(peoplesFrontOfJudea.definitionText.contains("Marxist-Leninist"))
    }
}