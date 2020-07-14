package com.example.firstapp.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
         inflater.inflate(R.layout.fragment_tasks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerListener()
        registerObservers()
    }

    override fun onStart() {
        super.onStart()

        setupRecyclerView()
    }

    private fun registerObservers() {
        taskViewModel.allTasks.observe(viewLifecycleOwner, Observer { history ->
            history?.let {
                task_progress_bar.visibility = View.INVISIBLE
                if (it.isNotEmpty()) {
                    adapter.setItem(it)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Empty List",
                       Toast.LENGTH_LONG
                    ).show()
                }
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
            TasksFragmentDirections.navigateToDetail()
        }
    }
}
