
package com.example.personalfinancetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.example.personalfinancetracker.data.local.Transaction
import com.example.personalfinancetracker.ui.FinanceViewModel

import java.time.LocalDate


@Composable
fun EditScreen(
 vm: FinanceViewModel,
 id: Long,
 onBack: () -> Unit
) {

 // 1. ViewModel-ээс бүх бүртгэлийг авах
 val all by vm.transactions.collectAsState()

 // 2. Засах бүртгэлийг ID-аар хайх
 val original = all.firstOrNull {
  it.id == id
 }

 // 3. Формын талбаруудын төлөв
 var type by rememberSaveableTransaction(
  id,
  original?.type ?: "Орлого"
 )

 var name by rememberSaveableTransaction(
  id,
  original?.name ?: ""
 )

 var amount by rememberSaveableTransaction(
  id,
  original?.amount?.toString() ?: ""
 )

 var date by rememberSaveableTransaction(
  id,
  original?.date ?: LocalDate.now().toString()
 )

 // 4. Алдааны мэдээлэл
 var error by remember {
  mutableStateOf("")
 }

 // 5. Room Flow-оос бүртгэл уншигдсаны дараа
 // формын утгуудыг шинэчлэх
 LaunchedEffect(original?.id) {

  if (id != 0L && original != null) {

   type = original.type
   name = original.name
   amount = original.amount.toString()
   date = original.date
  }
 }

 // 6. Дэлгэцийн үндсэн бүтэц
 Scaffold(
  topBar = {
   FinanceHeader(
    title = if (id == 0L) {
     "Бүртгэл нэмэх"
    } else {
     "Бүртгэл засах"
    },
    actionText = "Буцах",
    onAction = onBack
   )
  }
 ) { padding ->

  Column(
   modifier = Modifier
    .fillMaxSize()
    .padding(padding)
    .verticalScroll(rememberScrollState())
    .padding(20.dp),

   verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {

   // 7. Орлого эсвэл зарлага сонгох
   Text(
    text = "Гүйлгээний төрөл",
    style = MaterialTheme.typography.titleMedium
   )

   Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp)
   ) {

    FilterChip(
     selected = type == "Орлого",
     onClick = {
      type = "Орлого"
     },
     label = {
      Text("Орлого")
     }
    )

    FilterChip(
     selected = type == "Зарлага",
     onClick = {
      type = "Зарлага"
     },
     label = {
      Text("Зарлага")
     }
    )
   }

   // 8. Гүйлгээний нэр
   OutlinedTextField(
    value = name,
    onValueChange = {
     name = it
    },
    label = {
     Text("Гүйлгээний нэр")
    },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
   )

   // 9. Мөнгөн дүн
   OutlinedTextField(
    value = amount,
    onValueChange = {
     amount = it.filter(Char::isDigit)
    },
    label = {
     Text("Мөнгөн дүн (₮)")
    },
    keyboardOptions = KeyboardOptions(
     keyboardType = KeyboardType.Number
    ),
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
   )

   // 10. Огноо
   OutlinedTextField(
    value = date,
    onValueChange = {
     date = it
    },
    label = {
     Text("Огноо (YYYY-MM-DD)")
    },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true
   )

   // 11. Алдааны мэдээлэл харуулах
   if (error.isNotEmpty()) {
    Text(
     text = error,
     color = MaterialTheme.colorScheme.error
    )
   }

   // 12. Хадгалах товч
   Button(
    onClick = {

     val value = amount.toLongOrNull()

     // Оруулсан мэдээллийг шалгах
     if (
      name.isBlank() ||
      value == null ||
      value <= 0 ||
      runCatching {
       LocalDate.parse(date)
      }.isFailure
     ) {
      error =
       "Нэр, эерэг дүн, зөв огноо оруулна уу."
     }

     // Засах бүртгэл олдоогүй үед
     else if (
      id != 0L &&
      original == null
     ) {
      error = "Бүртгэл уншигдаагүй байна."
     }

     // Мэдээлэл зөв бол хадгалах
     else {

      vm.save(
       Transaction(
        id,
        type,
        name.trim(),
        value,
        date
       )
      )

      // Үндсэн дэлгэц рүү буцах
      onBack()
     }

    },
    modifier = Modifier.fillMaxWidth()
   ) {
    Text("ХАДГАЛАХ")
   }
  }
 }
}


// 13. Формын утгуудыг хадгалах helper функц
@Composable
private fun rememberSaveableTransaction(
 id: Long,
 initial: String
): MutableState<String> {

 return androidx.compose.runtime.saveable.rememberSaveable(id) {
  mutableStateOf(initial)
 }
}
