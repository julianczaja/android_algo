package com.android_algo.boids

import com.android_algo.math.Float2
import timber.log.Timber
import kotlin.random.Random

class BoidsSimulation(
    val boids: MutableList<Boid> = mutableListOf()
) {

    fun update(dt: Long) {
        if (boids.isNotEmpty()) {
            boids.forEach { boid ->
                boid.update(dt)
            }
        }
    }

    fun populateBoids(boidsCount: Int, boardSize: Float2) {
        Timber.i("populateBoids")
        for (x in 1..boidsCount) {
            val boid = Boid(bounds = boardSize)
            boid.position.x = Random.nextFloat() * boardSize.x
            boid.position.y = Random.nextFloat() * boardSize.y
            boid.velocity.x = Random.nextDouble(-1.0, 1.0).toFloat()
            boid.velocity.y = Random.nextDouble(-1.0, 1.0).toFloat()
            boids.add(boid)
        }
    }
}