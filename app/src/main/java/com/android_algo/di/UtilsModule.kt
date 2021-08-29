package com.android_algo.di

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import com.android_algo.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Singleton
    @Provides
    @BoidPaint
    fun provideBoidPaint(@ApplicationContext context: Context): Paint {
        return Paint().apply {
            color = context.resources.getColor(R.color.boids, context.theme)
            strokeWidth = 5f
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    @Singleton
    @Provides
    @BoidSightPaint
    fun provideBoidSightPaint(@ApplicationContext context: Context): Paint {
        return Paint().apply {
            color = context.resources.getColor(R.color.boidsSight, context.theme)
            style = Paint.Style.FILL
        }
    }

    @Singleton
    @Provides
    @BorderPaint
    fun provideBorderPaint(): Paint {
        return Paint().apply {
            color = Color.BLACK
            strokeJoin = Paint.Join.MITER
            strokeCap = Paint.Cap.SQUARE
            style = Paint.Style.STROKE
            isAntiAlias = false
            isDither = false
            strokeWidth = 4f
        }
    }

    @Singleton
    @Provides
    @BoidsBoardBackgroundPaint
    fun provideBoidsBoardBackgroundPaint(@ApplicationContext context: Context): Paint {
        return Paint().apply {
            color = context.resources.getColor(R.color.boidsBoardBackground, context.theme)
            style = Paint.Style.FILL
        }
    }

}