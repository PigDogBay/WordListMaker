package com.mpdbailey.utils

import com.mpdbailey.utils.RomanNumerals
import org.junit.Test

import org.junit.Assert.*

class RomanNumeralsTest {

    @Test
    fun toRoman1() {
        val target = RomanNumerals()
        assertEquals("MMXX",target.toRoman(2020))
        assertEquals("MCMLXXXIV",target.toRoman(1984))
        assertEquals("MCMXCIX",target.toRoman(1999))
    }
    @Test
    fun toRoman2() {
        val target = RomanNumerals()
        assertEquals("III",target.toRoman(3))
        assertEquals("VIII",target.toRoman(8))
        assertEquals("XIII",target.toRoman(13))
        assertEquals("XV",target.toRoman(15))
        assertEquals("XVII",target.toRoman(17))
        assertEquals("XVIII",target.toRoman(18))
        assertEquals("XX",target.toRoman(20))
        assertEquals("XXI",target.toRoman(21))
        assertEquals("XXII",target.toRoman(22))
        assertEquals("XXIII",target.toRoman(23))
        assertEquals("XXIV",target.toRoman(24))
        assertEquals("XXV",target.toRoman(25))
        assertEquals("XXVI",target.toRoman(26))
        assertEquals("XXVII",target.toRoman(27))
        assertEquals("XXVIII",target.toRoman(28))
        assertEquals("XXIX",target.toRoman(29))
        assertEquals("XXX",target.toRoman(30))
    }
}