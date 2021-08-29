package com.corrot.boidssimulation.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BoidPaint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BoidSightPaint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BorderPaint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BoidsBoardBackgroundPaint