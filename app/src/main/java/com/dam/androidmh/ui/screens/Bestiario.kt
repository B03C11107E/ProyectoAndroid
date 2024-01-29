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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialogDefaults.shape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bestiario() {

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

    val monstruosViewModel: MonstruosViewModelFirebase = viewModel()

    monstruosViewModel.obtenerLista()
    //monstruosViewModel.aniadirMonstruo(Monstruo(2,"Anjanath",false,false,false,R.drawable.anjanath))

    var listaMonstruosPrueba = monstruosViewModel.listaMonstruos.collectAsState().value

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
            }
            }
        ) {
            val filteredEquipos = listaMonstruosFiltrarPrueba.filter { it.contains(query, true) }
            filteredEquipos.forEach {item -> Text(
                text = item,
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp)
                    .clickable {
                        onSearch(item)
                    }
            )}
        }

        ListWithLazyColumn(listaMonstruosPrueba, query)
    }
}

@Composable
fun ListWithLazyColumn(items: MutableList<Monstruo>, query: String) { // (1)
    LazyColumn {
        items(items) { item ->
            if (query == item.nombre || query == "") {
                ListItemRow(item)
            }
        }
    }
}
@Composable // (3)
fun ListItemRow(item: Monstruo) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color.Black)
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            Row {
                Image(painter = painterResource(id = item.imagen),
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
                    CheckboxCustom(item.rangoBajo, "Rango Bajo")
                    CheckboxCustom(item.rangoAlto, "Rango Alto")
                    CheckboxCustom(item.rangoMaestro, "Rango Maestro")
                }
            }
        }
    }
}

@Composable
fun CheckboxCustom(check: Boolean, texto: String) {
    val checked = remember { mutableStateOf(check) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(texto)
        Checkbox(
            checked = checked.value,
            onCheckedChange = { isChecked -> checked.value = isChecked }
        )
    }
}