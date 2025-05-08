package com.farhansolih0009.assesment02.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.farhansolih0009.assesment02.model.Transaksi
import com.farhansolih0009.assesment02.database.TransaksiDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: TransaksiDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(judul: String, nominal: Double, tipe: String) {
        val transaksi = Transaksi(
            tanggal = formatter.format(Date()),
            judul = judul,
            nominal = nominal,
            tipe = tipe
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(transaksi)
        }
    }

    suspend fun getTransaksi(id: Long): Transaksi? {
        return dao.getTransaksiById(id)
    }

    fun update(id: Long, judul: String, nominal: Double, tipe: String) {
        val transaksi = Transaksi(
            id = id,
            tanggal = formatter.format(Date()),
            judul = judul,
            nominal = nominal,
            tipe = tipe
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(transaksi)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}
