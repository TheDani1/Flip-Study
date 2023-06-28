package com.example.flipstudy.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.flipstudy.ui.components.BottomNavigation
import com.example.flipstudy.ui.navigation.AppNavigation

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {innerPadding ->
        AppNavigation(navController, Modifier.padding(innerPadding))
    }
}