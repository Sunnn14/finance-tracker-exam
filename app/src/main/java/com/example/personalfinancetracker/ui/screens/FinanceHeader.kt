
package com.example.personalfinancetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FinanceHeader(
 title: String,
 actionText: String,
 onAction: () -> Unit
) {
 Surface(
  color = Color(0xFF1565C0),
  contentColor = Color.White,
  shadowElevation = 4.dp
 ) {
  Row(
   modifier = Modifier
    .fillMaxWidth()
    .height(56.dp)
    .padding(horizontal = 16.dp),
   horizontalArrangement = Arrangement.SpaceBetween,
   verticalAlignment = Alignment.CenterVertically
  ) {
   Text(
    text = title,
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    color = Color.White
   )

   TextButton(onClick = onAction) {
    Text(
     text = actionText,
     color = Color.White,
     fontSize = 14.sp,
     fontWeight = FontWeight.Medium
    )
   }
  }
 }
}
