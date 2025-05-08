package com.farhansolih0009.assesment02.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.farhansolih0009.assesment02.model.Transaksi

@Database(entities = [Transaksi::class], version = 2, exportSchema = false)  // Incremented version
abstract class TransaksiDb : RoomDatabase() {

    abstract val dao: TransaksiDao

    companion object {

        @Volatile
        private var INSTANCE: TransaksiDb? = null

        fun getInstance(context: Context): TransaksiDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransaksiDb::class.java,
                        "transaksi.db"
                    )
                        // Apply migration here (if necessary)
                        .fallbackToDestructiveMigration()  // Clears the database and recreates on schema changes
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
