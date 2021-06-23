package com.example.todolist.data

import android.view.AbsSavedState
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.activities.ADD_TASK_RESULT_OK
import com.example.todolist.activities.EDIT_TASK_RESULT_OK
import com.example.todolist.roomDatabase.Dao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val dao : Dao,
    @Assisted private val state: SavedStateHandle
) :ViewModel(){


    private val addTaskEventChannel = Channel<AddTaskEvent>()
    val addTaskEvent=addTaskEventChannel.receiveAsFlow()

    //on pressing ok floating
    fun onSaveClick() {
        if (taskName.isBlank()){
            //message
            shortMessage("Name cannot be empty")
            return
        }
        if (task !=null){
            val updateTask = task.copy(name = taskName,important = taskImportance)
            updatedTask(updateTask)
        }else{
            val newTask=Task(name = taskName,important = taskImportance)
            createTask(newTask)
        }
    }

    private fun createTask(newTask: Task) = viewModelScope.launch{
        dao.insert(newTask)
        addTaskEventChannel.send(AddTaskEvent.NavigateBackToResult(ADD_TASK_RESULT_OK))
    }

    private fun updatedTask(updateTask: Task)=viewModelScope.launch {
        dao.update(updateTask)
        addTaskEventChannel.send(AddTaskEvent.NavigateBackToResult(EDIT_TASK_RESULT_OK))
    }

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name?: ""
    set(value) {
        field = value
        state.set("taskName", value)
    }

    var taskImportance = state.get<Boolean>("taskName") ?: task?.important?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }
    private fun shortMessage(text: String) = viewModelScope.launch{
        addTaskEventChannel.send(AddTaskEvent.ShowInvalidInputMessage(text))
    }

    sealed class AddTaskEvent{
        data class ShowInvalidInputMessage(val msg: String):AddTaskEvent()
        data class NavigateBackToResult(val result:Int): AddTaskEvent()
}


}