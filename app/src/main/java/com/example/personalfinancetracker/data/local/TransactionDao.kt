
package com.example.personalfinancetracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow


// 1. Room Database-ийн DAO interface
@Dao
interface TransactionDao {

 // 2. Бүх орлого, зарлагын бүртгэлийг унших
 // ID-аар буурах дарааллаар эрэмбэлнэ
 @Query(
  "SELECT * FROM transactions ORDER BY id DESC"
 )
 fun observeAll(): Flow<List<Transaction>>


 // 3. Шинэ бүртгэл нэмэх (CREATE)
 @Insert
 suspend fun insert(
  item: Transaction
 )


 // 4. Бүртгэл засах (UPDATE)
 @Update
 suspend fun update(
  item: Transaction
 )


 // 5. Бүртгэл устгах (DELETE)
 @Delete
 suspend fun delete(
  item: Transaction
 )
}
