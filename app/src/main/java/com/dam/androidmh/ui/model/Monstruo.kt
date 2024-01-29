package com.dam.androidmh.ui.model

data class Monstruo(var id: Int = 0,
                    var nombre : String = "",
                    var rangoBajo : Boolean = false,
                    var rangoAlto : Boolean = false,
                    var rangoMaestro : Boolean = false,
                    var imagen : Int = 0
    )