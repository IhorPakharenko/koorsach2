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
            container,
            R.id.btn_go,
            englishAlphabet,
            radius = 160f.dpToPx(this).roundToInt(),
            elementsTextSize = 14f.dpToPx(this)
        ).apply {
            addToConstraintLayout()
        }

        innerCircle = CharsCircle(
            container,
            R.id.btn_go,
            englishAlphabet,
            radius = 130f.dpToPx(this).roundToInt(),
            elementsTextSize = 12f.dpToPx(this)
        ).apply {
            addToConstraintLayout()
        }

        et_decrypted.setOnFocusChangeListener { _, focused ->
            if (focused) {
                et_encrypted.text = null
            }
        }

        et_encrypted.setOnFocusChangeListener { _, focused ->
            if (focused) {
                et_decrypted.text = null
            }
        }

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
                innerCircle.setOffset(encryptionResult.offset)
            }
            encrypted.isNotEmpty() -> {
                val decryptionResult = CaesarCipher.decrypt(encrypted, englishAlphabet, keyBigInt)
                et_decrypted.setText(decryptionResult.text)
                innerCircle.setOffset(decryptionResult.offset)
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