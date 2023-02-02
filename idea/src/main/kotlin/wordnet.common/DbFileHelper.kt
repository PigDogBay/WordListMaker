package wordnet.common

import java.nio.file.Path

/**
 * Code to load the WordNet index/data files
 */
class DbFileHelper {
    /**
     * The index and data files all have the same copyright notice at the top of the file
     * This needs to be ignored when parsing the file
     */
    val copyrightLineCount = 29

    private fun getWordNetResourceName(db : DbType, pos : DbPartOfSpeech) : String = "/wordnet/${db.filename}.${pos.fileExtension}"

    /**
     * Loads the index file for the specified part of speech
     * Each line in the index file is parsed into an wordnet.common.Index object
     *
     * Note that the file index.sense is a different format to the other index files
     * and seems to be a combined list of the index files.
     */
    fun loadIndices(dbPartOfSpeech: DbPartOfSpeech) : List<Index> =
        this.javaClass.getResourceAsStream(getWordNetResourceName(DbType.Index,dbPartOfSpeech))
            .bufferedReader(Charsets.UTF_8)
            .readLines()
            .drop(copyrightLineCount)
            .map { Index(it,0) }

    /**
     * Loads the data file for the specified part of speech
     * Each line in the data file is parsed into a wordnet.common.Definition object
     */
    fun loadDefinitions(dbPartOfSpeech: DbPartOfSpeech) : List<Definition> =
        this.javaClass.getResourceAsStream(getWordNetResourceName(DbType.Data,dbPartOfSpeech))
            .bufferedReader(Charsets.UTF_8)
            .readLines()
            .drop(copyrightLineCount)
            .map { Definition("",it) }

    /**
     * Returns the absolute file path to the index/data file in the app's resource folder
     */
    fun getDbFilePath(db : DbType, pos : DbPartOfSpeech) : String {
        val url = this.javaClass.getResource("/wordnet/${db.filename}.${pos.fileExtension}").toURI()
        return Path.of(url).toAbsolutePath().toString()
    }

    /**
     * Returns a list of the noun, verb, adjective and adverb index files
     */
    fun getAllIndexFiles() : List<String> {
        return listOf(
            getDbFilePath(DbType.Index,DbPartOfSpeech.Adj),
            getDbFilePath(DbType.Index,DbPartOfSpeech.Adv),
            getDbFilePath(DbType.Index,DbPartOfSpeech.Noun),
            getDbFilePath(DbType.Index,DbPartOfSpeech.Verb),
        )
    }

    /**
     * Returns a list of the noun, verb, adjective and adverb data files
     */
    fun getAllDataFiles() : List<String> {
        return listOf(
            getDbFilePath(DbType.Data,DbPartOfSpeech.Adj),
            getDbFilePath(DbType.Data,DbPartOfSpeech.Adv),
            getDbFilePath(DbType.Data,DbPartOfSpeech.Noun),
            getDbFilePath(DbType.Data,DbPartOfSpeech.Verb),
        )
    }
}