package com.cristianboicu.ebsproductapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cristianboicu.ebsproductapp.R
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setUpToolBar(toolbar)
    }

    private fun setUpToolBar(
        toolbar: Toolbar,
    ) {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration(topLevelDestinationIds = setOf())


        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.productsFragment) {
                toolbar.setNavigationIcon(R.drawable.ic_user)
            }
        }
    }

    fun restoreToolbar() {
        toolbar.findViewById<ImageView>(R.id.iv_logo).visibility = View.VISIBLE
        toolbar.findViewById<TabLayout>(R.id.tab_layout).visibility = View.GONE
    }

    fun hideToolbarLogo() {
        toolbar.findViewById<ImageView>(R.id.iv_logo).visibility = View.GONE
    }

}