package br.com.gilsonjoanelo.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.gilsonjoanelo.todolist.databinding.ActivityAddTaskBinding
import br.com.gilsonjoanelo.todolist.datasource.TaskDataSource
import br.com.gilsonjoanelo.todolist.extensions.format
import br.com.gilsonjoanelo.todolist.extensions.text
import br.com.gilsonjoanelo.todolist.models.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity(){

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)) {
           val taskId = intent.getIntExtra(TASK_ID, 0)
           val task = TaskDataSource.findById(taskId)?.let {
               binding.tilTitle.text = it.title
               binding.tilContent.text = it.content
               binding.tilHour.text = it.hour
               binding.tilDate.text = it.date
           };
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                val minute =if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                binding.tilHour.text = "${hour}:${minute}"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }
        binding.btnCanel.setOnClickListener {
            finish()
        }
        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title= binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                content = binding.tilContent.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.salvarTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}