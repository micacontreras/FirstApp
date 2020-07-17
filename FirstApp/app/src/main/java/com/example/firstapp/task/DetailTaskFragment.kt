package com.example.firstapp.task

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firstapp.R
import com.example.firstapp.task.db.Tasks
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_detail_task.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DetailTaskFragment : Fragment() {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    private lateinit var titleLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout

    private var index = 0
    private var colorEvent: String? = null

    private val args: DetailTaskFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailTaskViewModel = ViewModelProvider(this).get(DetailTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewLayout = inflater.inflate(R.layout.fragment_detail_task, container, false)
        titleLayout = viewLayout.findViewById(R.id.detail_title) as TextInputLayout
        descriptionLayout = viewLayout.findViewById(R.id.detail_description) as TextInputLayout
        return viewLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        colorEvent = getString(R.string.defaultColor)

        loadData()

        registerListeners()
    }

    /*override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener("task", this,
            FragmentResultListener { _, bundle ->
                bundle.getString("TaskName").also {
                    val response = detailTaskViewModel.getTask(it.toString())
                    response?.let { response -> setupScreen(response) }
                }
            })
    }*/

    private fun loadData() {
        if(args.TaskName != "null" && args.TaskName != null ){
            detailTaskViewModel.getTask(args.TaskName.toString())?.observe(viewLifecycleOwner, androidx.lifecycle.Observer { task ->
                task.let {  setupScreen(task) }
            })


            //val response = detailTaskViewModel.getTask(it.TaskName.toString())
            //response?.let { response ->
              //  setupScreen(response) }
        }
    }

    private fun setupScreen(task: Tasks) {
        detail_title.editText?.setText(task.taskName)
        detail_title_et.setText(task.taskName)
        detail_description.editText?.setText(task.description)
        detail_date_start.setText(task.startDate.toString())
        detail_date_end.setText(task.endDate.toString())
        detail_color_image.background.setColorFilter(Color.parseColor(colorEvent), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun registerListeners() {
        detail_toolbar.menu.getItem(0).setOnMenuItemClickListener {
            if(!confirmInput()){
                val photo = createRequest()
                detailTaskViewModel.insert(photo)
                findNavController().navigateUp()
            }
            true
        }

        detail_color.setOnClickListener { displayColorPalette() }
    }

    private fun validateTitle(): Boolean {
        val title: String = detail_title.editText?.text.toString().trim()
        return if (title.isEmpty()) {
            titleLayout.error = "Este campo es obligatorio"
            false
        } else {
            titleLayout.error = null
            true
        }
    }

    private fun validateDescription(): Boolean {
        val description: String = detail_description.editText?.text.toString().trim()
        return if (description.isEmpty()) {
            descriptionLayout.error = "Este campo es obligatorio"
            false
        } else {
            descriptionLayout.error = null
            true
        }
    }

    private fun confirmInput(): Boolean =
        !validateTitle() || !validateDescription()

    private fun createRequest(): Tasks {
        val title = detail_title.editText?.text.toString().trim()
        val description = detail_description.editText?.text.toString().trim()
        val startDay: Date = Date()
        val endDate: Date = Date()
        return Tasks(index, title, description, startDay, endDate, colorEvent.toString())
    }

    private fun displayColorPalette() {
        MaterialColorPickerDialog
            .Builder(requireContext())
            .setColorSwatch(ColorSwatch._300)
            .setColorListener(object : ColorListener {
                override fun onColorSelected(color: Int, colorHex: String) {
                    colorEvent = colorHex
                    detail_color_image.background.setColorFilter(Color.parseColor(colorHex), PorterDuff.Mode.SRC_ATOP)

                }
            })
            .showBottomSheet(requireFragmentManager())
    }

}
