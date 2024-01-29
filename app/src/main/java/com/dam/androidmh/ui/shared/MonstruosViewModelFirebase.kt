package com.dam.androidmh.ui.shared

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import com.dam.androidmh.ui.model.Monstruo
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.asStateFlow
class MonstruosViewModelFirebase : ViewModel() {

    // conexion mysql...
    var conexion = FirebaseFirestore.getInstance()

    // Lista de Monstruos
    private var _listaMonstruos = MutableStateFlow(mutableStateListOf<Monstruo>())
    var listaMonstruos = _listaMonstruos.asStateFlow()

    fun crearListener(){
        // ponemos la oreja
        conexion.collection("Monstruos").addSnapshotListener{
                datos, error ->
            if(error == null) {
                // ¿Que cambios nuevos ha habido en la BBDD?
                datos?.documentChanges?.forEach { cambio ->
                    if(cambio.type == DocumentChange.Type.ADDED) {
                        // añadimos elemento a la lista UI
                    }
                    else if(cambio.type == DocumentChange.Type.REMOVED) {
                        // borramos elemento de la lista UI
                    }
                    else if(cambio.type == DocumentChange.Type.MODIFIED) {
                        // modificamos elemento de la lista UI
                    }
                }
            }
        }
    }

    fun obtenerLista(): String {
        var nombre = "No funciona"
        conexion.collection("Monstruos").addSnapshotListener {
            datos, excepcion ->
            if(excepcion == null) {
                nombre = "funciona?"
                if(datos != null) {
                    nombre = "funciona?"
                    listaMonstruos.value.clear()

                    datos.forEach { documento ->
                        val p = documento.toObject(Monstruo::class.java)
                        listaMonstruos.value.add(p)
                    }
                }
            }
        }
        return nombre
    }
}