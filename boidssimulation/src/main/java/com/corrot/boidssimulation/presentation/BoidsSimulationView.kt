package com.corrot.boidssimulation.presentation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.*
import com.corrot.boidssimulation.di.BoidPaint
import com.corrot.boidssimulation.di.BoidSightPaint
import com.corrot.boidssimulation.di.BoidsBoardBackgroundPaint
import com.corrot.boidssimulation.di.BorderPaint
import com.corrot.boidssimulation.model.Boid

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class BoidsSimulationView(
    context: Context,
    attributeSet: AttributeSet
) : SurfaceView(context, attributeSet), SurfaceHolder.Callback, LifecycleObserver {

    private lateinit var viewModel: BoidsSimulationViewModel
    private var canvas: Canvas? = null
    private var boidWidth = 0f
    private var boidHeight = 0f
    private val boidPath = Path()
    private val boidMat = Matrix()
    private var boidsSightRange = 250f

    @Inject
    @BoidPaint
    lateinit var boidPaint: Paint

    @Inject
    @BoidSightPaint
    lateinit var boidSightPaint: Paint

    @Inject
    @BorderPaint
    lateinit var borderPaint: Paint

    @Inject
    @BoidsBoardBackgroundPaint
    lateinit var boardBackgroundPaint: Paint

    init {
        this.holder.addCallback(this)
    }

    override fun onAttachedToWindow() {
        Timber.i("onAttachedToWindow")
        super.onAttachedToWindow()

        val viewModelStoreOwner = findViewTreeViewModelStoreOwner()!!
        val viewLifecycleOwner = findViewTreeLifecycleOwner()!!

        viewModel = ViewModelProvider(viewModelStoreOwner).get(BoidsSimulationViewModel::class.java)

        viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.Default) {
            viewModel.boids.collect { boids ->
                if (holder.surface.isValid) {
                    try {
                        // lockCanvas() is very slow and can take up to 20ms :(
                        canvas = holder.lockCanvas()
                        if (canvas == null) {
                            Timber.w("Canvas is null. Skipping frame")
                            return@collect
                        }

                        canvas!!.drawPaint(boardBackgroundPaint)

                        boids.forEach { boid ->
                            drawBoid(boid, canvas!!)
                        }

                        val debugBoid = true
                        if (debugBoid) {
                            // Draw sight range
                            canvas!!.drawCircle(
                                boids.first().position.x,
                                boids.first().position.y,
                                boidsSightRange,
                                boidSightPaint
                            )
                        }


                    } catch (e: Exception) {
                        Timber.w(e.message)
                    } finally {
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    }

    fun setSeparationGain(gain: Float) {
        viewModel.setSeparationGain(gain)
    }

    fun setAlignmentGain(gain: Float) {
        viewModel.setAlignmentGain(gain)
    }

    fun setCohesionGain(gain: Float) {
        viewModel.setCohesionGain(gain)
    }

    fun setSightRange(sightRange: Float) {
        boidsSightRange = sightRange
        viewModel.setSightRange(sightRange)
    }

    fun setBoidsCount(boidsCount: Int) {
        viewModel.setBoidsCount(boidsCount)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        Timber.i("onSizeChanged, $oldWidth -> $width, $oldHeight -> $height")
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        boidWidth = min(width, height) / 50f
        boidHeight = max(width, height) / 50f
        viewModel.updateBoardBounds(width, height)
        viewModel.initSimulation()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Timber.i("surfaceCreated")
        viewModel.startSimulation()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Timber.i("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Timber.i("surfaceDestroyed")
        viewModel.stopSimulation()
    }

    private fun drawBoid(boid: Boid, canvas: Canvas) {
        val rotDeg = Math.toDegrees(atan2(y = boid.velocity.y, x = boid.velocity.x).toDouble()) + 90

        boidMat.apply {
            reset()
            postRotate(rotDeg.toFloat(), boid.position.x, boid.position.y)
        }

        boidPath.apply {
            rewind()
            moveTo(boid.position.x - boidWidth / 2f, boid.position.y + boidHeight / 2f)
            lineTo(boid.position.x + boidWidth / 2f, boid.position.y + boidHeight / 2f)
            lineTo(boid.position.x, boid.position.y - boidHeight / 2f)
            close()
            transform(boidMat)
        }
        canvas.drawPath(boidPath, boidPaint)
        canvas.drawPath(boidPath, borderPaint)
    }
}