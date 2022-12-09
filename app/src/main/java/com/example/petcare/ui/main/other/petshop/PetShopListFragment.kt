package com.example.petcare.ui.main.other.petshop

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.R
import com.example.petcare.ViewModelFactory
import com.example.petcare.data.remote.Result
import com.example.petcare.data.remote.response.Feature
import com.example.petcare.data.remote.response.ListOfFeature
import com.example.petcare.data.remote.response.PlacesResponse
import com.example.petcare.databinding.FragmentPetShopListBinding
import com.example.petcare.di.Injection
import com.example.petcare.helper.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class PetShopListFragment : Fragment() {

    private var _binding: FragmentPetShopListBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var listMap : List<Feature>
    private var locationNow: Location?= null
    private val viewModel by activityViewModels<PetShopViewModel> {
        ViewModelFactory(Injection.provideStoryRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetShopListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_in_list) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        viewModel.listPetShop.observe(viewLifecycleOwner){
            it?.let{
                prepareData(it)

            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnMap.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putBoolean(EXTRA_BOOLEAN, false)
            mBundle.putParcelable(EXTRA_LIST_MAP, ListOfFeature(listMap))
            findNavController().navigate(R.id.action_petShopListFragment_to_petShopFragment, mBundle)
        }

    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap

        setMapStyle()
        getMyLastLocation()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                    showToast(getString(R.string.no_permission))
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                    locationNow = location
                } else {
                    showToast(getString(R.string.location_not_found))
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.your_location))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
        viewModel.getPetShopList(location.latitude, location.longitude, getString(R.string.mapbox_token))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun prepareData(result: Result<PlacesResponse>){
        when (result) {
            is Result.Loading -> {
                showLoading(true)
            }
            is Result.Success -> {
                showLoading(false)
                result.data.attribution?.let{
                    setCreditData(it)
                }
                result.data.features?.let{
                    setNewsData(it)
                    thereIsData(it.size)
                    listMap = it
                }
                result.data.message?.let {
                    showToast(it)
                }
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
            }
        }
    }

    private fun setCreditData(credit: String){
        binding.creditMapboxText.text = credit
        binding.creditMapboxText.visibility = View.VISIBLE
    }

    private fun thereIsData(thereIsData: Int){
        if(thereIsData >0){
            binding.imagePetshopEmpty.visibility = View.GONE
            binding.textPetshopEmpty.visibility = View.GONE
            binding.btnMap.visibility = View.VISIBLE
            binding.headerLocationThree.visibility = View.VISIBLE
        }else{
            binding.imagePetshopEmpty.visibility = View.VISIBLE
            binding.textPetshopEmpty.visibility = View.VISIBLE
            binding.btnMap.visibility = View.GONE
            binding.headerLocationThree.visibility = View.GONE
            binding.creditMapboxText.visibility = View.GONE
        }
    }

    private fun setNewsData(petShopList: List<Feature>) {
        val petShopAdapter = PetShopAdapter(petShopList)
        binding.recyclerPetShop.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerPetShop.adapter = petShopAdapter

        petShopAdapter.setOnItemClickCallback(object : PetShopAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Feature) {
                val mBundle = Bundle()
                mBundle.putParcelable(EXTRA_MAP , data)
                mBundle.putBoolean(EXTRA_BOOLEAN, true)
                findNavController().navigate(R.id.action_petShopListFragment_to_petShopFragment, mBundle)
            }
        })
    }

    companion object{
        const val EXTRA_MAP = "extra_map"
        const val EXTRA_LIST_MAP ="extra_list_map"
        const val EXTRA_BOOLEAN = "extra_boolean"
    }

}
