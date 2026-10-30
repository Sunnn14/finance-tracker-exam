package com.example.personalfinancetracker.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
                       val id: Long = 0,
                       val type: String,
                       val name: String,
                       val amount: Long,
                       val date: String
)
