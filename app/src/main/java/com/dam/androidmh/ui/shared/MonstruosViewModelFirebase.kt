package com.dam.androidmh.ui.shared

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import com.dam.androidmh.ui.model.Monstruo
import com.dam.androidmh.ui.model.Usuario
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.asStateFlow
class MonstruosViewModelFirebase : ViewModel() {

    // conexion mysql...
    var conexion = FirebaseFirestore.getInstance()

    private lateinit var listenerReq: ListenerRegistration

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
                        var nuevoMonstruo = cambio.document.toObject<Monstruo>()
                        nuevoMonstruo.nombre = cambio.document.id
                        _listaMonstruos.value.add(nuevoMonstruo)
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

    fun obtenerLista() {
        listenerReq = conexion.collection("Monstruos").addSnapshotListener {
                datos, excepcion ->
            if(excepcion == null) {
                if(datos != null) {
                    listaMonstruos.value.clear()

                    datos.forEach { documento ->
                        val p = documento.toObject(Monstruo::class.java)
                        listaMonstruos.value.add(p)
                    }
                }
            }
        }
    }

    fun aniadirMonstruo(nuevoMonstruo: Monstruo) {
        conexion.collection("Monstruos").add(nuevoMonstruo)
    }
}