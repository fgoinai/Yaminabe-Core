package src

interface ICategory {
    val url: String
    var tag: String
    fun filter(src: String): String?
}
