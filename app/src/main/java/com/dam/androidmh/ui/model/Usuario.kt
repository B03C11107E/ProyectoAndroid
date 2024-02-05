package com.dam.androidmh.ui.model

data class Usuario(
    var email: String = "",
    var fotoDePerfil : Int = 0,
    var monstruosCazados : List<Int> = emptyList(),
)
