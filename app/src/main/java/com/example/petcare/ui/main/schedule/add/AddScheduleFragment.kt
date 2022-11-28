package com.example.petcare.ui.main.schedule.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.FragmentAddScheduleBinding
import com.example.petcare.ui.dialog.DatePickerFragment
import com.example.petcare.ui.dialog.TimePickerFragment
import com.example.petcare.helper.DateHelper
import com.example.petcare.helper.showToast
import java.util.*


class AddScheduleFragment : Fragment(), View.OnClickListener, TimePickerFragment.TimeDialogListener,
    DatePickerFragment.DateDialogListener {
    private var _binding: FragmentAddScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddScheduleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addScheduleToolbar.setupWithNavController(findNavController())
        binding.btnAddDate.setOnClickListener(this)
        binding.btnAddTime.setOnClickListener(this)
        binding.btnAddSchedule.setOnClickListener(this)

        initDate()
    }

    private fun initDate() {
        val calendar = Calendar.getInstance()
        val time = calendar.time

        val dateNow = DateHelper.formatDate(time)
        val timeNow = DateHelper.formatTime(time)

        binding.btnAddDate.text = dateNow
        binding.btnAddTime.text = timeNow
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnAddDate -> {
                val fragment = DatePickerFragment()
                fragment.show(childFragmentManager, tag)
            }
            binding.btnAddTime -> {
                val fragment = TimePickerFragment()
                fragment.show(childFragmentManager, tag)
            }
            binding.btnAddSchedule -> {
                val category = binding.acActivityCategory.text.toString()
                val name = binding.etActivityName.text.toString()
                val desc = binding.etActivityDesc.text.toString()
                val remindBefore = binding.acRemindBeforeActivity.text.toString()
                val ps = binding.etActivityPostScript.text.toString()
                val timeStamp = System.currentTimeMillis()

                val rawDate = binding.btnAddDate.text.toString()
                val rawTime = binding.btnAddTime.text.toString()
                val rawDatetime = DateHelper.combineDateTime(rawDate, rawTime)
                val dateTime = DateHelper.fullParse(rawDatetime)

                val schedule = Schedule(
                    name = name,
                    category = category,
                    description = desc,
                    reminderBefore = remindBefore,
                    postScript = ps,
                    time = dateTime?.time,
                    timeStamp = timeStamp
                )
                viewModel.addData(schedule, requireActivity())
                showToast("Success")
                findNavController().popBackStack()
            }
        }
    }

    override fun onDataSubmitted(tag: String?, year: Int, month: Int, day: Int) {
        val calendar = DateHelper.setDateCalendarInstance(year, month, day)
        val submittedDate = DateHelper.formatDate(calendar.time)
        binding.btnAddDate.text = submittedDate
    }

    override fun onSubmitDialog(tag: String?, hour: Int, minute: Int) {
        val calendar = DateHelper.setTimeCalendarInstance(hour, minute)
        val submittedTime = DateHelper.formatTime(calendar.time)
        binding.btnAddTime.text = submittedTime
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        private const val TAG = "AddScheduleFragment"
    }
}