package com.example.petcare.ui.main.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.FragmentScheduleBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.DateHelper
import com.example.petcare.helper.showToast


class ScheduleFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ScheduleViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fbAddSchedule.setOnClickListener(this)

        val manager = LinearLayoutManager(requireActivity())
        val manager2 = LinearLayoutManager(requireActivity())
        val todayAdapter = ScheduleChildAdapter()
        val laterAdapter = ScheduleChildAdapter()

        todayAdapter.setClickListener(object : ScheduleChildAdapter.ScheduleButtonListener {
            override fun onDeleteClicked(documentId: String) {
                viewModel.deleteData(documentId)
            }

            override fun onEditClicked() {

            }
        })

        binding.apply {
            rvToday.adapter = todayAdapter
            rvToday.layoutManager = manager
            rvUpcoming.adapter = laterAdapter
            rvUpcoming.layoutManager = manager2
        }

        viewModel.listenForDataChanges().observe(viewLifecycleOwner) {
            when (it) {
                is Async.Error -> {
                    showToast("error ${it.error}")
                }
                is Async.Loading -> {}
                is Async.Success -> {
                    val snapshot = it.data
                    val snapshotObject: List<Schedule> = snapshot!!.map { querySnapshot ->
                        querySnapshot.toObject(Schedule::class.java).also { schedule ->
                            schedule.uniqueId = querySnapshot.id
                        }
                    }
                    val todayList = mutableListOf<Schedule>()
                    val laterList = mutableListOf<Schedule>()
                    snapshotObject.forEach { schedule ->
                        if (schedule.date == DateHelper.getTodayMillis()) {
                            todayList.add(schedule)
                        } else {
                            laterList.add(schedule)
                        }
                    }

                    todayAdapter.submitList(todayList)
                    laterAdapter.submitList(laterList)

                    Log.e(TAG, "today : $todayList")
                    Log.e(TAG, "later : $laterList")

                }
            }
        }

    }

    override fun onClick(view: View) {
        when (view) {
            binding.fbAddSchedule -> {
                val go = ScheduleFragmentDirections.actionActionScheduleToAddScheduleFragment()
                findNavController().navigate(go)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.unRegister()
    }

    companion object {
        const val TAG = "Schedule Fragment"
    }
}