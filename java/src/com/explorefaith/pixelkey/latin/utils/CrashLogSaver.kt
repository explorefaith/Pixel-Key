/*
 * Copyright (c) 2026 Eyasin Arafat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.explorefaith.pixelkey.latin.utils

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Saves the last uncaught crash to filesDir so the user can share it with the developer
 * (Settings -> Help -> "Send crash report") even without adb. Installed AFTER ACRA init
 * so the saved handler delegates to ACRA's dialog first.
 */
object CrashLogSaver {
    private const val TAG = "CrashLogSaver"
    private const val FILE_NAME = "last_crash.txt"
    private const val MAX_REPORT_CHARS = 8000

    fun install(context: Context) {
        val appContext = context.applicationContext
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            runCatching { save(appContext, thread, throwable) }
            previous?.uncaughtException(thread, throwable)
        }
    }

    private fun save(context: Context, thread: Thread, throwable: Throwable) {
        val text = buildString {
            append("Time: ")
                .append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()))
            append("\nVersion: ")
            runCatching {
                append(context.packageManager.getPackageInfo(context.packageName, 0).versionName)
            }
            append("\nDevice: ").append(Build.MANUFACTURER).append(' ').append(Build.MODEL)
            append(" (Android ").append(Build.VERSION.RELEASE).append(", API ").append(Build.VERSION.SDK_INT).append(")")
            append("\nThread: ").append(thread.name)
            append("\n\n")
            append(Log.getStackTraceString(throwable))
        }
        File(context.filesDir, FILE_NAME).writeText(text.take(MAX_REPORT_CHARS))
    }

    /** @return the last crash report (truncated for sharing), or null when none. */
    fun lastCrashText(context: Context): String? {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return null
        return runCatching { file.readText().take(3500) }.getOrNull()
    }
}
