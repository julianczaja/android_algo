package com.android_algo.algorithms.boids

import com.android_algo.math.Float2
import timber.log.Timber
import kotlin.random.Random

class BoidsSimulation(
    val boids: MutableList<Boid> = mutableListOf()
) {

    var sightRange: Float = 50f
    var boardBounds = Float2(0f, 0f)

    fun update(dt: Long) {
        if (boids.isNotEmpty()) {

            boids.forEach { boid ->
                val boidsInRange = getBoidsInRange(boid)
                boid.update(dt, boardBounds, boidsInRange)
            }
        }
    }

    fun getBoidsInRange(boid: Boid): List<Boid> {
        val boidsInRange = mutableListOf<Boid>()

        return boidsInRange
    }

    fun populateBoids(boidsCount: Int) {
        boids.clear()
        Timber.i("populateBoids")
        for (x in 1..boidsCount) {
            val boid = Boid()
            boid.position.x = Random.nextFloat() * boardBounds.x
            boid.position.y = Random.nextFloat() * boardBounds.y
            boid.velocity.x = Random.nextDouble(-1.0, 1.0).toFloat()
            boid.velocity.y = Random.nextDouble(-1.0, 1.0).toFloat()

            // Random normalized velocity
            // val vX = Random.nextDouble(-1.0, 1.0).toFloat()
            // val vY = Random.nextDouble(-1.0, 1.0).toFloat()
            // val len = sqr(vX * vX + vY * vY)
            // boid.velocity.x = vX / len
            // boid.velocity.y = vY / len

            boids.add(boid)
        }
    }
}