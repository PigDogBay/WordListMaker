import org.junit.Test

import org.junit.Assert.*

class ValidationKtTest {

    @Test
    fun containsIllegalChar1() {
        assertFalse(containsIllegalChar("abcdefghijklmnopqrstuvwxyx"))
    }
    @Test
    fun containsIllegalChar2() {
        assertTrue(containsIllegalChar("no space"))
    }
    @Test
    fun containsIllegalChar3() {
        assertTrue(containsIllegalChar("it's"))
    }
    @Test
    fun containsIllegalChar4() {
        assertTrue(containsIllegalChar("Uppercase"))
    }
    @Test
    fun containsIllegalChar5() {
        assertTrue(containsIllegalChar("hyphenated-words"))
    }
    @Test
    fun containsIllegalChar6() {
        assertTrue(containsIllegalChar("arête"))
    }
}