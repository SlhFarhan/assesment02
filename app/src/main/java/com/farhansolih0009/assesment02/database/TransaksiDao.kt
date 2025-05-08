package com.farhansolih0009.assesment02.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.farhansolih0009.assesment02.model.Transaksi
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaksiDao {

    @Insert
    suspend fun insert(transaksi: Transaksi)

    @Update
    suspend fun update(transaksi: Transaksi)

    // Get all active transactions
    @Query("SELECT * FROM transaksi WHERE isDeleted = 0 ORDER BY tanggal DESC")
    fun getTransaksi(): Flow<List<Transaksi>>

    // Get a transaction by its ID
    @Query("SELECT * FROM transaksi WHERE id = :id")
    suspend fun getTransaksiById(id: Long): Transaksi?

    // Get deleted transactions (for Recycle Bin)
    @Query("SELECT * FROM transaksi WHERE isDeleted = 1 ORDER BY tanggal DESC")
    fun getDeletedTransaksi(): Flow<List<Transaksi>>

    // Mark transaction as deleted (soft delete)
    @Query("UPDATE transaksi SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteById(id: Long)

    // Restore transaction from the Recycle Bin (restore soft delete)
    @Query("UPDATE transaksi SET isDeleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)

    // Permanently delete a transaction
    @Query("DELETE FROM transaksi WHERE id = :id")
    suspend fun permanentlyDelete(id: Long)
}
