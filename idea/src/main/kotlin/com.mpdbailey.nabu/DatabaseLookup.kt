package com.mpdbailey.nabu

class DatabaseLookup(filename : String) {
    private val database = Database(filename)
    private fun lookup(ids : List<String>) : List<SynonymSet>{
        val direct = ids.mapNotNull {database.querySynonymSet(it)}
        val associated = direct
            .flatMap { it.associatedIndices }
            .mapNotNull {database.querySynonymSet(it)}
        return direct+associated
    }

    fun search(word : String) : List<String> {
        val id = database.query(word)
        return lookup(id)
            .flatMap { it.words }
            .distinct()
    }
}