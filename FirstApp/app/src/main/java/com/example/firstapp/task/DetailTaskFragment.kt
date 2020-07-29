package com.example.firstapp.task

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.firstapp.R
import com.example.firstapp.task.db.TasksEntity
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.textfield.TextInputLayout
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog
import com.philliphsu.bottomsheetpickers.time.grid.GridTimePickerDialog
import kotlinx.android.synthetic.main.fragment_detail_task.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DetailTaskFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    BottomSheetTimePickerDialog.OnTimeSetListener {

    private lateinit var detailTaskViewModel: DetailTaskViewModel

    private lateinit var titleLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout

    private var colorEvent: String? = null
    private var colorEventInt: Int? = null
    private var dateEvent = Date()
    private var timeEvent = Date()
    private var savedTaskName: String? = null

    private val calendar: Calendar = Calendar.getInstance()

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
        colorEventInt = -2039584

        loadData()
        loadDate(false)

        registerListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun registerListeners() {
        detail_toolbar.menu.getItem(0).setOnMenuItemClickListener {
            if (!confirmInput()) {
                val task = createRequest()
                detailTaskViewModel.insert(task)
                findNavController().navigateUp()
            }
            true
        }

        detail_color.setOnClickListener { displayColorPalette() }

        detail_date_start.setOnClickListener {
            val date: DatePickerDialog = BottomSheetDatePickerDialog.Builder(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
                .setHeaderColor(colorEventInt!!)
                .setThemeDark(true)
                .build()
            date.show(requireFragmentManager(), "Date")
        }

        detail_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        detail_hour_start.setOnClickListener {
            val grid = GridTimePickerDialog.Builder(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
                .setHeaderColor(colorEventInt!!)
                .setThemeDark(true)
                .build()
            grid.show(requireFragmentManager(), "Time")
        }
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

    private fun createRequest(): TasksEntity {
        val title = detail_title.editText?.text.toString().trim()
        val description = detail_description.editText?.text.toString().trim()
        val startDay: Date = dateEvent
        val startTime: Date = timeEvent
        return TasksEntity(
            0L,
            title,
            description,
            startDay,
            startTime,
            colorEvent.toString(),
            colorEventInt!!
        )
    }

    private fun loadDate(currentDate: Boolean, dateSaved: Date? = null): List<String> {
        var date = 0L
        val dateList: MutableList<String> = ArrayList()

        date = if (!currentDate) {
            System.currentTimeMillis()
        } else {
            dateSaved?.time!!
        }
        //Otra forma fecha
        val format: DateFormat = SimpleDateFormat("E, dd MMM yyyy")
        val dateFormat = format.format(date)
        dateList.add(dateFormat)

        //Otra forma hora
        val format2: DateFormat = SimpleDateFormat("hh:mm a")
        val timeFormat = format2.format(date)
        dateList.add(timeFormat)

        if(!currentDate){
            detail_date_start.text = dateFormat
            detail_hour_start.text = timeFormat
        }
        return dateList
    }

    private fun loadData() {
        if (args.TaskName != "null" && args.TaskName != null) {
            detailTaskViewModel.getTask(args.TaskName.toString())
                ?.observe(viewLifecycleOwner, androidx.lifecycle.Observer { task ->
                    savedTaskName = args.TaskName
                    task.let { setupScreen(task) }
                })
        }
    }

    private fun setupScreen(task: TasksEntity) {
        detail_title.editText?.setText(task.taskName)
        detail_title_et.setText(task.taskName)
        detail_description.editText?.setText(task.description)
        detail_date_start.text = loadDate(true, task.startDate)[0]
        detail_hour_start.text = loadDate(true, task.startTime)[1]

        dateEvent = task.startDate
        timeEvent = task.startTime

        colorEvent = task.colorEvent
        colorEventInt = task.colorEventInt
        detail_color_image.background.setColorFilter(
            Color.parseColor(colorEvent),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun displayColorPalette() {
        MaterialColorPickerDialog
            .Builder(requireContext())
            .setColorSwatch(ColorSwatch._300)
            .setColorListener(object : ColorListener {
                override fun onColorSelected(color: Int, colorHex: String) {
                    colorEvent = colorHex
                    colorEventInt = color
                    detail_color_image.background.setColorFilter(
                        Color.parseColor(colorHex),
                        PorterDuff.Mode.SRC_ATOP
                    )

                }
            })
            .showBottomSheet(requireFragmentManager())
    }

    override fun onDateSet(
        dialog: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val zoneTime = TimeZone.getTimeZone("America/Argentina/Buenos_Aires")
        val cal = Calendar.getInstance()
        calendar.timeZone = zoneTime
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = monthOfYear
        cal[Calendar.DAY_OF_MONTH] = dayOfMonth
        val format: DateFormat = SimpleDateFormat("E, dd MMM yyyy")
        dateEvent = cal.time
        detail_date_start.text = format.format(cal.time)
    }

    override fun onTimeSet(viewGroup: ViewGroup?, hourOfDay: Int, minute: Int) {
        val cal: Calendar = GregorianCalendar()
        cal[Calendar.HOUR_OF_DAY] = hourOfDay
        cal[Calendar.MINUTE] = minute

        val format2: DateFormat = SimpleDateFormat("hh:mm a")
        timeEvent = cal.time
        detail_hour_start.text = format2.format(cal.time)
    }


}
