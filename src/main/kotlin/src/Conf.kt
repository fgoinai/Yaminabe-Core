package src

import java.io.File

class Conf(val path: String) {
    private val ini = org.ini4j.Ini(File(path))

    fun getIntervalConf(): Long {
        return ini.get("Other", "RenewInterval", Long::class.java)
    }

    fun getUrlConf(): HashMap<String, String> {
        val ret = HashMap<String, String>()
        getIndex.forEach { ret.put(it, ini.get("URL", it)) }
        return ret
    }

    fun getUrl(tag: String): String {
        return ini.get("URL", tag)
    }

    fun setUrl(tag: String, url: String) {
        ini.put("URL", tag, url)
    }

    private val getIndex = arrayOf("大巴", "四天司", "方陣HL", "通常", "召喚終突", "6人HL")
}
