package com.mpdbailey.nabu

import java.io.File
import java.sql.DriverManager

/**
 *
 * Database overview
 *
 * Lookup Table
 *     Word - The word to look up its synonyms and definition
 *     Ids - List of Ids, each Id is 3 characters and is used to query the Synonym Set table
 *
 * Synonym Set Table
 *     Id - Unique (Primary Key) 3 char code
 *     Synonym - list of related words (synonyms) separated by ','
 *     Part of speech - Noun, Verb, Adjective or Adverb
 *     Associated Ids - List of Ids, each Id is a key to another related synonym set
 *     Definitions - List of definitions separated by '; '
 *
 * The lookup table is used to find a list of Ids for a word,
 * these Ids are then used to find the definitions and synonyms in the synonymSet table
 *
 * A synonym set may also reference related synonym sets as associated ids, these associated ids
 * can be queried in the synonym set table to find more related words which are useful for finding
 * thesaurus results.
 *
 *
 * Exceptions Table
 *     Inflected - Inflected word, e.g. axes
 *     Bases - list of possible base words, e.g. ax,axis

 * Inflected words, e.g. boxful, boxing, boxes, boxed need to converted to a base form (box),
 * the lookup table only contains base forms of words which keeps the size of the DB down.
 *
 * Some words may have more base forms, for example axes could be ax or axis.
 * The exception table is used to find base forms for words that could not be handled by the Morphology algorithm.
 *
 */
class Database(private val filename : String) {
    private val url = "jdbc:sqlite:$filename"
    private val createIndexTableSql = """CREATE TABLE IF NOT EXISTS lookup (
	word text NOT NULL PRIMARY KEY,
	ids text NOT NULL
);"""
    private val createDataTableSql = """CREATE TABLE IF NOT EXISTS synonymSet (
	id text NOT NULL PRIMARY KEY,
    synonyms text NOT NULL,
    partOfSpeech text NOT NULL,
    associatedIds text NOT NULL,
    definitions text NOT NULL
);"""
    private val createExceptionsTableSql = """CREATE TABLE IF NOT EXISTS exceptions (
	inflected text NOT NULL PRIMARY KEY,
	bases text NOT NULL
);"""

    private val insertLookupSql = "INSERT INTO lookup(word,ids) VALUES(?,?)"
    private val insertSynonymsSql = "INSERT INTO synonymSet(id,synonyms,partOfSpeech,associatedIds,definitions) VALUES(?,?,?,?,?)"
    private val insertExceptionsSql = "INSERT INTO exceptions(inflected,bases) VALUES(?,?)"

    private val updateLookupSql = "UPDATE lookup SET ids = ? where word = ?"

    private val queryLookupSql = "SELECT ids FROM lookup WHERE word = ?"
    private val querySynonymSetSql = "SELECT synonyms,associatedIds,partOfSpeech,definitions FROM synonymSet WHERE id = ?"
    private val queryExceptionSql = "SELECT bases FROM exceptions WHERE inflected = ?"

    val lookupCountSql = "SELECT COUNT(*) FROM lookup"
    val synonymSetCountSql = "SELECT COUNT(*) FROM synonymSet"
    val exceptionsCountSql = "SELECT COUNT(*) FROM exceptions"

    fun create() {
        File(filename).delete()
        val connection = DriverManager.getConnection(url)
        val meta = connection.metaData
        println("Driver name ${meta.driverName}")

        val statement = connection.createStatement()
        statement.execute(createIndexTableSql)
        statement.execute(createDataTableSql)
        statement.execute(createExceptionsTableSql)
        statement.close()
        connection.close()
    }

    ///Replace the ids for the index
    fun update(index : SynonymIndex){
        val connection = DriverManager.getConnection(url)
        connection.autoCommit = false
        val prepStat = connection.prepareStatement(updateLookupSql)
        prepStat.setString(1, CompressedIndex.flatten(index.indices))
        prepStat.setString(2, index.word)
        prepStat.executeUpdate()
        connection.commit()
        prepStat.close()
        connection.close()
    }

    fun insert(indices : List<SynonymIndex>){
        val connection = DriverManager.getConnection(url)
        connection.autoCommit = false
        val prepStat = connection.prepareStatement(insertLookupSql)
        indices.forEach {
            prepStat.setString(1, it.word)
            //OK to use , as separator but can be optimised out as indices are same length
            prepStat.setString(2, CompressedIndex.flatten(it.indices))
            prepStat.executeUpdate()
        }
        connection.commit()
        prepStat.close()
        connection.close()
    }

    fun insertSynonyms(synonyms : List<SynonymSet>){
        val connection = DriverManager.getConnection(url)
        connection.autoCommit = false
        val prepStat = connection.prepareStatement(insertSynonymsSql)
        synonyms.forEach {
            prepStat.setString(1, it.index)
            prepStat.setString(2, it.words.joinToString(","))
            prepStat.setString(3, it.partOfSpeech.letter.toString())
            prepStat.setString(4, CompressedIndex.flatten(it.associatedIndices))
            prepStat.setString(5, it.definitions)
            prepStat.executeUpdate()
        }
        connection.commit()
        prepStat.close()
        connection.close()
    }

    fun insertExceptions(exceptions : List<ExceptionItem>){
        val connection = DriverManager.getConnection(url)
        connection.autoCommit = false
        val prepStat = connection.prepareStatement(insertExceptionsSql)
        exceptions.forEach {
            prepStat.setString(1, it.inflected)
            prepStat.setString(2, it.baseForms.joinToString(","))
            prepStat.executeUpdate()
        }
        connection.commit()
        prepStat.close()
        connection.close()
    }

    fun query(word : String) : List<String> {
        val connection = DriverManager.getConnection(url)
        val prepStat = connection.prepareStatement(queryLookupSql)
        prepStat.setString(1, word)
        val resultSet = prepStat.executeQuery()
        val results = if (resultSet.next()){
            CompressedIndex.unflatten(resultSet.getString("ids"))
        } else { emptyList()}
        prepStat.close()
        connection.close()
        return results
    }

    fun querySynonymSet(id : String) : SynonymSet? {
        val connection = DriverManager.getConnection(url)
        val prepStat = connection.prepareStatement(querySynonymSetSql)
        prepStat.setString(1, id)
        val resultSet = prepStat.executeQuery()
        val results = if (resultSet.next()){
            SynonymSet(id,
                resultSet.getString("synonyms").split(','),
                CompressedIndex.unflatten(resultSet.getString("associatedIds")),
                PartOfSpeech.from(resultSet.getString("partOfSpeech")),
                resultSet.getString("definitions")
            )
        } else {null}
        prepStat.close()
        connection.close()
        return results
    }

    fun queryExceptions(inflected : String) : List<String> {
        val connection = DriverManager.getConnection(url)
        val prepStat = connection.prepareStatement(queryExceptionSql)
        prepStat.setString(1, inflected)
        val resultSet = prepStat.executeQuery()
        val results = if (resultSet.next()){
            resultSet.getString("bases").split(',')
        } else { emptyList()}
        prepStat.close()
        connection.close()
        return results
    }

    fun count(countSql : String) : Int {
        val connection = DriverManager.getConnection(url)
        val prepStat = connection.prepareStatement(countSql)
        val resultSet = prepStat.executeQuery()
        val count = if (resultSet.next()){
            resultSet.getInt(1)
        } else { 0}
        prepStat.close()
        connection.close()
        return count
    }
}