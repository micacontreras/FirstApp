package com.example.firstapp.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.showDialog
import kotlinx.android.synthetic.main.fragment_tasks.*

/**
 * A simple [Fragment] subclass.
 */
class TasksFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_tasks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        taskViewModel.getAllTasks()

        registerListener()
        registerObservers()
    }

    private fun registerObservers() {
        taskViewModel.allTasks?.observe(viewLifecycleOwner, Observer { task ->
            task_progress_bar.visibility = View.INVISIBLE
            if (task != null) {
                if (task.isNotEmpty()) {
                    adapter.setItem(task)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty List",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                task_recycler_view.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(requireContext())
        task_recycler_view.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    private fun registerListener() {
        task_add.setOnClickListener {
            findNavController().navigate(TasksFragmentDirections.navigateToDetail())
        }

        adapter.onClick = {
            Bundle().apply { putString("TaskName", it.taskName) }
                .also { setFragmentResult("task", it) }
            findNavController().navigate(TasksFragmentDirections.navigateToDetail(it.taskName))
        }

        adapter.onLongClick = {
            showDialog(
                requireContext(),
                "Confirmation",
                "Are you sure that you want to delete this task?",
                "Ok",
                {
                    taskViewModel.delete(it.taskName)
                    taskViewModel.getAllTasks()
                },
                "Cancel"
            )
        }
    }
}
