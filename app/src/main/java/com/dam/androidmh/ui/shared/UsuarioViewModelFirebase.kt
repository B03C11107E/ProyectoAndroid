package com.dam.androidmh.ui.shared

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.dam.androidmh.ui.model.Usuario
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsuarioViewModelFirebase : ViewModel() {


    // conexion mysql...
    val conexion = FirebaseFirestore.getInstance()

    // El listener
    private lateinit var listenerReg: ListenerRegistration

    // Lista de todos los usuarios, que la actualizará Firebase.
    private var _listaUsuarios = MutableStateFlow(mutableStateListOf<Usuario>())
    var listaUsuarios = _listaUsuarios.asStateFlow()

    // Usuario activo
    private var _usuarioActivo = MutableStateFlow(Usuario())
    var usuarioActivo = _usuarioActivo.asStateFlow()

    fun crearListener() {
        // ponemos la oreja
        listenerReg = conexion.collection("Usuario").addSnapshotListener { datos, error ->
            if (error == null) {
                // ¿Que cambios nuevos ha habido en la BBDD?
                datos?.documentChanges?.forEach { cambio ->
                    if (cambio.type == DocumentChange.Type.ADDED) {
                        // añadimos elemento a la lista UI
                        var nuevoUsuario = cambio.document.toObject<Usuario>()
                        nuevoUsuario.email = cambio.document.id
                        _listaUsuarios.value.add(nuevoUsuario)
                    } else if (cambio.type == DocumentChange.Type.REMOVED) {
                        // borramos elemento de la lista UI
                        var nuevoUsuario = cambio.document.toObject<Usuario>()
                        _listaUsuarios.value.remove(nuevoUsuario)
                    } else if (cambio.type == DocumentChange.Type.MODIFIED) {
                        // modificamos elemento de la lista UI
                        var nuevoUsuario = cambio.document.toObject<Usuario>()
                        _listaUsuarios.value[cambio.newIndex] = nuevoUsuario
                    }
                }
            }
        }
    }

    fun borrarListener() {
        listenerReg.remove()
    }

    fun cambiarUsuarioActivo(usuarioBuscar: String) {
        _listaUsuarios.value.forEach {item ->
            if (item.email == usuarioBuscar) {
                _usuarioActivo.value = item
            }
        }
    }

    fun obtenerLista() {
        listenerReg = conexion.collection("Usuario").addSnapshotListener {
                datos, excepcion ->
            if(excepcion == null) {
                if(datos != null) {
                    _listaUsuarios.value.clear()

                    datos.forEach { documento ->
                        val p = documento.toObject(Usuario::class.java)
                        _listaUsuarios.value.add(p)
                    }
                }
            }
        }
    }

    fun aniadirUsuario(nuevoUsuario: Usuario) {
        conexion.collection("Usuario").add(nuevoUsuario)
    }
    fun borrarUsuario(usuarioABorrar: Usuario){
        conexion.collection("Usuario").document(usuarioABorrar.email).delete()
    }
    fun editarUsuario(usuarioAEditar: Usuario, nuevoUsuario: Usuario){
        conexion.collection("Usuario").document(usuarioAEditar.email).set(Usuario(nuevoUsuario.email,
            nuevoUsuario.fotoDePerfil, nuevoUsuario.monstruosCazadosId))
    }
}