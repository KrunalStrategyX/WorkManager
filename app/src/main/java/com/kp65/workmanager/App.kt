package com.kp65.workmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.kp65.workmanager.extension.Constant.CHANNEL_ID_ONE_TIME_WORK
import com.kp65.workmanager.extension.Constant.CHANNEL_ID_PERIOD_WORK

class App() : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelPeriodic = NotificationChannel(
                CHANNEL_ID_PERIOD_WORK,
                "Period Notification Work Request",
                importance
            )
            channelPeriodic.description = "Periodic Notification Work"
            val channelInstant = NotificationChannel(
                CHANNEL_ID_ONE_TIME_WORK,
                "Manual Notification Work Request",
                importance
            )
            channelInstant.description = "Manual Notification Work"
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager!!.createNotificationChannel(channelPeriodic)
            notificationManager.createNotificationChannel(channelInstant)
        }
    }
}