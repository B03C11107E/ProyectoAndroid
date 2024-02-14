package com.dam.androidmh.ui.screens

import android.content.res.Resources.Theme
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.dam.androidmh.R


@Composable
fun BarraInferior(funcionNavegar1: () -> Unit,
                  funcionNavegar2: () -> Unit,
                  icono1 : ImageVector,
                  icono2 : ImageVector
){
    BottomAppBar(modifier = Modifier.fillMaxWidth(),
        containerColor = Color( R.color.purple_500)) {
        Row(){
            IconButton(onClick = funcionNavegar1, modifier = Modifier.weight(1f)) {
                Icon(icono1, contentDescription = "", tint = Color.White)
            }
            IconButton(onClick = funcionNavegar2, modifier = Modifier.weight(1f)) {
                Icon(icono2, contentDescription = "", tint = Color.White)
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(titulo : String){

    CenterAlignedTopAppBar(title = { Text(titulo, color = Color.White) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(R.color.purple_500)
        )
    )

}