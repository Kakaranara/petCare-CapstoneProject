package com.example.petcare.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petcare.constant.OtherMenu
import com.example.petcare.databinding.FragmentProfileBinding
import com.example.petcare.helper.showToast
import com.example.petcare.preferences.SchedulePreferences
import com.example.petcare.scheduleDataStore
import com.example.petcare.ui.main.schedule.ScheduleVMFactory
import com.example.petcare.ui.main.schedule.ScheduleViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val scheduleViewModel by activityViewModels<ScheduleViewModel>() {
        ScheduleVMFactory(SchedulePreferences(requireActivity().scheduleDataStore))
    }

    //? other list menu item (check enum OtherMenu in constant package)
    private val listMenuArray: Array<String> = OtherMenu.values().map { it.string }.toTypedArray()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEditProfile.setOnClickListener(this)

        setUserInformation()
        setOtherList()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnEditProfile -> {
                val go = ProfileFragmentDirections.actionActionProfileToEditProfileFragment()
                findNavController().navigate(go)
            }
        }
    }

    private fun setUserInformation() {
        Firebase.auth.currentUser?.let { user: FirebaseUser ->
            binding.apply {
                tvProfileEmail.text = user.email
                tvProfileName.text = user.displayName
                Glide.with(requireActivity()).load(user.photoUrl).into(circleImageView)
            }
        }
    }

    private fun setOtherList() {
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            listMenuArray
        )

        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            //? Other Menu -> List that appear below profile image
            when (listMenuArray[position]) {
                OtherMenu.OtherFeature.string -> {
                    showToast("Other Feature")
                }
                OtherMenu.PetShop.string -> {
                    val go = ProfileFragmentDirections.actionActionProfileToPetShopFragment()
                    findNavController().navigate(go)
                }
                OtherMenu.Logout.string -> {
                    logout()
                    showToast("LOGOUT")
                }
            }
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        val go = ProfileFragmentDirections.actionGlobalLoginFragment()
        findNavController().navigate(go)
        scheduleViewModel.setHasLogout()
        Log.d(TAG, "onClick: LOGOUT SUCCESS")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "Profile Fragment"
    }
}