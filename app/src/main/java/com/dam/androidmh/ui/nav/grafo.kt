package com.dam.androidmh.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.screens.Bestiario
import com.dam.androidmh.ui.screens.Login
import com.dam.androidmh.ui.screens.Register

@Composable
fun GrafoNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = rutas.login.ruta) {

        composable(rutas.login.ruta){
            Login(navController = navController)
        }

        composable(rutas.bestiario.ruta){
            Bestiario()
        }
        composable(rutas.register.ruta){
            Register(navController = navController)
        }
    }
}