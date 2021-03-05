package com.mpdbailey.scowl

import com.mpdbailey.utils.removeAccents
import java.io.File

class Ukacd(private val filename : String) {
    //À-ÖØ-öø-ÿ
//    val regexPhrases = Regex("[àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅåÆæœ]")
    val regexPhrases = Regex("[À-ÖØ-öø-ÿ]")
    val regexIllegal = Regex("[^a-z0-9]")
    fun createWordList(){
        val list = File(filename)
            .readLines(Charsets.ISO_8859_1)
            .map{it.removeAccents()}
            .filter { it.contains(regexPhrases) }

        list.forEach{println(it)}
        println("Count ${list.count()}")
    }
}