package com.example.ropopescu.to_do_lost.db.asyncdb

import android.os.AsyncTask
import com.example.ropopescu.to_do_lost.db.AppDatabase
import com.example.ropopescu.to_do_lost.domain.Task

class RetrieveTaskAsyncTask(private val database: AppDatabase?)
    : AsyncTask<Void, Void, List<Task>>() {

    override fun doInBackground(vararg p0: Void?): List<Task> {
           return database?.taskDao()?.retrieveTaskList()!!
    }


}