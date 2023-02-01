package com.mpdbailey.nabu

import java.io.File
import java.sql.DriverManager

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

    private val insertLookupSql = "INSERT INTO lookup(word,ids) VALUES(?,?)"
    private val insertSynonymsSql = "INSERT INTO synonymSet(id,synonyms,partOfSpeech,associatedIds,definitions) VALUES(?,?,?,?,?)"

    private val queryLookupSql = "SELECT ids FROM lookup WHERE word = ?"
    private val querySynonymSetSql = "SELECT synonyms,associatedIds,partOfSpeech,definitions FROM synonymSet WHERE id = ?"

    fun create() {
        File(filename).delete()
        val connection = DriverManager.getConnection(url)
        val meta = connection.metaData
        println("Driver name ${meta.driverName}")

        val statement = connection.createStatement()
        statement.execute(createIndexTableSql)
        statement.execute(createDataTableSql)
        statement.close()
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

    fun query(word : String) : List<String> {
        val connection = DriverManager.getConnection(url)
        val prepStat = connection.prepareStatement(queryLookupSql)
        prepStat.setString(1, word)
        val resultSet = prepStat.executeQuery()
        val results = if (resultSet.next()){
            CompressedIndex.unflatten(resultSet.getString("ids"))
        } else { emptyList<String>()}
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
}