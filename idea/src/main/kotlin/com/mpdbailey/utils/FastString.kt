package com.mpdbailey.utils

class FastString {
    private val inBuffer = CharArray(64)
    private val outBuffer = CharArray(64)

    /**
     * Optimised code to remove spaces, hyphens and any punctuation from a string
     */
    fun strip(raw: String) : String {
        val l = raw.length
        raw.toCharArray(inBuffer, 0, 0, l)
        var j = 0
        for (i in 0 until l){
            val c = inBuffer[i]
            if (c in 'a'..'z' || c in 'A'..'Z'){
                outBuffer[j++] = c
            }
        }
        return if (j==l) raw else outBuffer.concatToString(0,j)
    }

}