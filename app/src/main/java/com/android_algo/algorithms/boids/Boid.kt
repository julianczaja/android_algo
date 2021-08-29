package com.android_algo.algorithms.boids

import com.android_algo.math.Float2
import com.android_algo.math.distance
import com.android_algo.math.max
import com.android_algo.math.normalize
import java.util.*

data class Boid(
    var position: Float2 = Float2(0f, 0f),
    var velocity: Float2 = Float2(0f, 0f),
    var speed: Float = 1.5f,
)