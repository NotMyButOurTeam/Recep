package com.recep.recep

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.recep.recep.database.Database
import com.recep.recep.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    companion object {
        const val FRAGMENT_HOME = 0
        const val FRAGMENT_BOOKMARKS = 1
        const val STATE_FRAGMENT = "previousFragmentId"
    }

    private var activeFragmentId: Int = FRAGMENT_HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Database.initLocal(this)
        if (savedInstanceState != null) {
            activeFragmentId = savedInstanceState.getInt(STATE_FRAGMENT)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragments = listOf(
            HomeFragment(),
            BookmarksFragment()
        )

        supportFragmentManager.beginTransaction().apply {
            for (fragment in fragments) {
                add(R.id.mainFragmentView, fragment)
                    .setReorderingAllowed(true)
                    .disallowAddToBackStack()
                hide(fragment)
            }
            show(fragments[activeFragmentId])
            commit()
        }

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

                    setCurrentFragment(FRAGMENT_HOME)
                }

                R.id.bottom_menu_item_bookmarks -> {
                    homeItem?.setIcon(R.drawable.ic_home_outline)
                    bookmarksItem?.setIcon(R.drawable.ic_bookmarks_fill)

                    setCurrentFragment(FRAGMENT_BOOKMARKS)
                }
            }
            true
        }
    }

    override fun onPause() {
        if (isChangingConfigurations) {
            supportFragmentManager.fragments.forEach { fragment ->
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitNowAllowingStateLoss()
            }
        }

        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt(STATE_FRAGMENT, activeFragmentId)

        super.onSaveInstanceState(outState, outPersistentState)
    }


    private fun setCurrentFragment(fragment: Int) {
        val fragments = supportFragmentManager.fragments

        supportFragmentManager.beginTransaction().apply {
            hide(fragments[activeFragmentId])
            show(fragments[fragment])
            commit()
        }

        activeFragmentId = fragment
    }
}