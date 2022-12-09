package com.example.petcare.ui.main.other.petshop

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.petcare.R
import com.example.petcare.data.remote.response.Feature
import com.example.petcare.data.remote.response.ListOfFeature
import com.example.petcare.databinding.FragmentPetShopBinding
import com.example.petcare.helper.showToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap

import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class PetShopFragment : Fragment() {

    private var _binding: FragmentPetShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        setMapStyle()

        val isDataNotList = arguments?.getBoolean(PetShopListFragment.EXTRA_BOOLEAN)

        if(isDataNotList!=null){
            if(isDataNotList){
                @Suppress("DEPRECATION")
                val dataMap = arguments?.getParcelable<Feature>(PetShopListFragment.EXTRA_MAP)
                setMarker(dataMap)
            }else{
                @Suppress("DEPRECATION")
                val dataListMap = arguments?.getParcelable<ListOfFeature>(PetShopListFragment.EXTRA_LIST_MAP)
                setListMarker(dataListMap)
            }
        }else{
            showToast(getString(R.string.location_not_found))
        }

        getMyLocation()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                showToast(getString(R.string.style_map_error))
            }
        } catch (exception: Resources.NotFoundException) {
            showToast("Can't find style. Error: $exception")
        }
    }

    private fun setMarker(data: Feature?) {
        if(data != null){
            val startLocation = LatLng(data.center!![1], data.center!![0])
            mMap.addMarker(
                MarkerOptions()
                    .position(startLocation)
                    .title(data.text)
                    .snippet(data.place_name)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
        }else{
            showToast(getString(R.string.location_not_found))
        }

    }

    private fun setListMarker(data: ListOfFeature?) {
        if(data != null){
            data.list.forEach {
                val latLng = LatLng(it.center!![1], it.center!![0])
                mMap.addMarker(
                    MarkerOptions()
                    .position(latLng)
                    .title(it.text)
                    .snippet(it.place_name))
                boundsBuilder.include(latLng)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }else{
            showToast(getString(R.string.location_not_found))
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}