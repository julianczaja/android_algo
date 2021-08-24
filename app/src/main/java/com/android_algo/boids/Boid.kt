package com.android_algo.boids

import com.android_algo.math.Float2
import java.util.*

data class Boid(
    val id: String = UUID.randomUUID().toString(),
    val width: Float = 25f,
    val height: Float = 50f,
    var position: Float2 = Float2(0f, 0f),
    var velocity: Float2 = Float2(0f, 0f),
    var bounds: Float2
) {

    fun update(dt: Long) {
        val newPos = position.plus(velocity.times(dt / 10f))
        if (newPos.x > 0 && newPos.x < bounds.x && newPos.y > 0 && newPos.y < bounds.y) {
            position = newPos
        }
    }
}