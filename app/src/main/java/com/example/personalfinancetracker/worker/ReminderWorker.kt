
package com.example.personalfinancetracker.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

import com.example.personalfinancetracker.MainActivity


class ReminderWorker(
 context: Context,
 params: WorkerParameters
) : Worker(context, params) {

 override fun doWork(): Result {

  // 1. NotificationManager авах
  val manager = applicationContext.getSystemService(
   Context.NOTIFICATION_SERVICE
  ) as NotificationManager

  // 2. Мэдэгдлийн суваг үүсгэх
  manager.createNotificationChannel(
   NotificationChannel(
    "finance_reminder",
    "Санхүүгийн сануулга",
    NotificationManager.IMPORTANCE_DEFAULT
   )
  )

  // 3. Мэдэгдэл дээр дарахад MainActivity нээх
  val intent = Intent(
   applicationContext,
   MainActivity::class.java
  ).apply {
   flags = Intent.FLAG_ACTIVITY_NEW_TASK or
           Intent.FLAG_ACTIVITY_CLEAR_TOP or
           Intent.FLAG_ACTIVITY_SINGLE_TOP
  }

  // 4. PendingIntent үүсгэх
  val pending = PendingIntent.getActivity(
   applicationContext,
   0,
   intent,
   PendingIntent.FLAG_UPDATE_CURRENT or
           PendingIntent.FLAG_IMMUTABLE
  )

  // 5. Мэдэгдэл үүсгэх
  val notification = NotificationCompat.Builder(
   applicationContext,
   "finance_reminder"
  )
   .setSmallIcon(android.R.drawable.ic_dialog_info)
   .setContentTitle("Санхүүгийн бүртгэл")
   .setContentText("Өнөөдрийн орлого, зарлагаа бүртгээрэй.")
   .setAutoCancel(true)
   .setContentIntent(pending)
   .build()

  // 6. Notification permission шалгах
  if (
   Build.VERSION.SDK_INT < 33 ||
   applicationContext.checkSelfPermission(
    Manifest.permission.POST_NOTIFICATIONS
   ) == PackageManager.PERMISSION_GRANTED
  ) {
   manager.notify(100, notification)
  }

  // 7. Worker амжилттай дууссан
  return Result.success()
 }
}
