package com.example.petcare.ui.main.schedule.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.R
import com.example.petcare.databinding.FragmentEditScheduleBinding
import com.example.petcare.helper.DateHelper
import com.example.petcare.helper.showToast
import com.example.petcare.ui.dialog.DatePickerFragment
import com.example.petcare.ui.dialog.TimePickerFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.*


class EditScheduleFragment : Fragment(), View.OnClickListener,
    TimePickerFragment.TimeDialogListener,
    DatePickerFragment.DateDialogListener {
    private var _binding: FragmentEditScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditScheduleViewModel>()
    private val args: EditScheduleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editScheduleToolbar.setupWithNavController(findNavController())
        binding.btnAddDate.setOnClickListener(this)
        binding.btnAddTime.setOnClickListener(this)
        binding.btnAddSchedule.setOnClickListener(this)

        val data = args.data
        val dataTime = data.time!!
        val initDate = DateHelper.formatDate(Date(dataTime))
        val initTime = DateHelper.formatTime(Date(dataTime))

        binding.apply {
            acActivityCategory.setText(data.category)
            etActivityName.setText(data.name)
            etActivityDesc.setText(data.description)
            acRemindBeforeActivity.setText(data.reminderBefore)
            etActivityPostScript.setText(data.postScript)
            binding.btnAddDate.text = initDate
            binding.btnAddTime.text = initTime
        }

        val categoryArr = requireActivity().resources.getStringArray(R.array.Category)
        (binding.acActivityCategory as MaterialAutoCompleteTextView).setSimpleItems(categoryArr)

        val remindArr = requireActivity().resources.getStringArray(R.array.remind_before)
        (binding.acRemindBeforeActivity as MaterialAutoCompleteTextView).setSimpleItems(remindArr)

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
                val date = DateHelper.parseDate(rawDate)

                args.data.apply {
                    this.name = name
                    this.description = desc
                    this.category = category
                    this.reminderBefore = remindBefore
                    this.postScript = ps
                    this.timeStamp = timeStamp
                    this.date = date?.time
                    this.time = dateTime?.time
                }

                showToast("Edited")
                viewModel.edit(requireActivity(), args.data)
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
        _binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

