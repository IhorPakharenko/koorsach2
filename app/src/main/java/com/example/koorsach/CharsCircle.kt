package com.example.koorsach

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import java.util.concurrent.TimeUnit

class CharsCircle(
    val context: Context,
    val alphabet: String,
    val circleStyle: CircleStyle
) {
    data class CircleStyle(val radius: Int, val textSize: Float)

    private var animator: ValueAnimator? = null

    val elements = createCircleElements()

    val angleBetweenElements = 359f / alphabet.length

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
                index * angleBetweenElements
            )
        }
        constraintSet.applyTo(layout)
    }

    fun shiftAnimated(
        shift: Int,
        duration: Long = TimeUnit.SECONDS.toMillis(3)
    ) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, shift * angleBetweenElements).also { animator ->
            animator.duration = duration
            animator.addUpdateListener {
                elements.forEachIndexed { index, view ->
                    val angleBeforeAnimation = (index * angleBetweenElements)
                    view.layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams).apply {
                        circleAngle = angleBeforeAnimation + animator!!.animatedValue as Float
                    }
                }
            }
            animator.doOnEnd {
            }
            animator.doOnCancel {
            }
            animator.start()
        }
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