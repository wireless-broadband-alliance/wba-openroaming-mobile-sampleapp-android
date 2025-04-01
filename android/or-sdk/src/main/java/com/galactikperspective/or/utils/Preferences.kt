package com.galactikperspective.or.utils

import android.content.Context
import com.google.android.play.core.ktx.BuildConfig
import androidx.core.content.edit

//
//  Preferences.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class Preferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun saveLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}
