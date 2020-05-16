package us.minevict.mvutil.common.ext

import com.google.common.reflect.TypeToken

@Suppress("UnstableApiUsage")
inline fun <reified T : Any> typeToken(): TypeToken<T> = object : TypeToken<T>() {}.wrap()