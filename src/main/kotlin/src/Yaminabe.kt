package src

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat

class Yaminabe {
    private val maxListLen = 5
    private val list = ArrayList<String>()
    private val dateTag = "<span class=\"comment_date\">"

    fun getList(cat: ICategory): ArrayList<String> {
        val tmp = getBoardUrl(cat.url)
        val reader = InputStreamReader((URL(tmp).openConnection() as HttpURLConnection).inputStream, Charset.forName("EUC-JP")).buffered()
        stringHandling(cat, reader)
        return list.reversed() as ArrayList<String>
    }

    @Throws(IOException::class) private fun getBoardUrl(url: String): String {
        val key = "コメントページを参照"
        return InputStreamReader((URL(url).openConnection() as HttpURLConnection).inputStream, Charset.forName("EUC-JP")).buffered().lines().parallel().filter { it.contains(key) }.findFirst().get().split("\"")[1]
    }

    private fun stringHandling(cat: ICategory, reader: BufferedReader) {
        reader.lines().parallel().forEach {
            val encodedText = encodingToUtf8(it)
            when {
                isTargetEmpty(it) || !encodedText.contains("<span class=\"comment_date\">") || cat.filter(encodedText) == null -> return@forEach
            }
            listSortAndFilter(list, encodedText)
        }
    }

    @Synchronized private fun listSortAndFilter(list: ArrayList<String>, src: String) {
        list.add(src)
        while (list.size > maxListLen) {
            sortListWithDate(list, dateTag)
            list.removeAt(0)
        }
    }

    companion object {
        private fun sortListWithDate(list: ArrayList<String>, dateTag: String) {
            list.sortWith(compareBy({
                SimpleDateFormat("yyyy-MM-dd").parse(it.split(dateTag)[1].split(" ").filter { it.contains(Regex("\\d{4}-\\d{2}-\\d{2}")) }[0])
            }, {
                SimpleDateFormat("HH:mm:ss").parse(it.split(dateTag)[1].split(" ").filter { it.contains(Regex("\\d{2}:\\d{2}:\\d{2}")) }[0])
            }))
        }
    }

    private fun encodingToUtf8(src: String) = java.lang.String(src.toByteArray(), "UTF-8") as String

    private fun isTargetEmpty(src: String) = src.split(CatCommonFun.nonWordPattern).filter { it.matches(CatCommonFun.idPattern) }.isEmpty()

}
