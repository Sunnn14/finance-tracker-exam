
package com.example.personalfinancetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.personalfinancetracker.data.FinanceRepository
import com.example.personalfinancetracker.data.local.Transaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FinanceViewModel(
 private val repository: FinanceRepository
) : ViewModel() {

 // 1. Room Database-аас бүртгэлүүдийг авах
 val transactions = repository.transactions.stateIn(
  scope = viewModelScope,
  started = SharingStarted.WhileSubscribed(5000),
  initialValue = emptyList()
 )

 // 2. DataStore-оос сонгосон шүүлтүүрийг авах
 val filter = repository.filter.stateIn(
  scope = viewModelScope,
  started = SharingStarted.WhileSubscribed(5000),
  initialValue = 0
 )

 // 3. Шүүлтүүрийн тохиргоог хадгалах
 fun setFilter(value: Int) = viewModelScope.launch {
  repository.setFilter(value)
 }

 // 4. Шинэ бүртгэл нэмэх эсвэл засах
 fun save(item: Transaction) = viewModelScope.launch {
  repository.save(item)
 }

 // 5. Бүртгэл устгах
 fun delete(item: Transaction) = viewModelScope.launch {
  repository.delete(item)
 }

 // 6. Repository-г ViewModel-д дамжуулах Factory
 companion object {

  fun factory(
   repo: FinanceRepository
  ): ViewModelProvider.Factory {

   return object : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
     modelClass: Class<T>
    ): T {
     return FinanceViewModel(repo) as T
    }
   }
  }
 }
}
