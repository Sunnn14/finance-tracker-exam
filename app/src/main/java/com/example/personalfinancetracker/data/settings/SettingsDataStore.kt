
package com.example.personalfinancetracker.data.settings

import android.content.Context

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// 1. Preferences DataStore үүсгэх
private val Context.settingsStore by preferencesDataStore(
 name = "settings"
)


class SettingsDataStore(
 private val context: Context
) {

 // 2. Шүүлтүүрийн утгыг хадгалах түлхүүр
 private val filterKey = intPreferencesKey("filter")

 // 3. Хадгалагдсан шүүлтүүрийг Flow хэлбэрээр унших
 val filter: Flow<Int> = context.settingsStore.data.map {
   preferences ->

  // Утга байхгүй бол анхны тохиргоо 0
  preferences[filterKey] ?: 0
 }

 // 4. Хэрэглэгчийн сонгосон шүүлтүүрийг хадгалах
 suspend fun saveFilter(value: Int) {
  context.settingsStore.edit { preferences ->
   preferences[filterKey] = value
  }
 }
}
