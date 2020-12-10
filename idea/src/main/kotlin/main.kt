const val OUT_FILENAME = "../../out/idea.txt"

fun main(args: Array<String>) {
    val words = createScowlWordList()
    println("Count: ${words.count()}")
    words.saveWordList(OUT_FILENAME)
}
