package com.farhansolih0009.assesment02.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import com.farhansolih0009.assesment02.database.TransaksiDao
import com.farhansolih0009.assesment02.model.Transaksi
import kotlinx.coroutines.launch

class MainViewModel(private val dao: TransaksiDao) : ViewModel() {

    // Get all active transactions (not deleted)
    val data: StateFlow<List<Transaksi>> = dao.getTransaksi().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    // Get all deleted transactions (Recycle Bin)
    val deletedData: StateFlow<List<Transaksi>> = dao.getDeletedTransaksi().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    // Restore a transaction from the Recycle Bin
    fun restoreTransaction(id: Long) {
        viewModelScope.launch {
            dao.restoreById(id)
        }
    }

    // Permanently delete a transaction from the database
    fun permanentlyDeleteTransaction(id: Long) {
        viewModelScope.launch {
            dao.permanentlyDelete(id)
        }
    }
}
