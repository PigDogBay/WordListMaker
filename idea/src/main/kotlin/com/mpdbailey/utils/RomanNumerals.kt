package com.mpdbailey.utils

import java.util.*

/**
 * Based on
 * https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java
 */
class RomanNumerals {
    private val numeralsMap = TreeMap<Int, String>()
    init {
        numeralsMap[1000] = "M"
        numeralsMap[900] = "CM"
        numeralsMap[500] = "D"
        numeralsMap[400] = "CD"
        numeralsMap[100] = "C"
        numeralsMap[90] = "XC"
        numeralsMap[50] = "L"
        numeralsMap[40] = "XL"
        numeralsMap[10] = "X"
        numeralsMap[9] = "IX"
        numeralsMap[5] = "V"
        numeralsMap[4] = "IV"
        numeralsMap[1] = "I"
    }

    fun toRoman(number : Int) : String {
        //Find the key less than or equal to the number
        val l = numeralsMap.floorKey(number)
        if (number == l){
            return numeralsMap[number]!!
        }
        return numeralsMap[l]!! + toRoman(number - l)
    }

}