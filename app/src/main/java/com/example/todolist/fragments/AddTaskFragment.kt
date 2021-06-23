package com.example.todolist.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.AddEditTaskViewModel
import com.example.todolist.databinding.FragmentAddTaskBinding
import com.example.todolist.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddTaskFragment  : Fragment(R.layout.fragment_add_task) {

    private val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddTaskBinding.bind(view)

        binding.apply {
            taskName.setText(viewModel.taskName)
            checkBox.isChecked = viewModel.taskImportance
            checkBox.jumpDrawablesToCurrentState()
            dateCreated.isVisible = viewModel.task != null
            dateCreated.text = "Created on : ${viewModel.task?.creationTime}"

            taskName.addTextChangedListener{
                viewModel.taskName = it.toString()
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            saveTask.setOnClickListener {
                viewModel.onSaveClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addTaskEvent.collect { event ->
                when(event){
                    is AddEditTaskViewModel.AddTaskEvent.NavigateBackToResult -> {
                        binding.taskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()

                    }
                    is AddEditTaskViewModel.AddTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }
}