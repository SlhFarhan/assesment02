package com.farhansolih0009.assesment02.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val nominal: Double,
    val tanggal: String,
    val tipe: String
)
