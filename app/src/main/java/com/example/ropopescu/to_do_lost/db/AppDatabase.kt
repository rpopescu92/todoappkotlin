package com.example.ropopescu.to_do_lost.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.ropopescu.to_do_lost.dao.TaskDao
import com.example.ropopescu.to_do_lost.domain.Task

@Database(entities = arrayOf(Task::class), version = TodoListDBContract.DATABASE_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}