package com.example.ropopescu.to_do_lost.dao

import android.arch.persistence.room.*
import com.example.ropopescu.to_do_lost.db.TodoListDBContract
import com.example.ropopescu.to_do_lost.domain.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM " + TodoListDBContract.TodoListItem.TABLE_NAME)
    fun retrieveTaskList(): List<Task>

    @Insert
    fun addNewTask(task: Task): Long

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}