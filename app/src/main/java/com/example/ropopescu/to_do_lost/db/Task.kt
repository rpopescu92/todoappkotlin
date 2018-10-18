package com.example.ropopescu.to_do_lost.db

class Task {
    var taskId: Long? = null
    var taskDetails: String? = null
    var taskDeadline: String? = null
    var completed: Int = 0

    constructor(taskDetails: String?, taskDeadline: String?) {
        this.taskDetails = taskDetails
        this.taskDeadline = taskDeadline
    }

    constructor(taskId: Long?, taskDetails: String?, taskDeadline: String?, completed: Int) {
        this.taskId = taskId
        this.taskDetails = taskDetails
        this.taskDeadline = taskDeadline
        this.completed = completed
    }


    override fun toString(): String {
        return taskDetails + ""
    }

}