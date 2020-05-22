/**
 * MV-Util
 * Copyright (C) 2020 Mariell Hoversholm, Nahuel Dolores
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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