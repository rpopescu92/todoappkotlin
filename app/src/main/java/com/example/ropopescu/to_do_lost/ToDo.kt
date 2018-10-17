package com.example.ropopescu.to_do_lost

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

import kotlinx.android.synthetic.main.activity_to_do.*

class ToDo : AppCompatActivity(), NewTaskDialogListener {

    private var todoListItems = ArrayList<String>()
    private var listView: ListView? = null
    private var listAdapter: ArrayAdapter<String>? = null
    private var showMenuItems = false
    private var selectedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        setSupportActionBar(toolbar)

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

    override fun onDialogPositiveClick(dialog: DialogFragment, task: String) {
        if ("newtask" == dialog.tag) {
            todoListItems.add(task)
            listAdapter?.notifyDataSetChanged()

            Snackbar.make(fab, "Task Added",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()

        } else if("updatetask" == dialog.tag) {
            todoListItems[selectedItem] = task
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
                        NewTaskDialogFragment.newInstance(R.string.update_task_dialog_title, todoListItems[selectedItem])
                updateFragment.show(supportFragmentManager, "updatetask")
            } else if (R.id.delete_item == item?.itemId) {
                todoListItems.removeAt(selectedItem)
                listAdapter?.notifyDataSetChanged()
                selectedItem = -1
                Snackbar.make(fab, "Task deleted",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewTaskUI() {
        val newFragment = NewTaskDialogFragment.newInstance(R.string.add_new_task_dialog_title, "")
        newFragment.show(supportFragmentManager, "newtask")
    }

    private fun showUpdateTaskUI(selected: Int) {
        selectedItem = selected
        showMenuItems = true
        invalidateOptionsMenu()
    }

    private fun populateListView() {
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoListItems)
        listView?.adapter = listAdapter
    }

}
