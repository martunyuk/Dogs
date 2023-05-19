package com.vitaly.dogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.vitaly.dogs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fragment: Fragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        FragmentManager.replaceFragment(R.id.frameLayout, fragment, this)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                else -> SearchFragment()
            }
            FragmentManager.replaceFragment(R.id.frameLayout, fragment, this)
            true
        }
    }

}