package com.example.petcare.ui.main.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.FragmentScheduleBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.DateHelper
import com.example.petcare.helper.showToast
import com.example.petcare.utils.gone
import com.example.petcare.utils.visible


class ScheduleFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ScheduleViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fbAddSchedule.setOnClickListener(this)
        binding.button.setOnClickListener(this)

        val manager = LinearLayoutManager(requireActivity())
        val manager2 = LinearLayoutManager(requireActivity())
        val todayAdapter = ScheduleChildAdapter()
        val upcomingAdapter = ScheduleChildAdapter()
        val divider1 = DividerItemDecoration(requireActivity(), manager.orientation)
        val divider2 = DividerItemDecoration(requireActivity(), manager2.orientation)

        val adapterClickListener = object : ScheduleChildAdapter.ScheduleButtonListener {
            override fun onDeleteClicked(documentId: String) {
                viewModel.deleteData(documentId)
            }

            override fun onEditClicked() {

            }

            override fun onItemClicked() {
                showToast("item clicked!")
            }
        }

        todayAdapter.setClickListener(adapterClickListener)
        upcomingAdapter.setClickListener(adapterClickListener)


        binding.apply {
            rvToday.adapter = todayAdapter
            rvToday.layoutManager = manager
            rvToday.addItemDecoration(divider1)
            rvUpcoming.adapter = upcomingAdapter
            rvUpcoming.layoutManager = manager2
            rvUpcoming.addItemDecoration(divider2)
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

                    if (todayList.isEmpty() && laterList.isEmpty()) {
                        binding.apply {
                            tvTodaySchedule.gone()
                            tvUpcoming.gone()
                            scheduleEmptyImage.visible()
                        }
                    } else if (todayList.isEmpty()) {
                        binding.apply {
                            tvTodaySchedule.gone()
                            tvUpcoming.visible()
                            scheduleEmptyImage.gone()
                        }
                    } else if (laterList.isEmpty()) {
                        binding.apply {
                            tvTodaySchedule.visible()
                            tvUpcoming.gone()
                            scheduleEmptyImage.gone()
                        }
                    } else {
                        binding.apply {
                            tvTodaySchedule.visible()
                            tvUpcoming.visible()
                            scheduleEmptyImage.gone()
                        }
                    }

                    todayAdapter.submitList(todayList)
                    upcomingAdapter.submitList(laterList)

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
            binding.button -> {
                val go = ScheduleFragmentDirections.actionActionScheduleToAllScheduleFragment()
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