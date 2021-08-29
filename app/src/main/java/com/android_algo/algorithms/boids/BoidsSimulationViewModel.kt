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

    val boids: Flow<MutableList<Boid>> = flow {
        while (true) {
            if (isRunning) {
                val dt = System.currentTimeMillis() - lastFrameTime
                lastFrameTime = System.currentTimeMillis()

                boidsSimulation.update(dt)
                emit(boidsSimulation.boids)
            }
        }
    }

    fun setSeparationGain(gain: Float) {
        boidsSimulation.separationGain = gain
    }

    fun setAlignmentGain(gain: Float) {
        boidsSimulation.alignmentGain = gain
    }

    fun setCohesionGain(gain: Float) {
        boidsSimulation.cohesionGain = gain
    }

    fun setSightRange(sightRange: Float) {
        boidsSimulation.sightRange = sightRange
    }

    fun setBoidsCount(boidsCount: Int) {
        stopSimulation()
        boidsSimulation.updateBoidsCount(boidsCount)
        startSimulation()
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