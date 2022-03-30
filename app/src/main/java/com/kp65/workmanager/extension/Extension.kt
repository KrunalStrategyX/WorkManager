package com.kp65.workmanager.extension

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat.getDrawable
import androidx.work.*
import com.kp65.workmanager.enums.AutoSyncInterval
import com.kp65.workmanager.extension.Constant.AUTO_NOTIFICATION_WORK_NAME
import com.kp65.workmanager.extension.Constant.WM_TYPE_MANUAL
import com.kp65.workmanager.extension.Constant.WM_TYPE_PERIODIC
import com.kp65.workmanager.extension.Constant.WORKER_TYPE
import com.kp65.workmanager.workmanger.SimpleWorkManager

fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
    val drawable = getDrawable(this, drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

object Constant {
    const val TAG = "NotificationWorker"
    const val AUTO_NOTIFICATION_WORK_NAME = "AUTO_NOTIFICATION_WORK_NAME"
    const val MANUAL_NOTIFICATION_WORK_NAME = "MANUAL_NOTIFICATION_WORK_NAME"


    const val CHANNEL_ID_PERIOD_WORK = "PERIODIC_APP_UPDATES"
    const val CHANNEL_ID_ONE_TIME_WORK = "MANUAL_APP_UPDATES"

    const val WORKER_TYPE = "WORKER_TYPE"
    const val WM_TYPE_PERIODIC = 1
    const val WM_TYPE_MANUAL = 0


    const val NOTIFICATION_NAME = "WORK_MANGER"
    const val NOTIFICATION_CHANNEL = "WORK_MANAGER_CHANNEL_01"
}

object WorkerConstraint {
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true)
        .build()
}

fun RunPeriodicWorker(context: Activity) {

    val interval: AutoSyncInterval = AutoSyncInterval.TWENTY_MINUTES

    val periodWork =
        PeriodicWorkRequest.Builder(SimpleWorkManager::class.java, interval.interval, interval.unit)
            .addTag(AUTO_NOTIFICATION_WORK_NAME)
            .setInputData(Data.Builder().putInt(WORKER_TYPE, WM_TYPE_PERIODIC).build())
            .setConstraints(WorkerConstraint.constraints)
            .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        AUTO_NOTIFICATION_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        periodWork
    )
}

fun RunManualWorker(context: Activity) {

    val manualWork = OneTimeWorkRequest.Builder(SimpleWorkManager::class.java)
        .setConstraints(WorkerConstraint.constraints)
        .setInputData(Data.Builder().putInt(WORKER_TYPE, WM_TYPE_MANUAL).build())

    WorkManager.getInstance(context).enqueue(manualWork.build())
}


