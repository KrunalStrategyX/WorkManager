package com.kp65.workmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.kp65.workmanager.work.NotifyWork
import com.kp65.workmanager.work.NotifyWork.Companion.NOTIFICATION_ID
import com.kp65.workmanager.work.NotifyWork.Companion.NOTIFICATION_WORK
import kotlinx.android.synthetic.main.activity_main.*
import ru.ifr0z.notify.R
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userInterface()
    }

    private fun userInterface() {
        setSupportActionBar(toolbar)

        val titleNotification = getString(R.string.notification_title)
        collapsing_toolbar_l.title = titleNotification
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        /*val notificationWork =  OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()*/


    }


    fun autoSync(view: View) {
        setAutoNotification()
    }

    private fun setAutoNotification() {
        val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()

        val req = PeriodicWorkRequest.Builder(NotifyWork::class.java, 20, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag(NOTIFICATION_WORK)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                NOTIFICATION_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                req
            )
    }
}
