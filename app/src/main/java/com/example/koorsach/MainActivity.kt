package com.example.koorsach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val ukrainianAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"

    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.parent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surroundWithChars(constraintLayout, findViewById(R.id.img_center), 300, ukrainianAlphabet)
        surroundWithChars(constraintLayout, findViewById(R.id.img_center), 400, ukrainianAlphabet)
    }

    fun surroundWithChars(parent: ConstraintLayout, center: View, radius: Int, chars: String) {
        val rate = 360f / chars.length

        val constraintSet = ConstraintSet()

        val views = chars.map {
            TextView(this).apply {
                id = View.generateViewId()
                text = it.toString()
                textSize = 30f
            }
        }

        views.forEach {
            parent.addView(it, layoutParams)
        }

        constraintSet.clone(parent)

        views.forEachIndexed { index, view ->
            constraintSet.constrainCircle(view.id, center.id, radius, index * rate)
        }
        constraintSet.applyTo(constraintLayout)
    }

    val layoutParams get() = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
}