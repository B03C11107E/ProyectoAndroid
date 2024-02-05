package com.dam.androidmh.ui.shared

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dam.androidmh.ui.model.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.checkerframework.checker.nullness.qual.NonNull

class UsuarioViewModelFirebase : ViewModel() {



    // conexion mysql...
    val conexion = FirebaseFirestore.getInstance()

    // El listener
    private lateinit var listenerReg: ListenerRegistration

    private var auth = FirebaseAuth.getInstance()

    // Lista de toda la grifa, que la actualizará Firebase.
    private var _listaUsuarios = MutableStateFlow(mutableStateListOf<Usuario>())
    var listaUsuarios = _listaUsuarios.asStateFlow()

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

    fun aniadirUsuario(nuevoUsuario: Usuario) {
        conexion.collection("Usuario").add(nuevoUsuario)
    }
    fun borrarUsuario(usuarioABorrar: Usuario){
        conexion.collection("Usuario").document(usuarioABorrar.email).delete()
    }
    fun editarUsuario(usuarioAEditar: Usuario, nuevoUsuario: Usuario){
        conexion.collection("Usuario").document(usuarioAEditar.email).set(Usuario(nuevoUsuario.email,
            nuevoUsuario.fotoDePerfil, nuevoUsuario.monstruosCazados))
    }
    fun login(email: String, password: String) : Boolean{
        var logged = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener()
            {
                @Override
                fun onComplete( task : @NonNull Task<AuthResult> ){
                    if(task.isSuccessful){
                        logged = true
                    }
                }
            }
        return logged
    }
}