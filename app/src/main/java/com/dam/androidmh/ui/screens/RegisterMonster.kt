package com.dam.androidmh.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dam.androidmh.ui.shared.MonstruosViewModelFirebase
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.androidmh.ui.model.Monstruo
import androidx.compose.material3.SearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.dam.androidmh.R
import com.dam.androidmh.ui.model.Usuario
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.shared.UsuarioViewModelFirebase

var listaAniadir: MutableList<Int> by mutableStateOf(mutableListOf())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMonster(navController : NavHostController, usuarioRecibido : String) {

    var query by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    val onSearch:(String) -> Unit = {
        query = it
        active = false
    }

    val monstruosViewModel: MonstruosViewModelFirebase = viewModel()

    val usuarioViewModel: UsuarioViewModelFirebase = viewModel()
    usuarioViewModel.obtenerLista()
    usuarioViewModel.cambiarUsuarioActivo(usuarioRecibido)

    val usuarioActivo by usuarioViewModel.usuarioActivo.collectAsState()

    val usuarioEditar = usuarioActivo.copy()

    usuarioEditar.monstruosCazadosId = listaAniadir

    Scaffold(topBar = {BarraSuperior(titulo = "Modificar Bestiario")} , containerColor = Color( R.color.purple_500),
        bottomBar = { BarraInferior(funcionNavegar1 = {
            usuarioViewModel.editarUsuario(usuarioActivo,usuarioEditar)
        }
            , funcionNavegar2 = {
                navController.navigate(rutas.bestiario.ruta+"/${usuarioActivo.email}")

            },icono1 = Icons.Filled.Add, icono2 = Icons.Default.ArrowBack)},
        content = {
            // paddingValues representa los dp que hay para evitar que el contenido se solape con las barras
                paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {

                monstruosViewModel.obtenerLista()
                var listaMonstruos = monstruosViewModel.listaMonstruos.collectAsState().value

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                    horizontalAlignment = Alignment.CenterHorizontally)
                {

                    SearchBar(query = query,
                        onQueryChange = {query = it},
                        onSearch = {
                            onSearch(query)
                        },
                        active = active,
                        onActiveChange = {active = it},
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        placeholder = {Text(text = "Buscar Monstruo")},
                        leadingIcon = { IconButton(onClick={ })  {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null
                            )
                        }
                        },
                        trailingIcon = { IconButton(
                            onClick = { onSearch(query) },
                            enabled = query.isNotEmpty()
                        ){
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                        }
                    ) {
                        val filteredMonstruos = listaMonstruos.filter { it.nombre.contains(query, true) }
                        filteredMonstruos.forEach {item -> Text(
                            text = item.nombre,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp)
                                .clickable {
                                    onSearch(item.nombre)
                                }
                        )}
                    }

                    ListWithLazyColumnEditarBestiario(listaMonstruos, query, usuarioActivo)

                }
            }

        })

}

@Composable
fun ListWithLazyColumnEditarBestiario(items: MutableList<Monstruo>, query: String, usuarioActivo: Usuario) {
    LazyColumn(
        modifier = Modifier.height(550.dp)
    ) {
        items(items) { item ->
            if (query == item.nombre || query == "") {
                var check = false
                usuarioActivo.monstruosCazadosId.forEach {
                    if (it == item.id) {
                        check = true
                    }
                }
                ListItemRowEditarBestiario(item, usuarioActivo, check)
            }
        }
    }
}
@Composable
fun ListItemRowEditarBestiario(item: Monstruo, usuarioActivo: Usuario, check: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color.White)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            Row( verticalAlignment = Alignment.CenterVertically
            ) {
                var scale by remember {
                    mutableStateOf(1f)
                }
                var offset by remember {
                    mutableStateOf(Offset.Zero)
                }
                val state = rememberTransformableState {
                        zoomChange, panChange, rotationChange ->
                    scale = (scale * zoomChange).coerceIn(1f, 5f)
                }
                AsyncImage(
                    model = item.imagen,
                    contentDescription = item.descripcion,
                    modifier = Modifier
                        .padding(top = 30.dp, end = 30.dp)
                        .size(120.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                        .transformable(state)
                )
                Text(text = item.nombre, fontSize = 18.sp, color = Color.White,  modifier = Modifier
                    .padding(0.dp,10.dp))

                var checked by remember {
                    if(check) {
                        mutableStateOf(true)
                    }  else {
                        mutableStateOf(false)
                    }
                }

                Checkbox(
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.Black, uncheckedColor = Color.White, checkedColor = Color.White),
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        if (checked) {
                            listaAniadir.add(item.id)
                        } else {
                            listaAniadir.remove(item.id)
                        }},
                )
            }
        }
    }
}