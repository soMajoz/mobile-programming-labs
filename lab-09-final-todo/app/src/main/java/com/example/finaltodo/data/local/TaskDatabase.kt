package com.example.finaltodo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Room database that owns the persistent task table. */
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    /** Returns the DAO for task queries and mutations. */
    abstract fun taskDao(): TaskDao

    /** Holds the lazily created process-wide database instance. */
    companion object {
        @Volatile
        private var instance: TaskDatabase? = null

        /** Returns the process-wide database instance. */
        fun getInstance(context: Context): TaskDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "final-tasks.db"
            ).build().also { instance = it }
        }
    }
}
