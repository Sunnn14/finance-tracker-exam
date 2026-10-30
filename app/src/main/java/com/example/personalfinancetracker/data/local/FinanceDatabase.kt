
package com.example.personalfinancetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


// 1. Room Database-ийн тохиргоо
@Database(
    entities = [Transaction::class],
    version = 1,
    exportSchema = false
)

// 2. Өгөгдлийн сангийн үндсэн класс
abstract class FinanceDatabase : RoomDatabase() {

    // 3. TransactionDao-д хандах функц
    abstract fun dao(): TransactionDao
}
