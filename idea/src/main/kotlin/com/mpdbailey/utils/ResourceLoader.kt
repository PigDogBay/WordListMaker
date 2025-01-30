package com.mpdbailey.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mpdbailey.nabu.DefinitionData

class ResourceLoader {
    fun load(resourceName : String) : List<String> = this.javaClass.getResourceAsStream(resourceName).use {
        it?.bufferedReader()?.readLines() ?: emptyList()
    }

    fun loadText(resourceName : String) : String = this.javaClass.getResourceAsStream(resourceName).use {
        it?.bufferedReader()?.readText() ?: ""
    }

    /**
     * Loads DefinitionData from a JSON file, see resources/ExtraDefinitions.json
     */
    fun loadDefinitionGson(path : String) : List<DefinitionData>{
        val gsonText = loadText(path)
        val dataDefinitionDataType = object : TypeToken<List<DefinitionData>>(){}.type
        val data :List<DefinitionData> = Gson().fromJson(gsonText, dataDefinitionDataType )
        return data
    }
}