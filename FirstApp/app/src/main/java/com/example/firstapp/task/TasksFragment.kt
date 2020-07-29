package com.example.firstapp.task

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.database.Cursor
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.provider.TaskProvider
import com.example.firstapp.service.TaskService
import com.example.firstapp.showDialog
import com.example.firstapp.task.db.Tasks
import com.example.firstapp.task.db.TasksEntity
import kotlinx.android.synthetic.main.fragment_tasks.*

/**
 * A simple [Fragment] subclass.
 */
class TasksFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    private lateinit var taskService: TaskService


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
        provider()
    }

    fun provider() {
        androidx.loader.app.LoaderManager.getInstance(this)
            .initLoader(LOADER_TASKS, null, loaderCallbacks)
    }
    private val loaderCallbacks =
        object : androidx.loader.app.LoaderManager.LoaderCallbacks<Cursor?> {
            override fun onCreateLoader(id: Int, @Nullable args: Bundle?): Loader<Cursor?> {
                return CursorLoader(
                    requireContext(),
                    TaskProvider.URI, arrayOf(TasksEntity.COLUMN_NAME),
                    null, null, null
                )
            }

            override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {}

            override fun onLoaderReset(loader: Loader<Cursor?>) {}
        }

    private fun registerObservers() {
        taskViewModel.allTasksEntity?.observe(viewLifecycleOwner, Observer { task ->
            task_progress_bar.visibility = View.INVISIBLE
            if (task != null) {
                if (task.isNotEmpty()) {
                    adapter.setItem(task)
                    startTaskService(task)
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
                    taskViewModel.delete(it.taskName!!)
                    taskViewModel.getAllTasks()
                },
                "Cancel"
            )
        }
    }

    private fun startTaskService(tasks: List<TasksEntity>) {
        val listTasks: MutableList<Tasks> = ArrayList()
        tasks.forEach {
            val taskModel = Tasks(it.id, it.taskName.toString(), it.description.toString(), it.startDate, it.startTime, it.colorEvent.toString(), it.colorEventInt!!)
            listTasks.add(taskModel)
        }

        Intent(requireContext(), TaskService::class.java).also { intent ->
            intent.putParcelableArrayListExtra("Tasks", listTasks as ArrayList<out Parcelable?>?)
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TaskService.LocalBinder
            taskService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    companion object{
        private const val LOADER_TASKS = 1
    }
}
