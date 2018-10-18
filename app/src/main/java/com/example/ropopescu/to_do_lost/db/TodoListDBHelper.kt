package com.example.ropopescu.to_do_lost.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.ropopescu.to_do_lost.db.TodoListDBContract.DATABASE_NAME
import com.example.ropopescu.to_do_lost.db.TodoListDBContract.DATABASE_VERSION

class TodoListDBHelper(context: Context): SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    private val SQL_CREATE_ENTRIES = "CREATE TABLE " + TodoListDBContract.TodoListItem.TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TodoListDBContract.TodoListItem.COLUMN_NAME_TASK + " TEXT, " +
            TodoListDBContract.TodoListItem.COLUMN_NAME_DEADLINE + " TEXT, " +
            TodoListDBContract.TodoListItem.COLUMN_NAME_COMPLETED + " INTEGER)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TodoListDBContract.TodoListItem.TABLE_NAME

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addNewTask(task: Task): Task {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_TASK, task.taskDetails)
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_DEADLINE, task.taskDeadline)
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_COMPLETED, task.completed)

        val taskId = db.insert(TodoListDBContract.TodoListItem.TABLE_NAME, null, values)
        task.taskId = taskId

        return task
    }

    fun retrieveTaskList(): ArrayList<Task> {
       val db = this.readableDatabase

        val projection = arrayOf(BaseColumns._ID,
                TodoListDBContract.TodoListItem.COLUMN_NAME_TASK,
                TodoListDBContract.TodoListItem.COLUMN_NAME_DEADLINE,
                TodoListDBContract.TodoListItem.COLUMN_NAME_COMPLETED)
        val cursor = db.query(TodoListDBContract.TodoListItem.TABLE_NAME, projection,
                null, null, null, null, null)

        val taskList = ArrayList<Task>()

        while(cursor.moveToNext()) {
            val task = Task(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TodoListDBContract.TodoListItem.COLUMN_NAME_TASK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TodoListDBContract.TodoListItem.COLUMN_NAME_DEADLINE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TodoListDBContract.TodoListItem.COLUMN_NAME_COMPLETED)))
            taskList.add(task)
        }
        cursor.close()

        return taskList
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_TASK, task.taskDetails)
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_DEADLINE, task.taskDeadline)
        values.put(TodoListDBContract.TodoListItem.COLUMN_NAME_COMPLETED, task.completed)

        val selection = BaseColumns._ID + " = ?"
        val selectionArgs = arrayOf(task.taskId.toString())
        db.update(TodoListDBContract.TodoListItem.TABLE_NAME, values, selection, selectionArgs)
    }

    fun deleteTask(task: Task) {
        val db = this.writableDatabase
        val selection = BaseColumns._ID + " = ?"
        val selectionArgs = arrayOf(task.taskId.toString())
        db.delete(TodoListDBContract.TodoListItem.TABLE_NAME, selection, selectionArgs)
    }
}