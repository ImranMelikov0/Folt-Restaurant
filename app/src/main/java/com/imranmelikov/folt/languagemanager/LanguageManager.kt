package com.imranmelikov.folt.languagemanager

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {

    fun updateLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}