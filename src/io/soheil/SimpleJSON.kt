package io.soheil

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * GitHub: https://github.com/soheily98/simple-json/
 */
class SimpleJSON(payload: Any? = null) {
    var map: Map<String, SimpleJSON>? = null
    var list: List<SimpleJSON>? = null

    var finalValue: Any? = null

    init {
        if (payload != null) {
            if (payload is String) {
                if (payload.startsWith("{"))
                    map = toMap(JSONObject(payload))

                if (payload.startsWith("["))
                    list = toList(JSONArray(payload))

                finalValue = payload
            } else if (payload is List<*>) {
                list = payload as List<SimpleJSON>?
            } else if (payload is Map<*, *>) {
                map = payload as Map<String, SimpleJSON>?
            } else {
                finalValue = payload
            }
        }
    }

    fun length() = list?.size ?: 0

    fun latest() = list?.get(list?.lastIndex ?: 0) ?: SimpleJSON()

    fun toInt(fallbackValue: Int = 0): Int {
        if (finalValue is Int)
            return finalValue as Int

        return fallbackValue
    }

    fun toBoolean(fallbackValue: Boolean = false): Boolean {
        if (finalValue is Boolean)
            return finalValue as Boolean

        return fallbackValue
    }

    override fun toString() = toString("")
    fun toString(fallbackValue: String = "") = finalValue?.toString() ?: fallbackValue

    @Throws(JSONException::class)
    private fun toMap(obj: JSONObject): HashMap<String, SimpleJSON> {
        val map = HashMap<String, SimpleJSON>()

        val keysItr = obj.keys()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            var value = obj.get(key)

            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            map.put(key, SimpleJSON(value))
        }
        return map
    }

    @Throws(JSONException::class)
    private fun toList(array: JSONArray): List<SimpleJSON>? {
        val list = ArrayList<SimpleJSON>()
        for (i in 0 until array.length()) {
            var value = array.get(i)
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            list.add(SimpleJSON(value))
        }
        return list
    }

    operator fun get(indexOrKey: Any): SimpleJSON {
        if (indexOrKey is Int && list != null && (indexOrKey < list?.size ?: 0))
            return list!![indexOrKey]

        if (indexOrKey is String && map != null && (map?.containsKey(indexOrKey) == true))
            return map!![indexOrKey]!!

        return SimpleJSON()
    }

    operator fun set(indexOrKey: Any, value: Any) {
        //todo Will be implemented, soon.
        throw Exception("Currently set operation is not supported on io.soheil.SimpleJSON objects.")
    }
}