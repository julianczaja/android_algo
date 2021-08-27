package com.android_algo.algorithms.boids

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.android_algo.di.BoidPaint
import com.android_algo.di.BorderPaint
import com.android_algo.di.TextPaint
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
    private val borderPath = Path()
    private val boidPath = Path()
    private val boidMat = Matrix()

    @Inject
    @TextPaint
    lateinit var textPaint: Paint

    @Inject
    @BoidPaint
    lateinit var boidPaint: Paint

    @Inject
    @BorderPaint
    lateinit var borderPaint: Paint


    init {
        this.holder.addCallback(this)
    }

    override fun onAttachedToWindow() {
        Timber.i("onAttachedToWindow")
        super.onAttachedToWindow()

        val viewModelStoreOwner = findViewTreeViewModelStoreOwner()!!
        viewModel = ViewModelProvider(viewModelStoreOwner).get(BoidsSimulationViewModel::class.java)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        Timber.i("onSizeChanged")
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        viewModel.initSimulation(width, height)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Timber.i("surfaceCreated")

        viewModel.viewModelScope.launch(Dispatchers.Default) {
            viewModel.boids.collect { boids ->
                if (holder.surface.isValid) {
                    try {
                        // lockCanvas() is very slow and can take up to 20ms :(
                        canvas = holder.lockCanvas()
                        if (canvas == null) {
                            Timber.e("Canvas is null!")
                            return@collect
                        }

                        canvas!!.drawColor(Color.LTGRAY)
                        boids.forEach { boid ->
                            drawBoid(boid, canvas!!)
                        }
                        drawBorder(canvas!!)
                    } finally {
                        holder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }

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
            moveTo(boid.position.x - boid.width / 2f, boid.position.y + boid.height / 2f)
            lineTo(boid.position.x + boid.width / 2f, boid.position.y + boid.height / 2f)
            lineTo(boid.position.x, boid.position.y - boid.height / 2f)
            lineTo(boid.position.x - boid.width / 2f, boid.position.y + boid.height / 2f)
            transform(boidMat)
        }
        canvas.drawPath(boidPath, boidPaint)
    }
}