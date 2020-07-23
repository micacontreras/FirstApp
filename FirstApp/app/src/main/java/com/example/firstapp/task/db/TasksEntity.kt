package com.example.firstapp.task.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TasksEntity(
    @PrimaryKey(name = "taskName") val taskName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "startTime") val startTime: Date,
    @ColumnInfo(name = "colorEvent") val colorEvent: String,
    @ColumnInfo(name = "colorEventInt") val colorEventInt: Int
)