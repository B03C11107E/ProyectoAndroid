package com.dam.androidmh.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dam.androidmh.R
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.shared.MonstruosViewModelFirebase
import com.dam.androidmh.ui.shared.UsuarioViewModelFirebase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavHostController?){
    var user by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var auth : FirebaseAuth = Firebase.auth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,



        ){
        Column (modifier = Modifier.padding(18.dp,20.dp,0.dp,0.dp)){
            Text(text = "Aplicación monster hunter:", fontSize = 24.sp, color = Color.White, fontWeight = Bold, textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
            Text(text = "Registrarse", fontSize = 24.sp, color = Color.White, fontWeight = Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
        Row (modifier = Modifier
            .border(2.dp, Color.White, shape)
        ) {
            Button(onClick = {
                auth.createUserWithEmailAndPassword(user, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(context, "Cuenta creada correctamente", Toast.LENGTH_LONG).show()
                            navController!!.navigate(rutas.login.ruta)
                        }
                        else{
                            Toast.makeText(context,"Ocurrió un problema a la hora de crear una cuenta: "+task.exception, Toast.LENGTH_LONG).show()
                        }
                    }
            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(0.dp),
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.logo_monsterhunterregister),
                        contentDescription = "Logo monster hunter world",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(400.dp)
                    )
                } }
        }
        Row{
            Column {
                Text("Email",color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(17.dp,3.dp))
                TextField(value = user, onValueChange ={user = it}, shape = RoundedCornerShape(28.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ))
                Text("Contraseña",color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(17.dp,3.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(28.dp),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Outlined.Visibility
                        else Icons.Filled.VisibilityOff

                        // Please provide localized description for accessibility services
                        val description = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
        Row {
            Button(onClick = { navController!!.navigate(rutas.login.ruta) }) {
                Text("¿Ya tienes una cuenta? Inicia sesión aquí")
            }
        }
    }
}