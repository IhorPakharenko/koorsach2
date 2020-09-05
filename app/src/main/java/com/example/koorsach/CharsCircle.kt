package com.example.koorsach

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager

class CharsCircle(
    val constraintLayout: ConstraintLayout,
    val centerId: Int,
    val alphabet: String,
    val radius: Int,
    val elementsTextSize: Float
) {
    val elements = createCircleElements()

    val angleBetweenElements = 360f / alphabet.length

    fun addToConstraintLayout() {
        elements[0].setTextColor(
            ContextCompat.getColor(
                constraintLayout.context,
                R.color.colorPrimary
            )
        )

        elements.forEach {
            constraintLayout.addView(it, elementLayoutParams)
        }
        setOffset(0)
    }

    fun setOffset(offset: Int) {
        val constraintSetEnd = ConstraintSet().apply {
            clone(constraintLayout)
            elements.forEachIndexed { index, view ->
                constrainCircle(view.id, centerId, radius, (offset + index) * angleBetweenElements)
            }
        }

        TransitionManager.beginDelayedTransition(constraintLayout)

        constraintSetEnd.applyTo(constraintLayout)
    }

    private fun createCircleElements() =
        alphabet.map {
            TextView(constraintLayout.context).apply {
                id = View.generateViewId()
                text = it.toString()
                textSize = this@CharsCircle.elementsTextSize
            }
        }

    private val elementLayoutParams
        get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
}