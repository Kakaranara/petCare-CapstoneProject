package com.example.petcare

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.databinding.ActivityMainBinding
import com.example.petcare.ui.main.schedule.ScheduleViewModel

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val scheduleViewModel by viewModels<ScheduleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // !required for splash screen
        Log.e(TAG, "onCreate: ACTIVITY", )

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        scheduleViewModel.startListeningOverview()
        scheduleViewModel.listenAllSchedule()

        //? setup for bottom navigation bar | same menu id & fragment = destination for bottom bar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavbar.setupWithNavController(navController)

        //?deeplink to specified fragment
        if (intent.data?.toString()?.contains(BuildConfig.PREFIX) == true) {
            navHostFragment.navController.navigate(R.id.action_story)
        }


        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            /**
             * * Here's where we hide bottom navbar if needed
             * ? Documentation : https://developer.android.com/guide/navigation/navigation-ui
             */
            when (destination.id) {
                //? id action_xx is only fragment id for bottom navigation bar.
                //? check menu and main_nav.
                R.id.action_home -> {
                    visible()
                }
                R.id.action_news -> {
                    visible()
                }
                R.id.action_profile -> {
                    visible()
                }
                R.id.action_story -> {
                    visible()
                }
                R.id.action_schedule -> {
                    visible()
                }
                else -> {
                    invisible()
                }
            }
        }
    }

    private fun visible() {
        binding.bottomNavbar.visibility = View.VISIBLE
    }

    private fun invisible() {
        binding.bottomNavbar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ACTIVITY", )
        scheduleViewModel.unRegisterAllSchedule()
        scheduleViewModel.stopListeningOverview()
    }
    
    companion object{
        private const val TAG = "MainActivity"
    }
}