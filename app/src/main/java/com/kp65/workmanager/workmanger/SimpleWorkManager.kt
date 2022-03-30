package com.kp65.workmanager.workmanger

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kp65.workmanager.MainActivity
import com.kp65.workmanager.R
import com.kp65.workmanager.extension.Constant

class SimpleWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val type = inputData.getInt(Constant.WORKER_TYPE, Constant.WM_TYPE_MANUAL)
        showNotification(type == Constant.WM_TYPE_MANUAL)

        return Result.success()
    }

    private fun showNotification(hasManualWM: Boolean) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack

            val flag =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_CANCEL_CURRENT

            getPendingIntent(0, flag)
            //getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val channelId =
            if (hasManualWM) Constant.CHANNEL_ID_ONE_TIME_WORK else Constant.CHANNEL_ID_PERIOD_WORK

        val notification = NotificationCompat.Builder(applicationContext, channelId).apply {
            setContentIntent(pendingIntent)
        }


        val id: Int = if (hasManualWM) 0 else 1
        val title: String = if (hasManualWM) "ONE TIME WORK" else "PERIOD WORK"
        val type: String = if (hasManualWM) "Manual" else "Period"
        val content = "$type Work Manager Notification Test"


        notification.setContentTitle(title)
        notification.setContentText(content)
        notification.priority = NotificationCompat.PRIORITY_HIGH
        notification.setCategory(NotificationCompat.CATEGORY_ALARM)
        notification.setSmallIcon(R.drawable.ic_notifications_black)
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        notification.setVibrate(longArrayOf(0, 100, 200, 300))

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(id, notification.build())
        }

    }

}