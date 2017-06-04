package src

object CatCommonFun {
    val spacing = "    "
    val idPattern = Regex("\\w{8}")
    val nonWordPattern = Regex("\\W")

    private fun strToList(src: String): List<String>? {
        if (src.contains("<span class=\"comment_date\">")) {
            val temp = src.split(CatCommonFun.nonWordPattern)
            if (temp.filter { it.matches(CatCommonFun.idPattern) }.isEmpty()) return null

            return temp.filter { it.matches(CatCommonFun.idPattern) }
        }
        return null
    }

    fun commonFilter(src: String, srcBuffer: ArrayList<String>): String? {
        srcBuffer.add(CatCommonFun.strToList(src)?.get(0)?.toUpperCase()?.replace(" ", "") ?: return null)
        srcBuffer.add(CatCommonFun.spacing)
        srcBuffer.add(src.split("<span class=\"comment_date\">")[1].split("<")[0].toUpperCase())
        return srcBuffer.reduce { x, y -> x + y }
    }

}