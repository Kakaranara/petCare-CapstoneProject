package com.example.petcare.ui.main.schedule

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.FragmentScheduleBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.DateHelper
import com.example.petcare.helper.safeNav
import com.example.petcare.helper.showToast
import com.example.petcare.service.NotificationService
import com.example.petcare.utils.gone
import com.example.petcare.utils.visible


class ScheduleFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<ScheduleViewModel>()

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

        viewModel.isDialogAlreadyShow.observe(viewLifecycleOwner) { shown ->
            if(!shown){
                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Have you already turn on notification setting?")
                    .setMessage("For better experience, please activate this in setting to notify your schedule.")
                    .setNegativeButton("No, Thanks") { _, _ ->
                        viewModel.setDialogHasShown()
                    }
                    .setNeutralButton("settings") { _, _ ->
                        goToNotificationSettings(null, requireActivity())
                    }
                    .setPositiveButton("yes") { dialogInterface: DialogInterface, i: Int ->
                        viewModel.setDialogHasShown()
                    }

                alertDialog.show()
            }
        }

        val adapterClickListener = object : ScheduleChildAdapter.ScheduleButtonListener {
            override fun onDeleteClicked(schedule: Schedule) {
                viewModel.deleteData(requireActivity(), schedule.id!!, schedule.uniqueId!!)
            }

            override fun onEditClicked(schedule: Schedule) {
                val go =
                    ScheduleFragmentDirections.actionActionScheduleToEditScheduleFragment(schedule)
                findNavController().navigate(go)
            }

            override fun onItemClicked(schedule: Schedule) {
                safeNav {
                    val go =
                        ScheduleFragmentDirections.actionActionScheduleToDetailScheduleDialog(
                            schedule
                        )
                    findNavController().navigate(go)
                }
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

        viewModel.overviewListener.observe(viewLifecycleOwner) {
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

                    val todaySorted = todayList.sortedBy { schedule -> schedule.time!! }

                    todayAdapter.submitList(todaySorted)
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

    private fun goToNotificationSettings(channel: String?, context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (channel != null) {
                intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel)
            } else {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel != null) {
                intent.action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel)
            } else {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            }
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
        }
        context.startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e(TAG, "onCreateView: FRAGMENT !")
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: FRAGMENT")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(TAG, "onAttach: FRAGMENT")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: FRAGMENT")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: FRAGMENT")
    }

    companion object {
        const val TAG = "Schedule Fragment"
    }
}