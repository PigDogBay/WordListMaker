package com.mpdbailey.utils

class ResourceLoader {
    fun load(resourceName : String) : List<String> = this.javaClass.getResourceAsStream(resourceName).use {
        it?.bufferedReader()?.readLines() ?: emptyList()
    }

    fun loadText(resourceName : String) : String = this.javaClass.getResourceAsStream(resourceName).use {
        it?.bufferedReader()?.readText() ?: ""
    }
}