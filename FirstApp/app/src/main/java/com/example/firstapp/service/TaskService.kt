package com.example.firstapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.firstapp.MainActivity
import com.example.firstapp.R
import com.example.firstapp.getCurrentTasks
import com.example.firstapp.task.db.Tasks


@Suppress("IMPLICIT_CAST_TO_ANY", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class TaskService : Service() {

    private val binder = LocalBinder()
    private var listTasks: MutableList<Tasks> = ArrayList()

    lateinit var mgr: NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBind(intent: Intent): IBinder? {
        listTasks = intent.getParcelableArrayListExtra("Tasks")
        val newList = getCurrentTasks(applicationContext, listTasks)

        createChannel()

        if (newList.isNotEmpty()) {
            newList.forEach {
                buildNotification(it.taskName)
            }
        }
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mgr = applicationContext.getSystemService(NotificationManager::class.java)

    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && mgr.getNotificationChannel("CHANNEL_WHATEVER") == null
        ) {
            mgr.createNotificationChannel(
                NotificationChannel(
                    "CHANNEL_WHATEVER",
                    "Whatever",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): TaskService = this@TaskService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(taskName: String) {

        lateinit var pendingIntent: PendingIntent

        val launchIntent =
            packageManager.getLaunchIntentForPackage("com.example.secapp")
        launchIntent?.let {
            val intent: Intent = launchIntent
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        }

        val builder = NotificationCompat.Builder(applicationContext, "CHANNEL_WHATEVER")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("$taskName  ${getString(R.string.notification_text)}")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setGroup("Group")

        mgr.notify(1, builder.build())

    }

    fun stopService() {
        Log.d("Service", "stoped")
        stopSelf()
    }

}