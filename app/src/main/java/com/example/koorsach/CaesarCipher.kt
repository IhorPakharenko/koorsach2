package com.example.koorsach

import java.math.BigInteger

object CaesarCipher {
    fun encrypt(toEncrypt: String, alphabet: String, key: BigInteger): Result {
        val operation = Encryption(toEncrypt, alphabet, key)
        return Result(text = operation.applyOperation(), offset = operation.offset)
    }

    fun decrypt(toDecrypt: String, alphabet: String, key: BigInteger): Result {
        val operation = Decryption(toDecrypt, alphabet, key)
        return Result(text = operation.applyOperation(), offset = operation.offset)
    }

    abstract class CipherOperation(
        private val original: String,
        private val alphabet: String,
        private val key: BigInteger
    ) {
        abstract val offset: Int

        fun applyOperation() = shift(original, alphabet, offset)

        fun shift(original: String, alphabet: String, offset: Int) =
            original.map { originalChar ->
                val charIndexInAlphabet = alphabet.indexOf(originalChar, ignoreCase = true)
                if (charIndexInAlphabet >= 0) {
                    val unsafeIndex = charIndexInAlphabet + offset
                    val index = when {
                        unsafeIndex > alphabet.lastIndex -> unsafeIndex - alphabet.length
                        unsafeIndex < 0 -> unsafeIndex + alphabet.length
                        else -> unsafeIndex
                    }
                    alphabet[index].let { newChar ->
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

    class Encryption(
        original: String,
        alphabet: String,
        key: BigInteger
    ) : CipherOperation(original, alphabet, key) {
        override val offset = (key % alphabet.length.toBigInteger()).toInt()
    }

    class Decryption(
        original: String,
        alphabet: String,
        key: BigInteger
    ) : CipherOperation(original, alphabet, key) {
        override val offset = (key % alphabet.length.toBigInteger()).toInt().unaryMinus()
    }

    data class Result(val text: String, val offset: Int)
}