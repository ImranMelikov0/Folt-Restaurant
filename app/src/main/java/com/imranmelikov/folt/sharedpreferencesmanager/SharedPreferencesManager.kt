package com.imranmelikov.folt.sharedpreferencesmanager

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(val sharedPreferences: SharedPreferences) {
     val gson = Gson()
    fun <T> save(key: String, value: T) {
        val editor = sharedPreferences.edit()
        val jsonValue = gson.toJson(value)
        editor.putString(key, jsonValue)
        editor.apply()
    }

    inline fun <reified T> load(key: String, defaultValue: T): T {
        val jsonValue = sharedPreferences.getString(key, null)
        return if (jsonValue != null) {
            gson.fromJson(jsonValue, T::class.java)
        } else {
            defaultValue
        }
    }
}