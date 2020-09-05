package com.example.koorsach

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.koorsach.Consts.UKRAINIAN_ALPHABET
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var operationMode: OperationMode

    private lateinit var innerCircle: CharsCircle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CharsCircle(
            container,
            R.id.btn_go,
            UKRAINIAN_ALPHABET,
            radius = 160f.dpToPx(this).roundToInt(),
            elementsTextSize = 12f.dpToPx(this)
        ).apply {
            addToConstraintLayout()
        }

        innerCircle = CharsCircle(
            container,
            R.id.btn_go,
            UKRAINIAN_ALPHABET,
            radius = 130f.dpToPx(this).roundToInt(),
            elementsTextSize = 9f.dpToPx(this)
        ).apply {
            addToConstraintLayout()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        (menu.findItem(R.id.spinner).actionView as Spinner).apply {
            onItemSelectedListener = this@MainActivity

            adapter = ArrayAdapter(
                this@MainActivity,
                R.layout.item_spinner,
                arrayOf("Encrypt", "Decrypt")
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
        return true
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        onEncryptSelected()
    }

    override fun onItemSelected(
        adapterView: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        when (position) {
            0 -> onEncryptSelected()
            1 -> onDecryptSelected()
            else -> throw IllegalArgumentException()
        }
    }

    private fun onEncryptSelected() {
        operationMode = OperationMode.ENCRYPT
        et_encrypted.apply {
            text = null
            isEnabled = false
        }
        et_decrypted.isEnabled = true
    }

    private fun onDecryptSelected() {
        operationMode = OperationMode.DECRYPT
        et_decrypted.apply {
            text = null
            isEnabled = false
        }
        et_encrypted.isEnabled = true
    }

    private fun updateUi(decrypted: String, encrypted: String, key: String) {
        val keyBigInt = key.toBigIntegerOrNull()

        if (keyBigInt == null) {
            Snackbar.make(container, "Enter integer key", Snackbar.LENGTH_SHORT).show()
            return
        }

        when (operationMode) {
            OperationMode.ENCRYPT -> {
                if (decrypted.isNotEmpty()) {
                    val encryptionResult =
                        CaesarCipher.encrypt(decrypted, UKRAINIAN_ALPHABET, keyBigInt)
                    et_encrypted.setText(encryptionResult.text)
                    innerCircle.setOffset(encryptionResult.offset)
                } else {
                    Snackbar.make(container, "Enter decrypted text", Snackbar.LENGTH_SHORT).show()
                }
            }
            OperationMode.DECRYPT -> {
                if (encrypted.isNotEmpty()) {
                    val decryptionResult =
                        CaesarCipher.decrypt(encrypted, UKRAINIAN_ALPHABET, keyBigInt)
                    et_decrypted.setText(decryptionResult.text)
                    innerCircle.setOffset(decryptionResult.offset)
                } else {
                    Snackbar.make(container, "Enter encrypted text", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}