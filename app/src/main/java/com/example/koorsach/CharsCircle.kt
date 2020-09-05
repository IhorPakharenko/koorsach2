package com.example.koorsach

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import java.util.concurrent.TimeUnit

class CharsCircle(
    val context: Context,
    val alphabet: String,
    val circleStyle: CircleStyle
) {
    data class CircleStyle(val radius: Int, val textSize: Float)

    val elements = createCircleElements()

    val distanceBetweenElements = 359f / alphabet.length

    fun addToConstraintLayout(
        layout: ConstraintLayout,
        centerId: Int
    ) {
        elements.forEach {
            layout.addView(it, layoutParams)
        }
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)

        elements.forEachIndexed { index, view ->
            constraintSet.constrainCircle(
                view.id,
                centerId,
                circleStyle.radius,
                index * distanceBetweenElements
            )
        }
        constraintSet.applyTo(layout)
    }

    fun shiftAnimated(
        shift: Int,
        duration: Long = TimeUnit.SECONDS.toMillis(10)
    ) {
        val animator = ValueAnimator.ofFloat(0f, shift * distanceBetweenElements)
        animator.duration = duration
        animator.addUpdateListener {
            elements.forEach { view ->
                view.layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams).apply {
                    circleAngle += animator.animatedValue as Float
                }
            }
        }
        animator.start()
    }

    private fun createCircleElements() =
        alphabet.map {
            TextView(context).apply {
                id = View.generateViewId()
                text = it.toString()
                textSize = circleStyle.textSize
            }
        }

    val layoutParams
        get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
}