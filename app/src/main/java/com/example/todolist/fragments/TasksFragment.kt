package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.TasksAdapter
import com.example.todolist.data.Task
import com.example.todolist.data.TodoViewModel
import com.example.todolist.databinding.FragmentTasksBinding
import com.example.todolist.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {

private val viewModel : TodoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)
        val todoAdapter = TasksAdapter(this)

        binding.apply {
            tasksRecycler.apply {
                adapter=todoAdapter
                layoutManager =LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabCreateTask.setOnClickListener {
                viewModel.onAddNewTaskClick()
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner){
            todoAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect{event->
                when(event){
                    is TodoViewModel.TasksEvent.ShowConfirmationMessage ->{
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    TodoViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddTaskFragment()
                      findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setFragmentResultListener("add_edit_request"){_,bundle ->
            val result =bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)

        }

    }


    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBocClicked(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckChanged(task,isChecked)
    }
}