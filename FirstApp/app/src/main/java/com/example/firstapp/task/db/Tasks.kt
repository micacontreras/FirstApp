package com.example.firstapp.task.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Tasks(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "taskName") val taskName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "startDate") val startDate: Date,
    @ColumnInfo(name = "endDate") val endDate: Date,
    @ColumnInfo(name = "colorEvent") val colorEvent: String
    )