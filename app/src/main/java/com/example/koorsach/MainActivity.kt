package com.example.koorsach

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val ukrainianAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"
    private val englishAlphabet = "abcdefghijklmnopqrstuvwxyz"
    private val rate = 360f / englishAlphabet.length

    private val cipher = CeasarCipher(englishAlphabet)

    private lateinit var innerCircleViews: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addCharsCircle(container, img_center, 400, englishAlphabet)
        innerCircleViews = addCharsCircle(container, img_center, 300, englishAlphabet)

        et_text.addTextChangedListener {
            updateUi(
                it.toString(),
                et_key.text.toString().toBigIntegerOrNull() ?: return@addTextChangedListener
            )
        }

        et_key.addTextChangedListener {
            updateUi(
                et_text.text.toString(),
                it.toString().toBigIntegerOrNull() ?: return@addTextChangedListener
            )
        }
    }

    fun updateUi(text: String, key: BigInteger) {
        txt_result.text = cipher.enrypt(text, key)
//        animateCircle(innerCircleViews, currentPositionShift + (englishAlphabet.length).rem(key))
    }

    fun animateCircle(elements: List<View>, shift: Int) {
        ValueAnimator.ofFloat(0f, shift * rate).apply {
            duration = 1000
            addUpdateListener {
                elements.forEach {
                    it.layoutParams = (it.layoutParams as ConstraintLayout.LayoutParams).apply {
                        circleAngle += animatedValue as Float
                    }
                }
            }
        }.start()
    }

    fun addCharsCircle(
        container: ConstraintLayout,
        center: View,
        radius: Int,
        chars: String
    ): List<View> {
        val constraintSet = ConstraintSet()

        val views = chars.map {
            TextView(this).apply {
                id = View.generateViewId()
                text = it.toString()
                textSize = 30f
            }
        }

        views.forEach {
            container.addView(it, layoutParams)
        }

        constraintSet.clone(container)

        views.forEachIndexed { index, view ->
            constraintSet.constrainCircle(view.id, center.id, radius, index * rate)
        }
        constraintSet.applyTo(container)

        return views
    }

    val layoutParams
        get() = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

    class CeasarCipher(val alphabet: String) {
        //TODO just shifting wont work :)
        fun getShift(key: BigInteger) = (key % alphabet.length.toBigInteger()).toInt()

        fun enrypt(toEncrypt: String, key: BigInteger): String {
            val shift = getShift(key)
            return toEncrypt.map { originalChar ->
                val charIndexInAlphabet = alphabet.indexOf(originalChar, ignoreCase = true)
                if (charIndexInAlphabet >= 0) {
                    alphabet.getOrNull(charIndexInAlphabet + shift)
                        ?: alphabet[charIndexInAlphabet + shift - alphabet.length].let { newChar ->
                            if (originalChar.isUpperCase()) {
                                newChar.toUpperCase()
                            } else {
                                newChar
                            }
                        }
                } else {
                    originalChar
                }
            }.joinToString("")
        }

        fun decrypt(toDecrypt: String, key: Int): String {
            val chaList = toDecrypt.toMutableList()
            Collections.rotate(chaList, key * -1)
            return chaList.joinToString()
        }
    }

    class CharsCircles(
        val context: Context,
        val alphabet: String,
        val innerCircleStyle: CircleStyle = CircleStyle(
            radius = 300f.dpToPx(context),
            textSize = 16f.spToPx(context)
        ),
        val outerCircleStyle: CircleStyle = CircleStyle(
            radius = 400f.dpToPx(context),
            textSize = 22f.spToPx(context)
        )
    ) {
        data class CircleStyle(val radius: Int, val textSize: Float)

        val innerCircleElements = createCircleElements(alphabet, innerCircleStyle)
        val outerCircleElements = createCircleElements(alphabet, outerCircleStyle)

        val distanceBetweenElements = 359f / alphabet.length

        fun addToConstraintLayout(layout: ConstraintLayout, centerId: Int) {
            addCircleToConstraintLayout(layout, centerId, innerCircleElements, innerCircleStyle)
            addCircleToConstraintLayout(layout, centerId, outerCircleElements, outerCircleStyle)
        }

        fun shiftInnerCircleAnimated(
            circleElements: List<View>,
            shift: Int,
            duration: Long = TimeUnit.SECONDS.toMillis(10)
        ) {

        }

        private fun shiftCircleAnimated(
            circleElements: List<View>,
            shift: Int,
            duration: Long = TimeUnit.SECONDS.toMillis(10)
        ) {
            val animator = ValueAnimator.ofFloat(0f, shift * distanceBetweenElements)
            animator.duration = duration
            animator.addUpdateListener {
                circleElements.forEach { view ->
                    view.layoutParams = (view.layoutParams as ConstraintLayout.LayoutParams).apply {
                        circleAngle += animator.animatedValue as Float
                    }
                }
            }
            animator.start()
        }

        private fun addCircleToConstraintLayout(
            layout: ConstraintLayout,
            centerId: Int,
            circleElements: List<View>,
            circleStyle: CircleStyle
        ) {
            circleElements.forEach {
                layout.addView(it, layoutParams)
            }
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout)

            innerCircleElements.forEachIndexed { index, view ->
                constraintSet.constrainCircle(
                    view.id,
                    centerId,
                    circleStyle.radius,
                    index * distanceBetweenElements
                )
            }
            constraintSet.applyTo(layout)
        }

        private fun createCircleElements(alphabet: String, circleStyle: CircleStyle) =
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
}

fun Float.dpToPx(context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
).roundToInt()

fun Float.spToPx(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    this,
    context.resources.displayMetrics
)