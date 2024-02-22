package com.dam.androidmh.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.screens.Bestiario
import com.dam.androidmh.ui.screens.Login
import com.dam.androidmh.ui.screens.Register
import com.dam.androidmh.ui.screens.RegisterMonster

@Composable
fun GrafoNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = rutas.login.ruta) {

        composable(rutas.login.ruta){
            Login(navController = navController)
        }

        composable(rutas.bestiario.ruta+"/{userId}"){
            val usuarioRecibido = it.arguments?.getString("userId") ?: ""
            Bestiario(navController = navController, usuarioRecibido)
        }
        composable(rutas.register.ruta){
            Register(navController = navController)
        }
        composable(rutas.registerMonster.ruta){
            val usuarioRecibido = it.arguments?.getString("userId") ?: ""
            RegisterMonster(navController = navController, usuarioRecibido)
        }
    }
}