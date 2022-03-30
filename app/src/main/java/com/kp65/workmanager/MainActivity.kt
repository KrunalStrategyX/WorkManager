package com.kp65.workmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kp65.workmanager.extension.RunManualWorker
import com.kp65.workmanager.extension.RunPeriodicWorker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * CREATE PERIODIC TIME WORK MANAGER
     * */
    fun autoSync(view: View) {
        setAutoNotification()
    }

    private fun setAutoNotification() {
        RunPeriodicWorker(this)
        //NotificationWorker.scheduleAutoWallpaper(this)
    }

    /**
     * CREATE MANUAL WORK MANAGER
     * */
    fun onManualNotification(view: View) {
        setManualNotification()
    }

    private fun setManualNotification() {
        RunManualWorker(this)
        //NotificationWorker.manual(this)
    }
}
