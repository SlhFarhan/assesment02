package com.farhansolih0009.assesment02.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.farhansolih0009.assesment02.R
import com.farhansolih0009.assesment02.database.TransaksiDb
import com.farhansolih0009.assesment02.model.Transaksi
import com.farhansolih0009.assesment02.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = TransaksiDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val deletedData by viewModel.deletedData.collectAsState()

    // Dialog state and item to delete
    var showDialog by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaksi?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.kotak_sampah), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Scrollable Content
        Column(modifier = Modifier.padding(padding)) {
            if (deletedData.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tidak ada data di kotak sampah", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(deletedData) { transaksi ->
                        RecycleBinListItem(
                            transaksi = transaksi,
                            onRestore = {
                                viewModel.restoreTransaction(transaksi.id)
                            },
                            onDeleteForever = {
                                // Set the transaction to delete and show the dialog
                                transactionToDelete = transaksi
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Show delete confirmation dialog when showDialog is true
    if (showDialog && transactionToDelete != null) {
        // Inline the AlertDialog here
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = stringResource(id = R.string.hapus_permanent), style = MaterialTheme.typography.bodyMedium)
            },
            text = {
                Text(text = stringResource(id = R.string.konfirmasi_hapus_permanent))
            },
            confirmButton = {
                TextButton(onClick = {
                    // Perform the permanent deletion
                    viewModel.permanentlyDeleteTransaction(transactionToDelete!!.id)
                    navController.popBackStack()
                    showDialog = false  // Dismiss the dialog
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun RecycleBinListItem(
    transaksi: Transaksi,
    onRestore: () -> Unit,
    onDeleteForever: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRestore() },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = transaksi.judul,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(text = "Nominal: ${transaksi.nominal}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tipe: ${transaksi.tipe}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Tanggal: ${transaksi.tanggal}", style = MaterialTheme.typography.bodyMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Restore Button
                Button(
                    onClick = { onRestore() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = stringResource(id = R.string.restore))
                }

                // Permanently Delete Button
                Button(
                    onClick = {
                        onDeleteForever() // Trigger delete confirmation dialog
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(id = R.string.hapus_permanent))
                }
            }
        }
    }
}
