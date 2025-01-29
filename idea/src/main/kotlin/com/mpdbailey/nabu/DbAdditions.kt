package com.mpdbailey.nabu

class DbAdditions(dbFileName : String) {
    val db = Database(dbFileName)

    fun addExtraDefinitions(){
        //Add extra definition for 'bail'
        val bailSyn = bailSynSet()
        insert(bailSyn)
        //Update the index for 'bail'
        updateIndex("bail",bailSyn)
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
     * Debugging
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

    /***
     * Data
     */

    private fun bailSynSet() : SynonymSet {
        return SynonymSet("F00",
            listOf("bail","wicket","stump","cricket equipment","cricket"),
            listOf(
                findAssociatedIndex("stump",2),
                findAssociatedIndex("wicket",0),
                findAssociatedIndex("cricket equipment",0),
                findAssociatedIndex("cricket",1),
            ),
            PartOfSpeech.NOUN,
            "One of the two wooden crosspieces that rest on top of the stumps to form a wicket"
            )
    }

    private fun findAssociatedIndex(word : String, synIndex : Int) : String {
        return db.query(word)[synIndex]
    }


}