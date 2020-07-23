package com.example.firstapp.task.db

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Tasks(
    val taskName: String,
    val description: String,
    val startDate: Date,
    val startTime: Date,
    val colorEvent: String,
    val colorEventInt: Int
) : Parcelable