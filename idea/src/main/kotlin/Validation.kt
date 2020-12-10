/**
 * Only a-z chars allowed in a word
 */
fun containsIllegalChar(word : String) : Boolean = word.any{it < 'a' || it > 'z'}
