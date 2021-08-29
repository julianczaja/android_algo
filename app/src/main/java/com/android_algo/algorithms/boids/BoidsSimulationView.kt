package com.android_algo.algorithms.boids

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.*
import com.android_algo.di.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.atan2

@AndroidEntryPoint
class BoidsSimulationView(
    context: Context,
    attributeSet: AttributeSet
) : SurfaceView(context, attributeSet), SurfaceHolder.Callback, LifecycleObserver {

    private lateinit var viewModel: BoidsSimulationViewModel
    private var canvas: Canvas? = null
    private val borderWidth = 8f
    private val boidWidth: Float = 20f
    private val boidHeight: Float = 40f
    private val borderPath = Path()
    private val boidPath = Path()
    private val boidMat = Matrix()

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
                                250f,
                                boidSightPaint
                            )
                        }

                        drawBorder(canvas!!)
                        holder.unlockCanvasAndPost(canvas)

                    } catch (e: Exception) {
                        Timber.w(e.message)
                    }
                }
            }
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        Timber.i("onSizeChanged, $oldWidth -> $width, $oldHeight -> $height")
        super.onSizeChanged(width, height, oldWidth, oldHeight)
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

    private fun drawBorder(canvas: Canvas) {
        borderPath.rewind()
        with(borderPath) {
            moveTo(borderWidth, borderWidth)
            lineTo(borderWidth, canvas.height.toFloat() - borderWidth)
            lineTo(canvas.width.toFloat() - borderWidth, canvas.height.toFloat() - borderWidth)
            lineTo(canvas.width.toFloat() - borderWidth, borderWidth)
            lineTo(borderWidth, borderWidth)
        }
        canvas.drawPath(borderPath, borderPaint)
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
            lineTo(boid.position.x - boidWidth / 2f, boid.position.y + boidHeight / 2f)
            transform(boidMat)
        }
        canvas.drawPath(boidPath, boidPaint)
        canvas.drawPath(boidPath, borderPaint)
    }
}