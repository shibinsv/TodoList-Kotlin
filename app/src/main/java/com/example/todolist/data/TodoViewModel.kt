package com.example.todolist.data

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.activities.ADD_TASK_RESULT_OK
import com.example.todolist.activities.EDIT_TASK_RESULT_OK
import com.example.todolist.roomDatabase.Dao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TodoViewModel @ViewModelInject constructor(
    private val todoDao:Dao,
    @Assisted private val state : SavedStateHandle

    ) : ViewModel() {

    val tasks = todoDao.getTasks().asLiveData()


    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    fun onTaskSelected(task: Task)=viewModelScope.launch{

    }

    fun onAddNewTaskClick() = viewModelScope.launch{
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskCheckChanged(task: Task, isChecked: Boolean)=viewModelScope.launch {
        todoDao.update(task.copy(completed = isChecked))

    }

    fun onAddEditResult(result: Int) {
        when(result){
            ADD_TASK_RESULT_OK -> showConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showConfirmationMessage("Task updated")

        }

    }

    private fun showConfirmationMessage(s: String)=viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowConfirmationMessage(s))
    }


    sealed class TasksEvent{
        object NavigateToAddTaskScreen : TasksEvent()
        data class ShowConfirmationMessage(val msg :String): TasksEvent()
    }
}
