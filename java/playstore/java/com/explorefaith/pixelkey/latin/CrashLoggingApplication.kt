package com.explorefaith.pixelkey.latin

import android.app.Application
import androidx.datastore.preferences.core.Preferences

class CrashLoggingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Keep a local copy of the last crash so the user can share it from
        // Settings -> Help ("Send crash report to developer").
        com.explorefaith.pixelkey.latin.utils.CrashLogSaver.install(this)
    }

    companion object {
        fun logPreferences(preferences: Preferences) {

        }
    }
}