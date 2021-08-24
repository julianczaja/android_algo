package com.android_algo.boids

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.android_algo.di.BoidPaint
import com.android_algo.di.BorderPaint
import com.android_algo.di.TextPaint
import com.android_algo.math.Float2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.atan2

@AndroidEntryPoint
class BoidsSimulationView(
    context: Context,
    attributeSet: AttributeSet
) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val boidsSimulation = BoidsSimulation()
    private var job: Job? = null
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

    fun run() {
        Timber.i("run")


        var canvas: Canvas?

        while (true) {
            if (holder.surface.isValid) {
                try {
                    canvas = holder.lockCanvas()
                    if (canvas == null) {
                        continue
                    }

                    canvas.drawColor(Color.LTGRAY)

                    boidsSimulation.update(16)
                    boidsSimulation.boids.forEach { boid ->
                        drawBoid(boid, canvas)
                    }

                    drawBorder(canvas)

                    holder.unlockCanvasAndPost(canvas)
                } catch (e: Exception) {
                    Timber.e(e.message)
                }
            }
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        Timber.i("onSizeChanged")
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        boidsSimulation.populateBoids(
            boidsCount = 30,
            boardSize = Float2(width.toFloat(), height.toFloat())
        )
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Timber.i("surfaceCreated")

        job = CoroutineScope(Dispatchers.Default).launch {
            Timber.i("job started")
            run()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Timber.i("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Timber.i("surfaceDestroyed")
        job?.cancel()
        Timber.i("job cancelled")
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