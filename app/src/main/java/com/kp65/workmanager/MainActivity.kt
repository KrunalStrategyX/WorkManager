package com.kp65.workmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.kp65.workmanager.work.NotifyWork
import com.kp65.workmanager.work.NotifyWork.Companion.MANUAL_WORKER_NAME
import com.kp65.workmanager.work.NotifyWork.Companion.NOTIFICATION_ID
import com.kp65.workmanager.work.NotifyWork.Companion.PERIODIC_WORKER_NAME
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    var data: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationID()

    }

    private fun createNotificationID() {
        data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
    }

    /**
     * CREATE PERIODIC TIME WORK MANAGER
     * */
    fun autoSync(view: View) {
        setAutoNotification()
    }

    private fun setAutoNotification() {

        val req = PeriodicWorkRequest.Builder(NotifyWork::class.java, 20, TimeUnit.MINUTES)
            .setInputData(data!!)
            .addTag(PERIODIC_WORKER_NAME)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                PERIODIC_WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                req
            )
    }

    /**
     * CREATE MANUAL WORK MANAGER
     * */
    fun onManualNotification(view: View) {
        setManualNotification()
    }

    private fun setManualNotification() {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(2, TimeUnit.MINUTES).setInputData(data!!).build()


        WorkManager.getInstance(this)
            .beginUniqueWork(MANUAL_WORKER_NAME, ExistingWorkPolicy.KEEP, notificationWork)
            .enqueue()


    }
}
