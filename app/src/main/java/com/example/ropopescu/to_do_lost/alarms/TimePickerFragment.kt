package com.example.ropopescu.to_do_lost.alarms

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import com.example.ropopescu.to_do_lost.AlarmReceiver
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d("on Time set", "hourOfDay: $hourOfDay minute: $minute")

        Toast.makeText(activity, "Reminder set succesfully", Toast.LENGTH_LONG).show()

        val intent = Intent(activity, AlarmReceiver::class.java)
        intent.putExtra(ARG_TASK_DECSRIPTION, "task-desc")

        val alarmIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)
        val alarmManag = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        alarmManag.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }

    companion object {
        val ARG_TASK_DECSRIPTION = "task-description"

        fun newInstance(taskDescription: String): TimePickerFragment {
            val fragment = TimePickerFragment()
            val args = Bundle()
            args.putString(ARG_TASK_DECSRIPTION, taskDescription)
            fragment.arguments = args

            return fragment
        }
    }
}