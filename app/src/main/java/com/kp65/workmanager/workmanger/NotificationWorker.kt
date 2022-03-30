package com.kp65.workmanager.workmanger

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.kp65.workmanager.MainActivity
import com.kp65.workmanager.R
import com.kp65.workmanager.enums.AutoSyncInterval
import com.kp65.workmanager.extension.Constant.AUTO_NOTIFICATION_WORK_NAME
import com.kp65.workmanager.extension.Constant.CHANNEL_ID_ONE_TIME_WORK
import com.kp65.workmanager.extension.Constant.CHANNEL_ID_PERIOD_WORK
import com.kp65.workmanager.extension.Constant.MANUAL_NOTIFICATION_WORK_NAME
import com.kp65.workmanager.extension.Constant.WM_TYPE_MANUAL
import com.kp65.workmanager.extension.Constant.WM_TYPE_PERIODIC
import com.kp65.workmanager.extension.Constant.WORKER_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        /*val type = inputData.getInt(WORKER_TYPE, WM_TYPE_MANUAL)
        showNotification(type == WM_TYPE_MANUAL)*/

        return@withContext Result.success()
    }

    private fun showNotification(hasManualWM: Boolean) {

        val id: Int = if (hasManualWM) 0 else 1
        val title: String = if (hasManualWM) "ONE TIME WORK" else "PERIOD WORK"
        val content: String =
            (if (hasManualWM) "Manual" else "Period") + " Work Manager Notification Test"


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
        }

        /*val pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    applicationContext,
                    id,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    applicationContext,
                    id,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            }*/

        val channelId = if (hasManualWM) CHANNEL_ID_ONE_TIME_WORK else CHANNEL_ID_PERIOD_WORK

        val notification = NotificationCompat.Builder(applicationContext, channelId).apply {
            setContentIntent(pendingIntent)
        }


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


    companion object {

        @JvmStatic
        fun getConstraints() = Constraints.Builder()
            //.setRequiresCharging(false)
            //.setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            //.setRequiresDeviceIdle(false)
            .build()

        @JvmStatic
        fun manual(
            context: Context,
        ) {

            val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                .addTag(MANUAL_NOTIFICATION_WORK_NAME)
                .setInputData(getData(true))
                .build()

            WorkManager.getInstance(context)
                .enqueue(request)

        }

        @JvmStatic
        fun scheduleAutoWallpaper(
            applicationContext: Context,
            workPolicy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP
        ) {

            val interval: AutoSyncInterval = AutoSyncInterval.TWENTY_MINUTES

            val periodWork = PeriodicWorkRequestBuilder<NotificationWorker>(
                interval.interval,
                interval.unit
            ).setConstraints(getConstraints())
                .addTag(AUTO_NOTIFICATION_WORK_NAME)
                .setInputData(getData(false))
                .build()

            //Can store requestId for future usage
            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                    AUTO_NOTIFICATION_WORK_NAME,
                    workPolicy,
                    periodWork
                )
        }

        @JvmStatic
        fun cancelAutoWallpaper(applicationContext: Context) {
            //Can use requestId instead of workName
            WorkManager.getInstance(applicationContext)
                .cancelUniqueWork(AUTO_NOTIFICATION_WORK_NAME)
        }

        private fun getData(isManual: Boolean): Data {
            val type = if (isManual) WM_TYPE_MANUAL else WM_TYPE_PERIODIC
            return Data.Builder().putInt(WORKER_TYPE, type).build()
        }

    }


}