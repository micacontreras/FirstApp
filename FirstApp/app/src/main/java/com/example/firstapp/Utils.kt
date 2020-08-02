package com.example.firstapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksEntity
import java.util.*
import kotlin.collections.ArrayList

fun showDialog(
    context: Context,
    title: String,
    message: String,
    positiveButton: String,
    positiveAction: (() -> Unit)? = null,
    negativeButton: String? = null
) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButton) { _, _ -> positiveAction?.invoke() }
        setNegativeButton(negativeButton) { _, _ -> }
        create()
        show()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTasks(context: Context, listTasks: List<Tasks>): List<Tasks> {
    val newList: MutableList<Tasks> = ArrayList()
    listTasks.forEach {
        val dateCal = GregorianCalendar()
        val timeCal = GregorianCalendar()

        dateCal.time = it.startDate
        timeCal.time = it.startTime

        val year = dateCal[Calendar.YEAR]
        val month = dateCal[Calendar.MONTH]
        val day = dateCal[Calendar.DAY_OF_MONTH]

        val hour = timeCal[Calendar.HOUR_OF_DAY]
        val minute = timeCal[Calendar.MINUTE]
        val second = timeCal[Calendar.SECOND]

        val dateTimeCal = GregorianCalendar(year, month, day, hour, minute, second)

        if (dateTimeCal.timeInMillis <= System.currentTimeMillis()) {
            it.status = context.getString(R.string.inProgress)
            newList.add(it)
        }
    }
    return newList
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTasksEntity(context: Context, listTasksEntity: List<TasksEntity>): List<TasksEntity> {
    val newList: MutableList<TasksEntity> = ArrayList()

    listTasksEntity.forEach {
        val dateCal = GregorianCalendar()
        val timeCal = GregorianCalendar()

        dateCal.time = it.startDate
        timeCal.time = it.startTime

        val year = dateCal[Calendar.YEAR]
        val month = dateCal[Calendar.MONTH]
        val day = dateCal[Calendar.DAY_OF_MONTH]

        val hour = timeCal[Calendar.HOUR_OF_DAY]
        val minute = timeCal[Calendar.MINUTE]
        val second = timeCal[Calendar.SECOND]

        val dateTimeCal = GregorianCalendar(year, month, day, hour, minute, second)

        if (dateTimeCal.timeInMillis <= System.currentTimeMillis()) {
            it.status = context.getString(R.string.inProgress)
            newList.add(it)
        }
    }
    return newList
}