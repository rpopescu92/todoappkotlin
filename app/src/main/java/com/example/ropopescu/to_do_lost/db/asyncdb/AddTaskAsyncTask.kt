package com.example.ropopescu.to_do_lost.db.asyncdb

import android.os.AsyncTask
import com.example.ropopescu.to_do_lost.db.AppDatabase
import com.example.ropopescu.to_do_lost.domain.Task

class AddTaskAsyncTask(private val database: AppDatabase?, private val newTask: Task)
    : AsyncTask<Void, Void, Long>() {

    override fun doInBackground(vararg p0: Void?): Long {
        return database?.taskDao()?.addNewTask(newTask)!!
    }
}