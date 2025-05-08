package com.farhansolih0009.assesment02.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.farhansolih0009.assesment02.ui.screen.DetailViewModel
import com.farhansolih0009.assesment02.ui.screen.MainViewModel
import com.farhansolih0009.assesment02.database.TransaksiDao

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val dao: TransaksiDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
