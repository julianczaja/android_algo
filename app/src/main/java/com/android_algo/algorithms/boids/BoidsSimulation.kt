package com.android_algo.algorithms.boids

import com.android_algo.math.Float2
import com.android_algo.math.distance
import com.android_algo.math.normalize
import timber.log.Timber
import kotlin.random.Random

class BoidsSimulation(
    val boids: MutableList<Boid> = mutableListOf()
) {

    var sightRange: Float = 250f
    var boardBounds = Float2(0f, 0f)
    var separationGain = 0.2f
    var alignmentGain = 0.01f
    var cohesionGain = 0.2f

    fun update(dt: Long) {
        if (boids.isNotEmpty()) {

            boids.forEach { boid ->
                val boidsInRange = getBoidsInRange(boid)
                updateBoid(boid, dt, boardBounds, boidsInRange)
            }
        }
    }

    private fun getBoidsInRange(boid: Boid) = boids.filter {
        (it != boid) && distance(it.position, boid.position) < sightRange
    }

    fun populateBoids(boidsCount: Int) {
        Timber.i("populateBoids")
        boids.clear()
        for (x in 1..boidsCount) {
            val boid = generateRandomBoid()
            boids.add(boid)
        }
    }

    private fun generateRandomBoid(): Boid {
        return Boid().apply {
            position.x = Random.nextFloat() * boardBounds.x
            position.y = Random.nextFloat() * boardBounds.y
            velocity.x = Random.nextDouble(-1.0, 1.0).toFloat()
            velocity.y = Random.nextDouble(-1.0, 1.0).toFloat()
            velocity = normalize(velocity) * speed
        }
    }

    private fun updateBoid(boid: Boid, dt: Long, bounds: Float2, boidsInRange: List<Boid>) {
        if (boidsInRange.isNotEmpty()) {
            var separation = Float2(0f, 0f)
            var alignment = Float2(0f, 0f)
            var cohesion = Float2(0f, 0f)

            boidsInRange.forEach {
                val dist = distance(boid.position, it.position)
                val avoidingForce = (boid.position - it.position) / dist
                separation += avoidingForce
                alignment += it.velocity
                cohesion += it.position
            }

            separation /= boidsInRange.size.toFloat()
            separation = normalize(separation) * boid.speed

            alignment /= boidsInRange.size.toFloat()
            alignment = normalize(alignment) * boid.speed
            alignment -= boid.velocity

            cohesion /= boidsInRange.size.toFloat()
            cohesion -= boid.position
            cohesion = normalize(cohesion) * boid.speed
//
//            println("Separation: $separation")
//            println("alignment: $alignment")
//            println("cohesion: $cohesion")
//            println("------------------------")

            boid.velocity += separation * separationGain
            boid.velocity += alignment * alignmentGain
            boid.velocity += cohesion * cohesionGain

            // Set magnitude of velocity
            boid.velocity = normalize(boid.velocity) * boid.speed
        }

        val newPos = boid.position + (boid.velocity * (dt / 10f) * (boid.speed))

        // Check boundaries
        // version 1
//        if (newPos.x < 0) newPos.x = bounds.x
//        else if (newPos.y < 0) newPos.y = bounds.y
//        if (newPos.x > bounds.x) newPos.x = 0f
//        else if (newPos.y > bounds.y) newPos.y = 0f
//
//        boid.position = newPos

        // version 2
        if (newPos.x > 0 && newPos.x < bounds.x && newPos.y > 0 && newPos.y < bounds.y) {
            boid.position = newPos
        } else {
            boid.velocity = -boid.velocity
        }
    }

    fun updateBoidsCount(boidsCount: Int) {
        when {
            boidsCount < boids.size -> {
                for (i in 1..boids.size - boidsCount) {
                    boids.removeLast()
                }
            }
            boidsCount > boids.size -> {
                for (i in 1..boidsCount - boids.size) {
                    val boid = generateRandomBoid()
                    boids.add(boid)
                }
            }
            else -> {
                return
            }
        }
    }
}