package com.corrot.boidssimulation.model

import com.example.simplemath.Float2

data class Boid(
    var position: Float2 = Float2(0f, 0f),
    var velocity: Float2 = Float2(0f, 0f),
    var speed: Float = 1.5f,
)