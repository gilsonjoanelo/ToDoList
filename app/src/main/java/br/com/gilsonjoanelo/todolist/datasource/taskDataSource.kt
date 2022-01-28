package br.com.gilsonjoanelo.todolist.datasource

import br.com.gilsonjoanelo.todolist.models.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getLit() = list.toList()

    fun salvarTask(task: Task) {
        if(task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        } else {
            list.remove(task)
            list.add(task)
        }
    }

    fun deleteTask(task: Task) {
        if( task == null) return
        list.remove(task)
    }

    fun findById(taskId: Int)  = list.find { it.id == taskId }
}