package org.bandev.buddhaquotes.core

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import java.util.*

class Languages {

    /**
     * Sets the activity's langauge to the one selected by user
     * @param [context] context of activity (Context)
     * @author jack.txt
     * @added [1008] v1.5.0 - 30/10/2020
     */

    fun setLanguage(context: Context){
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val languageToLoad = sharedPrefs.getString("app_language", "en")

        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(
            config,
            context.resources.displayMetrics
        )
    }
}