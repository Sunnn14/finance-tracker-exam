
package com.example.personalfinancetracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.personalfinancetracker.data.FinanceRepository
import com.example.personalfinancetracker.data.local.FinanceDatabase
import com.example.personalfinancetracker.data.settings.SettingsDataStore
import com.example.personalfinancetracker.navigation.FinanceNavigation
import com.example.personalfinancetracker.ui.FinanceViewModel
import com.example.personalfinancetracker.worker.ReminderScheduler


class MainActivity : ComponentActivity() {

 // 1. Апп background төлөвт шилжих үед ажиллана
 override fun onStop() {
  super.onStop()

  // Дэлгэц эргүүлэх үед сануулгын хугацааг дахин эхлүүлэхгүй
  if (!isChangingConfigurations) {
   ReminderScheduler.schedule(applicationContext)
  }
 }

 // 2. Апп нээгдэх үед ажиллах үндсэн функц
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)

  // 3. Room Database үүсгэх
  val db = Room.databaseBuilder(
   applicationContext,
   FinanceDatabase::class.java,
   "finance.db"
  ).build()

  // 4. Repository үүсгэх
  val repository = FinanceRepository(
   db.dao(),
   SettingsDataStore(applicationContext)
  )

  // 5. Jetpack Compose UI эхлүүлэх
  setContent {

   // 6. ViewModel үүсгэх
   val vm: FinanceViewModel = viewModel(
    factory = FinanceViewModel.factory(repository)
   )

   // 7. Notification permission хүсэх
   val permission = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
   ) {}

   LaunchedEffect(Unit) {

    // Android 13 (API 33)-аас дээш үед
    // мэдэгдлийн зөвшөөрөл авах
    if (
     Build.VERSION.SDK_INT >= 33 &&
     checkSelfPermission(
      Manifest.permission.POST_NOTIFICATIONS
     ) != PackageManager.PERMISSION_GRANTED
    ) {
     permission.launch(
      Manifest.permission.POST_NOTIFICATIONS
     )
    }
   }

   // 8. Аппликейшний үндсэн UI болон Navigation
   MaterialTheme {
    FinanceNavigation(vm)
   }
  }
 }
}
