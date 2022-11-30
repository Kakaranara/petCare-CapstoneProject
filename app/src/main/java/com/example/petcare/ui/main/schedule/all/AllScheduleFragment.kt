package com.example.petcare.ui.main.schedule.all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.Schedule
import com.example.petcare.databinding.FragmentAllScheduleBinding
import com.example.petcare.helper.Async
import com.example.petcare.helper.showToast
import com.example.petcare.ui.main.schedule.ScheduleChildAdapter

class AllScheduleFragment : Fragment() {
    private var _binding: FragmentAllScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AllScheduleViewModel>()
    private lateinit var adapter: ScheduleChildAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.allScheduleToolbar.setupWithNavController(findNavController())
        setupAdapter()

        viewModel.listenData().observe(viewLifecycleOwner) {
            when (it) {
                is Async.Error -> {
                    showToast("Some error. ${it.error}")
                }
                is Async.Loading -> {

                }
                is Async.Success -> {
                    val snapshot = it.data
                    val snapshotObject = snapshot!!.map { queryDocumentSnapshot ->
                        queryDocumentSnapshot.toObject(Schedule::class.java).also { schedule ->
                            schedule.uniqueId = queryDocumentSnapshot.id
                        }
                    }

                    adapter.submitList(snapshotObject)
                }
            }
        }
    }

    private fun setupAdapter() {
        adapter = ScheduleChildAdapter()
        val manager = LinearLayoutManager(requireActivity())
        val decor = DividerItemDecoration(requireActivity(), manager.orientation)

        binding.apply {
            rvAllSchedule.adapter = adapter
            rvAllSchedule.layoutManager = manager
            rvAllSchedule.addItemDecoration(decor)
        }

        adapter.setClickListener(object : ScheduleChildAdapter.ScheduleButtonListener {
            override fun onDeleteClicked(documentId: String) {
                viewModel.deleteData(documentId)
            }

            override fun onItemClicked() {

            }

            override fun onEditClicked() {

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.unRegister()
    }
}