package com.example.ropopescu.to_do_lost.db.asyncdb

import android.os.AsyncTask
import com.example.ropopescu.to_do_lost.db.AppDatabase
import com.example.ropopescu.to_do_lost.domain.Task

class DeleteTaskAsyncTask(private val database: AppDatabase?, private val task: Task)
    : AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?) {
        return database?.taskDao()?.deleteTask(task)!!
    }
}