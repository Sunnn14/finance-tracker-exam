
package com.example.personalfinancetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.ui.FinanceViewModel

import androidx.compose.ui.platform.LocalContext
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import com.example.personalfinancetracker.worker.ReminderWorker

@Composable
fun SettingsScreen(
 vm: FinanceViewModel,
 onBack: () -> Unit
) {

 // 1. ViewModel-ээс хадгалагдсан тохиргоог авах
 val filter by vm.filter.collectAsState()

 val context = LocalContext.current

 // 2. Хэрэглэгчийн сонгосон утгыг түр хадгалах
 var selected by remember(filter) {
  mutableIntStateOf(filter)
 }

 // 3. Дэлгэцийн үндсэн бүтэц
 Scaffold(
  topBar = {
   FinanceHeader(
    title = "Тохиргоо",
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

   // 4. Тохиргооны гарчиг
   Text(
    text = "Үндсэн дэлгэцэд харуулах төрөл",
    style = MaterialTheme.typography.titleMedium
   )

   // 5. Гурван сонголтыг харуулах
   listOf(
    "Бүгдийг харуулах",
    "Зөвхөн орлого",
    "Зөвхөн зарлага"
   ).forEachIndexed { i, label ->

    Row {

     // 6. RadioButton сонголт
     RadioButton(
      selected = selected == i,
      onClick = {
       selected = i
      }
     )

     // 7. Сонголтын нэр дээр дарах боломж
     TextButton(
      onClick = {
       selected = i
      }
     ) {
      Text(label)
     }
    }
   }

   // 8. Тохиргоо хадгалах
   Button(
    onClick = {

     // Сонгосон утгыг ViewModel-д дамжуулах
     vm.setFilter(selected)

     // Үндсэн дэлгэц рүү буцах
     onBack()
    },
    modifier = Modifier.fillMaxWidth()
   ) {
    Text("ХАДГАЛАХ")
   }

   // Мэдэгдлийг шууд турших товч
   Button(
    onClick = {

     val testWork =
      OneTimeWorkRequestBuilder<ReminderWorker>()
       .build()

     WorkManager.getInstance(context)
      .enqueue(testWork)
    },
    modifier = Modifier.fillMaxWidth()
   ) {
    Text("МЭДЭГДЭЛ ТУРШИХ")
   }

  }
 }
}
