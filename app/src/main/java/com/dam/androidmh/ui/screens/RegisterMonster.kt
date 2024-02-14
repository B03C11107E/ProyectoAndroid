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

var listaAniadir: MutableList<Int> by mutableStateOf(mutableListOf())
var primeraVez = true;

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterMonster(navController : NavHostController) {

    primeraVez = true

    //val listaMonstruosFiltrarPrueba = arrayOf("Pukei", "Anjanath", "Velkhana")

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
    val usuarioViewModel: UsuarioViewModelFirebase = viewModel()

    usuarioViewModel.obtenerLista()

    var listaUsuariosPrueba = usuarioViewModel.listaUsuarios.collectAsState().value

    // Usuario de prueba, lo hago con un lazyColumn porque de las otras formas que he probado me da error
    var usuarioPruebaEditar by remember {
        mutableStateOf(Usuario("Prueba1",1, emptyList()))
    }
    var usuario by remember {
        mutableStateOf(Usuario("Prueba2",1, emptyList()))
    }
    LazyColumn {
        items(listaUsuariosPrueba) { item ->
            textoPrueba = item.email
            usuarioPruebaEditar = item.copy()
            usuario = item.copy()
            usuario.monstruosCazadosId = listaAniadir
        }
    }

    monstruosViewModel.obtenerLista()

    var listaMonstruos = monstruosViewModel.listaMonstruos.collectAsState().value

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row {
            Text(text = "Modificar Bestiario", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
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

        ListWithLazyColumnEditarBestiario(listaMonstruos, query, usuarioPruebaEditar)

        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp, 20.dp),
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    usuarioViewModel.editarUsuario(usuarioPruebaEditar,usuario)
                          textoPrueba = listaUsuariosPrueba[0].monstruosCazadosId.size.toString()
                          },
                icon = { Icon(Icons.Filled.Add, "Boton de añadir monstruo") },
                text = { Text(text = "Confirmar cambios") },
            )
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(rutas.bestiario.ruta)
                },
                icon = { Icon(Icons.Default.ArrowBack, "Boton de volver atras") },
                text = { Text(text = "Volver") },
                modifier = Modifier.padding(start = 65.dp)
            )
        }
    }
}

@Composable
fun ListWithLazyColumnEditarBestiario(items: MutableList<Monstruo>, query: String, usuarioActivo: Usuario) {
    LazyColumn(
        modifier = Modifier.height(550.dp)
    ) {
        items(items) { item ->
            if (query == item.nombre || query == "") {
                ListItemRowEditarBestiario(item, usuarioActivo)
            }
        }
    }
}
@Composable
fun ListItemRowEditarBestiario(item: Monstruo, usuarioActivo: Usuario) {
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
                //Image(painter = painterResource(id = item.imagen),
                Image(painter = painterResource(id = R.drawable.anjanath),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .size(120.dp)
                )
                Text(text = item.nombre, fontSize = 18.sp, color = Color.White,  modifier = Modifier
                    .padding(0.dp,10.dp))


                var checked by remember { mutableStateOf(false) }

                // Esto va a acabar con mi vida, cada vez funciona diferente.
                /*
                               var contador = 1
                               usuarioActivo.monstruosCazadosId.forEach {

                                   if (it == item.id && primeraVez) {
                                       checked = true
                                       contador++
                                       if (usuarioActivo.monstruosCazadosId.size == contador) {
                                           primeraVez = false
                                       }
                                   }
                               } */

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