package com.example.firstapp.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.firstapp.R
import com.example.firstapp.provider.TaskProvider
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksEntity
import java.util.*
import kotlin.collections.ArrayList


@Suppress("IMPLICIT_CAST_TO_ANY")
open class TaskService : Service() {

    private val binder = LocalBinder()
    private var listTasks: MutableList<Tasks> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBind(intent: Intent): IBinder? {
        listTasks = intent.getParcelableArrayListExtra("Tasks")
        getCurrentTasks()
        return binder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        getCurrentTasks()


    }

    inner class LocalBinder : Binder() {
        // Return this instance so clients can call public methods
        fun getService(): TaskService = this@TaskService
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildNotification(taskName: String) {
        val open = "Open second app"
        registerReceiver(openReceiver, IntentFilter(open))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(open), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        val channelId = createNotificationChannel("my_service", "My Background Service")

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(taskName + getString(R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.logo)
        startForeground(1, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private var openReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            //unregisterReceiver(this)
            //stopSelf()
        }
    }

    fun stopService() {
        unregisterReceiver(openReceiver)
        Log.d("Service", "stoped")
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTasks() {
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

            if(dateTimeCal.timeInMillis <= System.currentTimeMillis()){
                buildNotification(it.taskName)
            }
        }

    }


    companion object {
        private val TAG = TaskService::class.java.simpleName

    }

}