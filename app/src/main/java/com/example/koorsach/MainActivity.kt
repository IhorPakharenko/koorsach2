package com.example.koorsach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private val ukrainianAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        surroundWithChars(findViewById(R.id.parent), findViewById(R.id.img_center), ukrainianAlphabet)
    }

    fun surroundWithChars(parent: ConstraintLayout, center: View, chars: String) {
        val rate = 360f / chars.length

        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            circleRadius = 200
            circleConstraint = center.id
        }

        chars.forEachIndexed { index, char ->
            parent.addView(TextView(this).apply {
                text = char.toString()
            }, ConstraintLayout.LayoutParams(layoutParams).apply {
                circleAngle = index * rate
            })
        }
    }
}