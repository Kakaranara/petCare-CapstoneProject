package com.example.petcare.ui.user.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.petcare.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val menuArray = arrayOf("Another Feature", "Logout")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        auth.currentUser?.let {
            Glide.with(requireActivity())
                .load(it.photoUrl)
                .into(binding.circleImageView)
            binding.tvProfileName.text = it.displayName
            binding.tvProfileEmail.text = it.email
        }
        val adapter =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, menuArray)

        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { parent, v, position, id ->
            when (position) {
                0 -> {
                    Toast.makeText(requireActivity(), menuArray[position], Toast.LENGTH_SHORT)
                        .show()
                }
                1 -> {
                    logout()
                    Toast.makeText(requireActivity(), menuArray[position], Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            /**
             * ? in case you need it
             */
        }
    }

    private fun logout(){
        Firebase.auth.signOut()
        val go = ProfileFragmentDirections.actionGlobalLoginFragment()
        findNavController().navigate(go)
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