package com.recep.recep

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.recep.recep.database.Database
import com.recep.recep.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    private lateinit var activeFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Database.initLocal(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val homeFragment = HomeFragment()
        val bookmarksFragment = BookmarksFragment()

        supportFragmentManager.beginTransaction().apply {
            add(R.id.mainFragmentView, homeFragment)
            add(R.id.mainFragmentView, bookmarksFragment)
            hide(bookmarksFragment)
            commit()
        }

        activeFragment = homeFragment

        // Hilangin title app yang ntah kenapa muncul di aplikasi
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.mainFloatingActionButton)
        floatingActionButton.setOnClickListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                val publishIntent = Intent(this, PublishActivity::class.java)

                startActivity(publishIntent)
            } else {
                MaterialAlertDialogBuilder(this)
                    .setIcon(R.drawable.ic_error)
                    .setTitle("No Internet.")
                    .show()
            }
        }

        val bottomNavView = findViewById<BottomNavigationView>(R.id.mainBottomNavView)
        bottomNavView.background = null
        bottomNavView.setOnItemSelectedListener { item ->
            val homeItem = bottomNavView.menu.findItem(R.id.bottom_menu_item_home)
            val bookmarksItem = bottomNavView.menu.findItem(R.id.bottom_menu_item_bookmarks)

            when (item.itemId) {
                R.id.bottom_menu_item_home -> {
                    homeItem?.setIcon(R.drawable.ic_home_fill)
                    bookmarksItem?.setIcon(R.drawable.ic_bookmarks_outline)

                    setCurrentFragment(homeFragment)
                    true
                }

                R.id.bottom_menu_item_bookmarks -> {
                    homeItem?.setIcon(R.drawable.ic_home_outline)
                    bookmarksItem?.setIcon(R.drawable.ic_bookmarks_fill)

                    setCurrentFragment(bookmarksFragment)
                    true
                }

                else -> true
            }
        }
    }

    private fun setCurrentFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(fragment)
            commit()
        }

        activeFragment = fragment
    }
}