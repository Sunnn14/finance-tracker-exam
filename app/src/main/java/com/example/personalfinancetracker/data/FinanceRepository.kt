
package com.example.personalfinancetracker.data

import com.example.personalfinancetracker.data.local.Transaction
import com.example.personalfinancetracker.data.local.TransactionDao
import com.example.personalfinancetracker.data.settings.SettingsDataStore


class FinanceRepository(
 private val dao: TransactionDao,
 private val settings: SettingsDataStore
) {

 // 1. Room Database-аас бүх бүртгэлийг авах
 val transactions = dao.observeAll()

 // 2. DataStore-оос сонгосон шүүлтүүрийг авах
 val filter = settings.filter

 // 3. Шүүлтүүрийн тохиргоог хадгалах
 suspend fun setFilter(value: Int) {
  settings.saveFilter(value)
 }

 // 4. Шинэ бүртгэл нэмэх эсвэл байгаа бүртгэлийг засах
 suspend fun save(item: Transaction) {

  if (item.id == 0L) {

   // Шинэ бүртгэл нэмэх
   dao.insert(item)

  } else {

   // Байгаа бүртгэлийг шинэчлэх
   dao.update(item)
  }
 }

 // 5. Бүртгэл устгах
 suspend fun delete(item: Transaction) {
  dao.delete(item)
 }
}
