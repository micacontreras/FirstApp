package com.example.firstapp.task

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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

        registerObservers()
        registerListeners()
    }

    private fun registerListeners() {
        toolbar_save.setOnClickListener {
            if(confirmInput()){
                val photo = createRequest()
                detailTaskViewModel.insert(photo)
            }
        }

        detail_color.setOnClickListener { displayColorPalette() }
    }

    private fun registerObservers() {
        TODO("Not yet implemented")
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
            .showBottomSheet(fragmentManager!!)
    }

}
