package com.halil.e_marketcase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
    }

    private fun initNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.containerViewB) as? NavHostFragment? ?: return
        navController = host.navController
        val startDestinationId = navController.graph.startDestinationId

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
            val isRootDest = destination.id == startDestinationId
            supportActionBar?.setDisplayHomeAsUpEnabled(!isRootDest)
            supportActionBar?.setDisplayShowHomeEnabled(!isRootDest)
        }
    }

}
