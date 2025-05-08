package com.farhansolih0009.assesment02.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.farhansolih0009.assesment02.model.Transaksi
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {

    @Insert
    suspend fun insert(transaksi: Transaksi)

    @Update
    suspend fun update(transaksi: Transaksi)

    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    fun getTransaksi(): Flow<List<Transaksi>>

    @Query("SELECT * FROM transaksi WHERE id = :id")
    suspend fun getTransaksiById(id: Long): Transaksi?

    @Query("DELETE FROM transaksi WHERE id = :id")
    suspend fun deleteById(id: Long)
}
