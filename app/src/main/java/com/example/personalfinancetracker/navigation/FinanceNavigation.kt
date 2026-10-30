package com.example.personalfinancetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.personalfinancetracker.ui.FinanceViewModel
import com.example.personalfinancetracker.ui.screens.EditScreen
import com.example.personalfinancetracker.ui.screens.HomeScreen
import com.example.personalfinancetracker.ui.screens.SettingsScreen


object Routes {

 const val HOME = "home"
 const val SETTINGS = "settings"
 const val EDIT = "edit/{id}"
 fun edit(id: Long = 0L): String {
  return "edit/$id"
 }
}


@Composable
fun FinanceNavigation(vm: FinanceViewModel) {

 val nav = rememberNavController()

 NavHost(
  navController = nav,
  startDestination = Routes.HOME
 ) {

  // 1. Үндсэн дэлгэц
  composable(Routes.HOME) {

   HomeScreen(
    vm = vm,

    onAdd = {
     nav.navigate(Routes.edit())
    },

    onEdit = { id ->
     nav.navigate(Routes.edit(id))
    },

    onSettings = {
     nav.navigate(Routes.SETTINGS)
    }
   )
  }


  // 2. Бүртгэл нэмэх, засах дэлгэц
  composable(
   route = Routes.EDIT,
   arguments = listOf(
    navArgument("id") {
     type = NavType.LongType
    }
   )
  ) { entry ->

   EditScreen(
    vm = vm,
    id = entry.arguments?.getLong("id") ?: 0L,
    onBack = {
     nav.popBackStack()
    }
   )
  }


  // 3. Тохиргооны дэлгэц
  composable(Routes.SETTINGS) {

   SettingsScreen(
    vm = vm,

    onBack = {
     nav.popBackStack()
    }
   )
  }
 }
}