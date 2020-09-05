package com.example.koorsach

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private val ukrainianAlphabet = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя"
    private val englishAlphabet = "abcdefghijklmnopqrstuvwxyz"

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
            addToConstraintLayout(container, R.id.btn_go)
        }

        innerCircle = CharsCircle(
            this, englishAlphabet, CharsCircle.CircleStyle(
                radius = 130f.dpToPx(this).roundToInt(),
                textSize = 12f.dpToPx(this)
            )
        ).apply {
            addToConstraintLayout(container, R.id.btn_go)
        }

        et_decrypted.setOnFocusChangeListener { _, focused ->
//            et_encrypted.isEnabled = !focused
            if (focused) {
                et_encrypted.text = null
            }
        }

//        et_decrypted.addTextChangedListener {
//            et_encrypted.text = null
//        }

        et_encrypted.setOnFocusChangeListener { _, focused ->
//            et_decrypted.isEnabled = !focused
            if (focused) {
                et_decrypted.text = null
            }
        }

//        et_encrypted.addTextChangedListener {
//            et_decrypted.text = null
//        }

        btn_go.setOnClickListener {
            hideKeyboard()
            updateUi(
                decrypted = et_decrypted.text.toString(),
                encrypted = et_encrypted.text.toString(),
                key = et_key.text.toString()
            )
        }
    }

    private fun updateUi(decrypted: String, encrypted: String, key: String) {
        val keyBigInt = key.toBigIntegerOrNull()

        if (keyBigInt == null) {
            Snackbar.make(container, "Enter key", Snackbar.LENGTH_SHORT).show()
            return
        }

        when {
            decrypted.isNotEmpty() -> {
                val encryptionResult = CaesarCipher.encrypt(decrypted, englishAlphabet, keyBigInt)
                et_encrypted.setText(encryptionResult.text)
                innerCircle.shiftAnimated(container, R.id.btn_go, encryptionResult.offset)
            }
            encrypted.isNotEmpty() -> {
                val decryptionResult = CaesarCipher.decrypt(encrypted, englishAlphabet, keyBigInt)
                et_decrypted.setText(decryptionResult.text)
                innerCircle.shiftAnimated(container, R.id.btn_go, decryptionResult.offset)
            }
            else -> {
                Snackbar.make(
                    container,
                    "Enter either decrypted or encrypted text",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}