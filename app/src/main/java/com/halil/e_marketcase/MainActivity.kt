package com.halil.e_marketcase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.halil.e_marketcase.ui.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigation()
        observeCartItems()
    }

    private fun initNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.containerViewB) as? NavHostFragment? ?: return
        navController = host.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productDetailFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }
        val startDestinationId = navController.graph.startDestinationId

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
            val isRootDest = destination.id == startDestinationId
            supportActionBar?.setDisplayHomeAsUpEnabled(!isRootDest)
            supportActionBar?.setDisplayShowHomeEnabled(!isRootDest)
        }
    }

    private fun observeCartItems() {
        cartViewModel.cartItems.observe(this) { cartItems ->
            updateCartItemCount(cartItems.size)
        }
    }

    private fun updateCartItemCount(count: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val badge = bottomNavigationView.getOrCreateBadge(R.id.cartPageFragment)
        badge.isVisible = count > 0
        badge.number = count
    }

    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }
}
