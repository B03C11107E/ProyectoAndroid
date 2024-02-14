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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dam.androidmh.R
import com.dam.androidmh.ui.model.Usuario
import com.dam.androidmh.ui.rutas.rutas
import com.dam.androidmh.ui.shared.UsuarioViewModelFirebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bestiario(navController : NavHostController) {

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

    var usuarioActivoEmail by remember {
        mutableStateOf("Soy una prueba")
    }

    val monstruosViewModel: MonstruosViewModelFirebase = viewModel()

    monstruosViewModel.obtenerLista()
    //monstruosViewModel.aniadirMonstruo(Monstruo(2,"Anjanath",false,false,false,R.drawable.anjanath))
    /* //Introduciendo datos dejado comentado por si acaso se necesita otra vez
    monstruosViewModel.aniadirMonstruo(Monstruo(3,"Bazelgeuse","Monstruo que patrulla el continente entero en búsqueda de presas. Suele esparcir escamas explosivas.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(4,"Diablos","El mandamás del Yermo de Agujas. Extremadamente territorial y muy dado a embestir por sorpresa.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(5,"Gran Jagras","El voraz líder de los Jagras. Siempre atento y en busca del siguiente almuerzo.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(6,"Kulu-Ya-Ku","Un wyvern pájaro que usa sus extremidades frontales para atacar con todo tipo de recursos.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(7,"Odogaron","Terrorífico monstruo que recorre el Valle Putrefacto en busca de carroña para llevársela a su nido.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(8,"Nergigante","Un terrible dragón anciano consumido por su sed de destrucción. Su propia seguridad no parece preocuparle.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(9,"Rathalos","El mayor depredador del Bosque Primigenio, que patrulla los cielos en busca de intrusos.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(10,"Vaal Hazak","Poderosísimo dragón anciano que vive en los efluvios. Si el vapor llega a disiparse, el monstruo acumula más.",R.drawable.anjanath))
    monstruosViewModel.aniadirMonstruo(Monstruo(11,"Xeno'Jiiva","Nueva especie descubierta en las profundidades del Lecho de los Ancianos. Ni su relación con los otros dragones ancianos ni su ecología están claras.",R.drawable.anjanath))
    */
    var listaMonstruos = monstruosViewModel.listaMonstruos.collectAsState().value

    val usuarioViewModel: UsuarioViewModelFirebase = viewModel()

    usuarioViewModel.obtenerLista()

    var listaUsuariosPrueba = usuarioViewModel.listaUsuarios.collectAsState().value

    // Usuario de prueba, lo hago con un lazyColumn porque de las otras formas que he probado me da error
    var usuarioPrueba by remember {
        mutableStateOf(Usuario())
    }

    Scaffold(topBar = {BarraSuperior(titulo = "Bestiario de $usuarioActivoEmail")} , containerColor = Color( R.color.purple_500),
        bottomBar = { BarraInferior(funcionNavegar1 = {
            navController.navigate(rutas.registerMonster.ruta)
        }
            , funcionNavegar2 = {
                navController.navigate(rutas.login.ruta)

            },icono1 = Icons.Filled.Add, icono2 = Icons.Default.ArrowBack)},
        content = {
            // paddingValues representa los dp que hay para evitar que el contenido se solape con las barras
                paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn {
                    items(listaUsuariosPrueba) { item ->
                        usuarioPrueba = item
                        usuarioActivoEmail = item.email
                    }
                }

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
                        } }
                    ) {
                        val filteredMonstruo = listaMonstruos.filter { it.nombre.contains(query, true) }
                        filteredMonstruo.forEach {item -> Text(
                            text = item.nombre,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 5.dp)
                                .clickable {
                                    onSearch(item.nombre)
                                }
                        )}
                    }

                    if(usuarioPrueba.monstruosCazadosId.isNotEmpty()) {
                        ListWithLazyColumn(listaMonstruos, query, usuarioPrueba)
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

        })

}

@Composable
fun ListWithLazyColumn(items: MutableList<Monstruo>, query: String, usuarioActivo: Usuario) {
    LazyColumn(
        modifier = Modifier.height(550.dp)
    ) {
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
            .border(2.dp, Color.White)
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
                    Text(text = item.nombre, fontSize = 18.sp, color = Color.White,  modifier = Modifier
                        .padding(0.dp,10.dp))
                    Text(text = "Cazado", color = Color.White, modifier = Modifier
                        .padding(bottom = 10.dp))
                    Text(text = item.descripcion, color = Color.White)
                }
            }
        }
    }
}