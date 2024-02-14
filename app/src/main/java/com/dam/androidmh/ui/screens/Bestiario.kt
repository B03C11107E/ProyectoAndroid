package com.dam.androidmh.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.androidmh.ui.model.Monstruo
import androidx.compose.material3.SearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.dam.androidmh.R
import com.dam.androidmh.ui.model.Usuario
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.shared.UsuarioViewModelFirebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bestiario(navController : NavHostController) {

    val listaMonstruosFiltrarPrueba = arrayOf("Pukei", "Anjanath", "Velkhana")

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

    var borrar by remember {
        mutableStateOf(false)
    }
    var openDialog by remember {
        mutableStateOf(false)
    }

    var textoPrueba by remember {
        mutableStateOf("Soy una prueba")
    }

    val monstruosViewModel: MonstruosViewModelFirebase = viewModel()

    monstruosViewModel.obtenerLista()
    //monstruosViewModel.aniadirMonstruo(Monstruo(2,"Anjanath",false,false,false,R.drawable.anjanath))

    var listaMonstruosPrueba = monstruosViewModel.listaMonstruos.collectAsState().value

    val usuarioViewModel: UsuarioViewModelFirebase = viewModel()

    usuarioViewModel.obtenerLista()

    var listaUsuariosPrueba = usuarioViewModel.listaUsuarios.collectAsState().value

    // Usuario de prueba, lo hago con un lazyColumn porque de las otras formas que he probado me da error
    var usuarioPrueba by remember {
        mutableStateOf(Usuario())
    }
    LazyColumn {
        items(listaUsuariosPrueba) { item ->
            usuarioPrueba = item
            textoPrueba = item.email
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row {
            Text(text = "Bestiario", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)
        }

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
            } }
        ) {
            val filteredMonstruo = listaMonstruosFiltrarPrueba.filter { it.contains(query, true) }
            filteredMonstruo.forEach {item -> Text(
                text = item,
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp)
                    .clickable {
                        onSearch(item)
                    }
            )}
        }

        if(usuarioPrueba.monstruosCazadosId.isNotEmpty()) {
            ListWithLazyColumn(listaMonstruosPrueba, query, usuarioPrueba)
        }

        // Saber el usuario que se editara
        Text(textoPrueba)

        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp, 20.dp),
        ) {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(rutas.registerMonster.ruta) },
                icon = { Icon(Icons.Filled.Add, "Boton flotante de añadir equipo") },
                text = { Text(text = "Añadir") },
            )
            ExtendedFloatingActionButton(
                onClick = {
                    if (!borrar) {
                        borrar = true
                    } else {
                        openDialog = true
                    }
                },
                icon = { Icon(Icons.Filled.Delete, "Boton flotante de borrar equipo") },
                text = { Text(text = "Borrar") },
                modifier = Modifier.padding(start = 145.dp)
            )
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = "Confirme el borrado")
            },
            text = {
                Text("¿Esta seguro de que quiere eliminar los monstruos marcados?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        //listaRemover.forEach { item ->
                        //    listaEquipos.remove(item)
                        //}
                        borrar = false
                        openDialog = false
                    }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        borrar = false
                        openDialog = false
                    }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ListWithLazyColumn(items: MutableList<Monstruo>, query: String, usuarioActivo: Usuario) {
    LazyColumn {
        usuarioActivo.monstruosCazadosId.forEach {
            items(items) { item ->
                if (query == item.nombre || query == "" && it == item.id) {
                    ListItemRow(item)
                }
            }
        }
    }
}

@Composable
fun ListItemRow(item: Monstruo) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color.Black)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            Row {
                //Image(painter = painterResource(id = item.imagen),
                Image(painter = painterResource(id = R.drawable.anjanath),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(top = 30.dp, end = 30.dp)
                        .size(120.dp)
                )
                Column {
                    Text(text = item.nombre, fontSize = 18.sp, color = Color.Black,  modifier = Modifier
                        .padding(0.dp,10.dp))
                    Text(text = "Cazado", modifier = Modifier
                        .padding(bottom = 10.dp))
                    Text(item.descripcion)
                }
            }
        }
    }
}