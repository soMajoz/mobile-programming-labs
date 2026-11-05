package com.example.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Локальная база данных Room с одной таблицей задач.
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /** DAO для CRUD-операций над задачами. */
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Возвращает единственный экземпляр базы данных приложения.
         *
         * @param context контекст приложения.
         */
        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasks.db"
                ).build().also { instance = it }
            }
    }
}