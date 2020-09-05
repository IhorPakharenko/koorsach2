package com.example.koorsach

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val ukrainianAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"
    private val englishAlphabet = "abcdefghijklmnopqrstuvwxyz"

    private val cipher = CeasarCipher(englishAlphabet)

    private lateinit var innerCircle: CharsCircle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CharsCircle(
            this, englishAlphabet, CharsCircle.CircleStyle(
                radius = 160f.dpToPx(this).roundToInt(),
                textSize = 14f.dpToPx(this)
            )
        ).apply {
            addToConstraintLayout(container, R.id.img_center)
        }

        innerCircle = CharsCircle(
            this, englishAlphabet, CharsCircle.CircleStyle(
                radius = 130f.dpToPx(this).roundToInt(),
                textSize = 12f.dpToPx(this)
            )
        ).apply {
            addToConstraintLayout(container, R.id.img_center)
        }

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

    private fun updateUi(text: String, key: BigInteger) {
        txt_result.text = cipher.enrypt(text, key)
//        innerCircle.shiftAnimated(cipher.getShift(key))
    }
}