package com.mpdbailey.nabu

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mpdbailey.utils.ResourceLoader


/**
 * Synonym set can have associated words
 * These words will be looked up in the lookUp table
 * index is to select the relevant 'synonym index' from the list of ids
 * The synonym index points to another synonym in the synonymSet table
 */
data class AssociatedWord (
    val word : String,
    val index : Int
)

/**
 * Class to represent the data needed to add a new definition to the Nabu database
 * word: The word that is being defined and is used to query the lookUp table
 * partOfSpeech: What type is the word
 * definition: Separate multiple definition strings using '; '
 * words: List of closely related words that are also found in the lookUp table, these will appear in the synonym list for the word
 * associatedWords: The word may also be related to other definitions, instead of specifying the synonym id for the definition
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
 * Loads DefinitionData from a JSON file, see resources/ExtraDefinitions.json
 */
fun loadDefinitionGson(path : String) : List<DefinitionData>{
    val gsonText = ResourceLoader().loadText(path)
    val dataDefinitionDataType = object : TypeToken<List<DefinitionData>>(){}.type
    val data :List<DefinitionData> = Gson().fromJson(gsonText, dataDefinitionDataType )
    return data
}

/**
 * Adds DefintionData to the Nabu database
 * Also some helper functions (displayIds) to find which index to use for AssociatedWords
 */
class DbAdditions(dbFileName : String) {
    val db = Database(dbFileName)

    /**
     * data: - list of definitions to add
     * compressedIndex: - Used to generate unique ids for each new definition
     */
    fun addAll(data : List<DefinitionData>, compressedIndex: CompressedIndex) {
        data.forEach {
            it.synIndex = compressedIndex.next()
            add(it)
        }
    }

    fun add(definitionData : DefinitionData){
        val synonymSet = toSynonymSet(definitionData)
        //Add the definition
        insert(synonymSet)
        //Update the lookup table to point to the new definition
        updateIndex(definitionData.word,synonymSet)
    }

    private fun toSynonymSet(definitionData: DefinitionData) :SynonymSet{
        return SynonymSet(definitionData.synIndex,
            definitionData.words,
            //Find the definition index for the associated word
            definitionData.associatedWords.map { db.query(it.word)[it.index] },
            definitionData.partOfSpeech,
            definitionData.definition
        )
    }

    private fun insert(synonymSet: SynonymSet){
        //Don't add it if already in the database
        val result = db.querySynonymSet(synonymSet.index)
        if (result!=null) {
            println("Warning! Synonym Set already exists in the database: ${synonymSet.index}")
            return
        }
        db.insertSynonyms(listOf(synonymSet))
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
