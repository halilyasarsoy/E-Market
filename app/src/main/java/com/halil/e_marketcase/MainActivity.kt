package com.halil.e_marketcase

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
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

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerViewB) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
        initNavigation()
        observeCartItems()
        handleOnBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
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
            if (!isRootDest) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            }
        }
    }

    private fun observeCartItems() {
        cartViewModel.totalItemCount.observe(this) { itemCount ->
            updateCartItemCount(itemCount)
        }
    }

    private fun updateCartItemCount(count: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val badge = bottomNavigationView.getOrCreateBadge(R.id.cartPageFragment)
        badge.isVisible = count > 0
        badge.number = count
    }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }
}

