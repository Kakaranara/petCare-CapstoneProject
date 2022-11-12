package com.example.petcare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.petcare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // !required for splash screen

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //? setup for bottom navigation bar | same menu id & fragment = destination for bottom bar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavbar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            /**
             * * Here's where we hide bottom navbar if needed
             * ? Documentation : https://developer.android.com/guide/navigation/navigation-ui
             */
        }
    }
}