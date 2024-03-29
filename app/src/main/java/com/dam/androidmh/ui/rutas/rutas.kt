package com.dam.androidmh.ui.rutas

sealed class rutas(val ruta: String) {
    object login: rutas("login")
    object home: rutas("home")
    object bestiario: rutas("bestiario")
    object register: rutas("register")
    object registerMonster: rutas("registerMonster")
}