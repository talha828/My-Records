package com.cs191014.assignment1.ui.records

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Record::class], version = 2)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        private var instance: RecordDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): RecordDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    ctx.applicationContext, RecordDatabase::class.java, "record_database"
                ).fallbackToDestructiveMigration().addCallback(roomCallback).build()
            }
            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: RecordDatabase) {
            val recordDao = db.recordDao()
            val recordsList = Array<Record>(10) {
                Record(
                    it + 1,
                    "Name  ${it + 1}",
                    "Lorem ipsum dolor",
                    "https://picsum.photos/${it + 200}",
                    false,
                )
            };
            db.transactionExecutor.execute {
                recordDao.insertAll(
                    *recordsList
                )
            }
        }
    }
}