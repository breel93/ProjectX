package com.xplorer.projectx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.xplorer.projectx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        showChooseDestinationFragment()
    }

    private fun showChooseDestinationFragment() {
        val searchFragment = SearchStartFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFragment, searchFragment)
            .commit()
    }
}
