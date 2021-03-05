package com.mpdbailey.utils

import org.junit.Test

import org.junit.Assert.*


internal class ExtensionsTest {

    @Test
    fun removeAccents1() {
        assertEquals("aaaaa", "àáâäå".removeAccents())
        assertEquals("c", "ç".removeAccents())
        assertEquals("eeee", "èéêë".removeAccents())
        assertEquals("iii", "íîï".removeAccents())
        assertEquals("n", "ñ".removeAccents())
        assertEquals("oooo", "óôöø".removeAccents())
        assertEquals("uuuu", "ùúûü".removeAccents())
    }
    @Test
    fun removeAccents2() {
        assertEquals("mary had a little lamb", "máry hád ä littlê lämb".removeAccents())
        assertEquals("all's well that ends well", "all's well that ends well".removeAccents())
    }
    /**
     * Accents found in UKACD
     */
    @Test
    fun removeAccents3(){
        assertEquals("AAoiIOEEaEoU","ÀÅòìÎÖÉÉãÉòÜ".removeAccents())
    }
    /**
     * From
     * https://stackoverflow.com/questions/20690499/concrete-javascript-regex-for-accented-characters-diacritics
     * àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇßØøÅå
     */
    @Test
    fun removeAccents4(){
        assertEquals("aeiouAEIOUaeiouyAEIOUYaeiouAEIOUanoANOaeiouyAEIOUYcCOoAa",
            "àèìòùÀÈÌÒÙáéíóúýÁÉÍÓÚÝâêîôûÂÊÎÔÛãñõÃÑÕäëïöüÿÄËÏÖÜŸçÇØøÅå".removeAccents())
    }

}