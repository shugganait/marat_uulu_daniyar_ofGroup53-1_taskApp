package com.kg.taskapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.kg.taskapp.data.local.Pref
import com.kg.taskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: Pref
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        pref = Pref(this)


        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("shug", "OnCreate: $it")
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (!pref.isUserSeen()) {
            navController.navigate(R.id.onBoardFragment)
        }

        if (auth.currentUser?.uid == null) {
            navController.navigate(R.id.authFragment)
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.task_fragment,
                R.id.navigation_profile,
            )
        )

        val bottomNavFragments = setOf(
            R.id.navigation_home,
            R.id.navigation_dashboard,
            R.id.navigation_notifications,
            R.id.navigation_profile
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            navView.isVisible = bottomNavFragments.contains(destination.id)
            if (destination.id == R.id.onBoardFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }
}