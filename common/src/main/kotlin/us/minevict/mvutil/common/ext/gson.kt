@file:Suppress("UnstableApiUsage")

package us.minevict.mvutil.common.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.stream.JsonReader
import java.io.Reader

val simpleGson by lazy {
    Gson()
}

val prettyGson by lazy {
    GsonBuilder()
        .setPrettyPrinting()
        .create()!!
}

inline fun <reified T : Any> Gson.fromJson(reader: Reader) =
    this.fromJson(reader, typeToken<T>().type) as T

inline fun <reified T : Any> Gson.fromJson(reader: JsonReader) =
    this.fromJson(reader, typeToken<T>().type) as T

inline fun <reified T : Any> Gson.fromJson(str: String) =
    this.fromJson(str, typeToken<T>().type) as T

inline fun <reified T : Any> Gson.fromJson(elem: JsonElement) =
    this.fromJson(elem, typeToken<T>().type) as T