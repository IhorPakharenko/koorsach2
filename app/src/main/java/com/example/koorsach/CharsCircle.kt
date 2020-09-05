package com.example.koorsach

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager

class CharsCircle(
    val context: Context,
    val alphabet: String,
    val circleStyle: CircleStyle
) {
    val elements = createCircleElements()

    val angleBetweenElements = 360f / alphabet.length

    fun addToConstraintLayout(
        layout: ConstraintLayout,
        centerId: Int
    ) {
        elements.forEach {
            layout.addView(it, elementLayoutParams)
        }

        elements[0].setTextColor(ContextCompat.getColor(layout.context, R.color.colorPrimary))

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
        constraintLayout: ConstraintLayout,
        centerId: Int,
        shift: Int
    ) {
        val constraintSetStart = ConstraintSet().apply { clone(constraintLayout) }
        elements.forEach {
            Log.d(
                "start",
                "${it.text}:${constraintSetStart.getConstraint(it.id).layout.circleAngle}"
            )
        }
        val constraintSetEnd = ConstraintSet().apply {
            clone(constraintLayout)
            elements.forEachIndexed { index, view ->
                val angle = (shift + index) * angleBetweenElements
                val safeAngle = if (angle == 0f) angle else angle % 360f
                constrainCircle(view.id, centerId, circleStyle.radius, safeAngle)
                Log.d("middle?", "I wanna ${(safeAngle)}")
            }
        }

        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSetEnd.applyTo(constraintLayout)

        elements.forEach {
            Log.d("end", "${it.text}:${constraintSetStart.getConstraint(it.id).layout.circleAngle}")
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

    private val elementLayoutParams
        get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

    data class CircleStyle(val radius: Int, val textSize: Float)
}