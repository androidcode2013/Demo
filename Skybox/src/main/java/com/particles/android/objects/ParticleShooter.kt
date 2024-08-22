package com.particles.android.objects

import android.opengl.Matrix
import android.os.Build
import androidx.annotation.RequiresApi
import com.particles.android.util.Geometry
import kotlin.random.Random

class ParticleShooter {
    private var position: Geometry.Companion.Point? = null
    var color: Int = 0

    var angleVariance = 0f
    var speedVariance = 0f

    var rotationMatrix = FloatArray(16)
    var directionVector = FloatArray(4)
    var resultVector = FloatArray(4)


    constructor(
        point: Geometry.Companion.Point,
        direction: Geometry.Companion.Vector,
        color: Int,
        angleVariance: Float,
        speedVariance: Float
    ) {
        this.position = point
        this.color = color
        this.angleVariance = angleVariance
        this.speedVariance = speedVariance
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun addParticles(particleSystem: ParticleSystem, currentTime: Float, count: Int) {
        for (i in 0 until count) {
            Matrix.setRotateEulerM(
                rotationMatrix, 0,
                (Random.nextFloat() - 0.5f) * angleVariance,
                (Random.nextFloat() - 0.5f) * angleVariance,
                (Random.nextFloat() - 0.5f) * angleVariance,
            )
            Matrix.multiplyMV(
                resultVector, 0,
                rotationMatrix, 0,
                directionVector, 0
            )
            var speedAdjustment = 1f + Random.nextFloat() * speedVariance
            var thisDirection = Geometry.Companion.Vector(
                resultVector[0] * speedAdjustment,
                resultVector[1] * speedAdjustment,
                resultVector[2] * speedAdjustment
            )
            particleSystem.addParticle(position!!, color, thisDirection, currentTime)
        }
    }
}