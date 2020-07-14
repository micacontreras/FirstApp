package com.example.firstapp.task.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Tasks::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDataBase : RoomDatabase() {
    abstract fun taskDao(): TasksDao
    companion object {
        @Volatile
        private var INSTANCE: TaskDataBase? = null

        fun getDatabase(
            context: Context
        ): TaskDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDataBase::class.java,
                    "TaskDataBase"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
