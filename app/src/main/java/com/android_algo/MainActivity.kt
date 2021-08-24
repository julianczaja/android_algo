package com.android_algo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android_algo.boids.BoidsSimulationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val algoView = findViewById<BoidsSimulationView>(R.id.surface_view)

//        algoView.
    }
}