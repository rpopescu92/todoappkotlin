package com.example.ropopescu.to_do_lost

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.EditText

class NewTaskDialogFragment : DialogFragment() {

    interface NewTaskDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, task: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    var newTaskDIalogListener: NewTaskDialogListener? = null

    companion object {
        fun newInstance(title: Int, selected: String?): NewTaskDialogFragment {
            val newTaskDialogFragment = NewTaskDialogFragment()
            val args = Bundle()
            args.putInt("dialog_title", title)
            args.putString("selected_item", selected)
            newTaskDialogFragment.arguments = args
            return newTaskDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getInt("dialog_title")
        val selectedText = arguments?.getString("selected_item")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title!!)

        val dialogView = activity?.layoutInflater?.inflate(R.layout.dialog_new_task, null)
        val task = dialogView?.findViewById<EditText>(R.id.task)
        task?.setText(selectedText)

        builder.setView(dialogView)
                .setPositiveButton(R.string.save, {dialog, id ->
                    newTaskDIalogListener?.onDialogPositiveClick(this,
                            task?.text.toString());
                })
                .setNegativeButton(android.R.string.cancel, {dialog, id ->
                    newTaskDIalogListener?.onDialogNegativeClick(this)
                })
        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            newTaskDIalogListener = context as NewTaskDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement NewTaskDialogListner")
        }
    }

}
