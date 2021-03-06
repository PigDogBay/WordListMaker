package com.mpdbailey.utils

import java.io.File


/**
 * Using normalizer didn't work
 * https://stackoverflow.com/questions/51731574/removing-accents-and-diacritics-in-kotlin
 *
 * Accents from
 * https://stackoverflow.com/questions/20690499/concrete-javascript-regex-for-accented-characters-diacritics
 * àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅå [Ææœ -not implemented]
 */
fun String.removeAccents() : String = this
    .replace('Ý','Y')
    .replace('Ÿ','Y')
    .replace('ý','y')
    .replace('ÿ','y')
    .replace('À','A')
    .replace('È','E')
    .replace('Ì','I')
    .replace('Ò','O')
    .replace('Ù','U')
    .replace('Á','A')
    .replace('É','E')
    .replace('Í','I')
    .replace('Ó','O')
    .replace('Ú','U')
    .replace('Á','A')
    .replace('É','E')
    .replace('Í','I')
    .replace('Ó','O')
    .replace('Ú','U')
    .replace('Â','A')
    .replace('Ê','E')
    .replace('Î','I')
    .replace('Ô','O')
    .replace('Û','U')
    .replace('Ä','A')
    .replace('Ë','E')
    .replace('Ï','I')
    .replace('Ö','O')
    .replace('Ü','U')
    .replace('Ã','A')
    .replace('Ñ','N')
    .replace('Õ','O')
    .replace('Å','A')
    .replace('à','a')
    .replace('á','a')
    .replace('â','a')
    .replace('ä','a')
    .replace('å','a')
    .replace('ã','a')
    .replace('ç','c')
    .replace('Ç','C')
    .replace('è','e')
    .replace('é','e')
    .replace('ê','e')
    .replace('ë','e')
    .replace('í','i')
    .replace('ì','i')
    .replace('î','i')
    .replace('ï','i')
    .replace('ñ','n')
    .replace('ó','o')
    .replace('ò','o')
    .replace('ô','o')
    .replace('ö','o')
    .replace('õ','o')
    .replace('ø','o')
    .replace('Ø','O')
    .replace('ù','u')
    .replace('ú','u')
    .replace('û','u')
    .replace('ü','u')

fun String.removePunctuation() : String = this
    .replace(",","")
    .replace(",","")
    .replace("?","")
    .replace("!","")
    .replace("'","")
    .replace(".","")
    .replace(";","")
    .replace(":","")

fun String.removeWordSeparators() : String = this
    .replace(" ","")
    .replace("-","")

fun List<String>.saveWordList(filename: String) {
    File(filename).printWriter().use { out ->
        forEach { out.println(it) }
    }
}

fun String.unexpectedChar() : Boolean {
    this.forEach {
        if (!(it in 'a'..'z' ||  it == ' ' || it == '-')){
            return true
        }
    }
    return false
}