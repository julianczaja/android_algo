package com.android_algo.algorithms.boids

import androidx.lifecycle.ViewModel
import com.android_algo.math.Float2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class BoidsSimulationViewModel : ViewModel() {

    private val boidsSimulation = BoidsSimulation()
    private var isRunning = false
    private var lastFrameTime = System.currentTimeMillis()

    val boids: Flow<List<Boid>> = flow {
        while (true) {
            if (isRunning) {
                val dt = System.currentTimeMillis() - lastFrameTime
                lastFrameTime = System.currentTimeMillis()

                boidsSimulation.update(dt)
                emit(boidsSimulation.boids)

//                delay(kotlin.math.max(0L, 33L - dt))
            }
        }
    }

    fun updateBoardBounds(width: Int, height: Int) {
        Timber.i("updateBoardBounds")
        boidsSimulation.boardBounds = Float2(width.toFloat(), height.toFloat())
    }

    fun initSimulation() {
        Timber.i("initSimulation")
        boidsSimulation.populateBoids(boidsCount = 50)
    }

    fun startSimulation() {
        Timber.i("startSimulation")
        isRunning = true
    }

    fun stopSimulation() {
        Timber.i("stopSimulation")
        isRunning = false
    }

}