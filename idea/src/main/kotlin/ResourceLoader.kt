class ResourceLoader {
    fun load(resourceName : String) : List<String> = this.javaClass.getResourceAsStream(resourceName).bufferedReader().readLines()
}