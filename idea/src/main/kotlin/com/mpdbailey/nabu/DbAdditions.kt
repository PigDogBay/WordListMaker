package com.mpdbailey.nabu

import com.google.gson.Gson

/**
 * A synonym set can have associated words
 * These words will be looked up in the lookUp table
 * index is to select the relevant 'synonym index' from the list of ids
 * The synonym index points to another synonym in the synonymSet table
 *
 * @param word The word to query the lookUp table
 * @param index Index of the synonym ID in the list of ids returned from the lookUp query
 */
data class AssociatedWord (
    val word : String,
    val index : Int
)

/**
 * Class to represent the data needed to add a new definition to the Nabu database
 *
 * @param word The word that is being defined and is used to query the lookUp table
 * @param partOfSpeech What type is the word
 * @param definition Separate multiple definition strings using '; '
 * @param words List of closely related words that are also found in the lookUp table, these will appear in the synonym list for the word
 * @param associatedWords The word may also be related to other definitions, instead of specifying the synonym id for the definition
 *  you specify the lookUp word to get a list of synonym ids and the index selects which id to use from the list.
 *
 *  synIndex: unique 3-char ID for the definition, it's a var as compressedIndex will set this automatically before database insertion
 */
data class DefinitionData (
    val word : String,
    val partOfSpeech: PartOfSpeech,
    val definition : String,
    val words : List<String>,
    val associatedWords : List<AssociatedWord>
) {
    var synIndex = "000"
}

/**
 * Adds DefinitionData to the Nabu database
 * Also some helper functions (displayIds) to find which index to use for AssociatedWords
 *
 * @param dbFileName filename of the Nabu database
 */
class DbAdditions(dbFileName : String) {
    private val db = Database(dbFileName)

    /**
     * Adds the definition to the Nabu database,
     *
     * Note that for the lookUp table, new words will be added and
     * existing words will be appended the new id to the list of synonym ids
     *
     * @param definitionData contains data for the word, including its synonyms, definitions and unique ID for the synonymSet table
     */
    fun add(definitionData : DefinitionData){
        val synonymSet = toSynonymSet(definitionData)
        //Add the definition
        db.insertSynonyms(listOf(synonymSet))
        //Update the lookup table to point to the new definition
        updateIndex(definitionData.word,synonymSet)
    }

    /**
     * Converts DefinitionData (JSON data objects) to SynonymSet (Nabu database objects
     * )
     * @param definitionData to be converted for use by Nabu DB
     */
    private fun toSynonymSet(definitionData: DefinitionData) :SynonymSet{
        return SynonymSet(definitionData.synIndex,
            definitionData.words,
            //Find the definition index for the associated word
            definitionData.associatedWords.map { db.query(it.word)[it.index] },
            definitionData.partOfSpeech,
            definitionData.definition
        )
    }

    /**
     * If a word is already defined in the lookUp table, then the
     * synonymSet index is added to the words id list
     *
     * If the word does not exist in the lookUp table, a new entry is created.
     */
    private fun updateIndex(word : String, synonymSet: SynonymSet){
        val result = db.query(word)
        if (result.isEmpty()){
            val newIndex = SynonymIndex(word, listOf(synonymSet.index))
            db.insert(listOf(newIndex))
            return
        }

        if (result.contains(synonymSet.index)){
            println("Warning! Index for $word already contains ${synonymSet.index}")
            return
        }
        val newIds = result + listOf(synonymSet.index)
        val synIndex = SynonymIndex(word,newIds)
        db.update(synIndex)
    }

    /**
     * Debugging, useful for finding associated words+index
     */
    fun displayIds(word : String){
        val list = db.query(word)
        println("Index for $word")
        list.forEach{
            val synSet = db.querySynonymSet(it)
            if (synSet!=null) {
                println("$it ${synSet.definitions}")
            }
        }
    }

    private fun display(synonymSet: SynonymSet){
        val words = synonymSet.words.reduce{acc,s-> "$acc $s" }
        println("Definition for ${synonymSet.index} (${synonymSet.partOfSpeech.name})")
        println(words)
        println(synonymSet.definitions)
        synonymSet.associatedIndices.forEach { ass ->
            val assSyn = db.querySynonymSet(ass)
            println("$ass ${assSyn?.definitions}")
        }
    }

    private fun displayGson(data : List<DefinitionData>){
        val gson = Gson()
        val json = gson.toJson(data)
        println("----JSON------\n$json\n-----JSON END------")
    }
}
