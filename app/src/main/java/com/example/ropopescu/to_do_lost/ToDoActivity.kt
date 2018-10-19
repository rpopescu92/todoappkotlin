package com.example.ropopescu.to_do_lost

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.ropopescu.to_do_lost.NewTaskDialogFragment.NewTaskDialogListener
import com.example.ropopescu.to_do_lost.db.AppDatabase
import com.example.ropopescu.to_do_lost.db.asyncdb.RetrieveTaskAsyncTask
import com.example.ropopescu.to_do_lost.db.TodoListDBContract
import com.example.ropopescu.to_do_lost.db.TodoListDBContract.DATABASE_NAME
import com.example.ropopescu.to_do_lost.db.asyncdb.AddTaskAsyncTask
import com.example.ropopescu.to_do_lost.db.asyncdb.DeleteTaskAsyncTask
import com.example.ropopescu.to_do_lost.db.asyncdb.UpdateTaskAsyncTask
import com.example.ropopescu.to_do_lost.domain.Task

import kotlinx.android.synthetic.main.activity_to_do.*

class ToDoActivity : AppCompatActivity(), NewTaskDialogListener {

    companion object  {
        const val NEW_TASK_TAG = "newtask"
        const val UPDATE_TASK_TAG = "updatetask"
    }

    private var todoListItems = ArrayList<Task>()
    private var listView: ListView? = null
    private var listAdapter: ArrayAdapter<Task>? = null
    private var showMenuItems = false
    private var selectedItem = -1
    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        setSupportActionBar(toolbar)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        listView = this.findViewById(R.id.listview)
        listView?.onItemClickListener =
                AdapterView.OnItemClickListener {parent, view, position, id -> showUpdateTaskUI(position)}
        populateListView()

        fab.setOnClickListener { showNewTaskUI()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
       dialog.dismiss()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, taskDetails: String) {
        if (NEW_TASK_TAG == dialog.tag) {
            val task = Task(taskDetails, "")
            val taskId = AddTaskAsyncTask(database, task).execute().get()
            task.taskId = taskId
            todoListItems.add(task)
            listAdapter?.notifyDataSetChanged()


            Snackbar.make(fab, "Task Added",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()

        } else if(UPDATE_TASK_TAG == dialog.tag) {
            todoListItems[selectedItem].taskDetails = taskDetails
            UpdateTaskAsyncTask(database, todoListItems[selectedItem]).execute().get()

            listAdapter?.notifyDataSetChanged()
            selectedItem = -1

            Snackbar.make(fab,"Task updated",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        listAdapter?.notifyDataSetChanged()
        Snackbar.make(fab, "Task Added Successfully", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.to_do_list_menu, menu)
        val editItem = menu?.findItem(R.id.edit_item)
        val deleteItem = menu?.findItem(R.id.delete_item)

        if(showMenuItems) {
            editItem?.isVisible = true
            deleteItem?.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(-1 != selectedItem) {
            if(R.id.edit_item == item?.itemId) {
                val updateFragment =
                        NewTaskDialogFragment.newInstance(R.string.update_task_dialog_title, todoListItems[selectedItem].taskDetails)
                updateFragment.show(supportFragmentManager, UPDATE_TASK_TAG)
            } else if (R.id.delete_item == item?.itemId) {
                val selectedTask = todoListItems[selectedItem]
                todoListItems.removeAt(selectedItem)
                DeleteTaskAsyncTask(database, selectedTask).execute()

                listAdapter?.notifyDataSetChanged()
                selectedItem = -1
                Snackbar.make(fab, "Task deleted",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show()
            } else if(R.id.complete_item == item?.itemId) {
                todoListItems[selectedItem].completed = 1


                Snackbar.make(fab, "Task completed",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    private fun showNewTaskUI() {
        val newFragment = NewTaskDialogFragment.newInstance(R.string.add_new_task_dialog_title, "")
        newFragment.show(supportFragmentManager, NEW_TASK_TAG)
    }

    private fun showUpdateTaskUI(selected: Int) {
        selectedItem = selected
        showMenuItems = true
        invalidateOptionsMenu()
    }

    private fun populateListView() {
        todoListItems = RetrieveTaskAsyncTask(database).execute().get() as ArrayList<Task>
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoListItems)
        listView?.adapter = listAdapter
    }

}
