package com.android_algo.algorithms.boids

import androidx.lifecycle.ViewModel
import com.android_algo.math.Float2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class BoidsSimulationViewModel : ViewModel() {

//    private val _liveData = MutableLiveData<Int>(1)
//    val liveData: LiveData<Int>
//        get() = _liveData

    private val boidsSimulation = BoidsSimulation()
    private var isRunning = false
    private var lastFrameTime = System.currentTimeMillis()

    val boids: Flow<List<Boid>> = flow {
        while (true) {
            if (isRunning) {
                val dt = System.currentTimeMillis() - lastFrameTime
                lastFrameTime = System.currentTimeMillis()
                println("DT: ${dt.toInt()}")

                boidsSimulation.update(dt)
                emit(boidsSimulation.boids)

//                delay(kotlin.math.max(0L, 33L - dt))
            }
        }
    }

    fun initSimulation(width: Int, height: Int) {
        boidsSimulation.populateBoids(
            boidsCount = 50,
            boardSize = Float2(width.toFloat(), height.toFloat())
        )
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