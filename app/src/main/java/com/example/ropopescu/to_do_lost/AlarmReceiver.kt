package com.example.ropopescu.to_do_lost

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ropopescu.to_do_lost.alarms.AlarmService

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("onReceive", "intent$intent")
        val service = Intent(context, AlarmService::class.java)
        context?.startService(service)
    }
}