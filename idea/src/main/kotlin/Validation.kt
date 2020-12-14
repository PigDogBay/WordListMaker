import java.io.File

/**
 * Only a-z chars allowed in a word
 */
fun containsIllegalChar(word : String) : Boolean = word.any{it < 'a' || it > 'z'}


fun validate(filename : String) : Int {
    val invalidWords = File(filename)
        .readLines()
        .filter { containsIllegalChar(it) }

         invalidWords.forEach { println(it) }
        return invalidWords.count()
}