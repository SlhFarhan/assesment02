package com.farhansolih0009.assesment02.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.farhansolih0009.assesment02.R
import com.farhansolih0009.assesment02.database.TransaksiDb
import com.farhansolih0009.assesment02.util.ViewModelFactory

const val KEY_ID_TRANSAKSI = "idTransaksi"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = TransaksiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var kategori by remember { mutableStateOf("") }
    var nominal by remember { mutableStateOf("") }
    var tipe by remember { mutableStateOf("Pemasukan") }

    var showDialog by remember { mutableStateOf(false) }

    // Handle category dropdown expand state separately
    var expandedKategori by remember { mutableStateOf(false) }
    var expandedTipe by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getTransaksi(id) ?: return@LaunchedEffect
        kategori = data.judul // Set kategori (category)
        nominal = data.nominal.toString()
        tipe = data.tipe
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null) stringResource(R.string.tambah_transaksi)
                        else stringResource(R.string.edit_transaksi)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (kategori.isEmpty() || nominal.isEmpty() || tipe.isEmpty()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(kategori, nominal.toDouble(), tipe)
                        } else {
                            viewModel.update(id, kategori, nominal.toDouble(), tipe)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }
                        ) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormTransaksi(
            kategori = kategori,
            onKategoriChange = { kategori = it },
            nominal = nominal,
            onNominalChange = { nominal = it },
            tipe = tipe,
            onTipeChange = { tipe = it },
            expandedKategori = expandedKategori,
            onExpandedKategoriChange = { expandedKategori = it },
            expandedTipe = expandedTipe,
            onExpandedTipeChange = { expandedTipe = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.hapus)) },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormTransaksi(
    kategori: String, onKategoriChange: (String) -> Unit,
    nominal: String, onNominalChange: (String) -> Unit,
    tipe: String, onTipeChange: (String) -> Unit,
    expandedKategori: Boolean, onExpandedKategoriChange: (Boolean) -> Unit,
    expandedTipe: Boolean, onExpandedTipeChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    val kategoriList = listOf("Makan", "Kebutuhan", "Tabungan", "Invest")
    val tipeList = listOf("Pemasukan", "Pengeluaran")

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dropdown untuk memilih kategori
        Text("Pilih Kategori Transaksi:", style = MaterialTheme.typography.bodyMedium)

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExpandedKategoriChange(!expandedKategori) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Kategori: $kategori",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Pilih Kategori",
                    )
                }


                DropdownMenu(
                    expanded = expandedKategori,
                    onDismissRequest = { onExpandedKategoriChange(false) }
                ) {
                    kategoriList.forEach { kategoriItem ->
                        DropdownMenuItem(
                            text = { Text(text = kategoriItem) },
                            onClick = {
                                onKategoriChange(kategoriItem)
                                onExpandedKategoriChange(false)
                            }
                        )
                    }
                }
            }
        }


        OutlinedTextField(
            value = nominal,
            onValueChange = onNominalChange,
            label = { Text("Nominal Rp: ... ") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )


        Text("Pilih Tipe Transaksi:", style = MaterialTheme.typography.bodyMedium)

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExpandedTipeChange(!expandedTipe) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = tipe,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Pilih Tipe",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                DropdownMenu(
                    expanded = expandedTipe,
                    onDismissRequest = { onExpandedTipeChange(false) }
                ) {
                    tipeList.forEach { tipeItem ->
                        DropdownMenuItem(
                            text = { Text(text = tipeItem) },
                            onClick = {
                                onTipeChange(tipeItem)
                                onExpandedTipeChange(false)
                            }
                        )
                    }
                }
            }
        }
    }
}
