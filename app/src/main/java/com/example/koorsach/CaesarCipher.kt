package com.example.koorsach

import java.math.BigInteger

object CaesarCipher {
    fun encrypt(toEncrypt: String, alphabet: String, key: BigInteger): Result {
        val offset = getOffset(key, alphabet)
        return Result(text = shift(toEncrypt, alphabet, offset), offset = offset)
    }

    fun decrypt(toDecrypt: String, alphabet: String, key: BigInteger): Result {
        val offset = getOffset(key.unaryMinus(), alphabet)
        return Result(text = shift(toDecrypt, alphabet, offset), offset = offset)
    }

    private fun shift(original: String, alphabet: String, offset: Int) =
        original.map { originalChar ->
            val charIndexInAlphabet = alphabet.indexOf(originalChar, ignoreCase = true)
            if (charIndexInAlphabet >= 0) {
                alphabet.getOrNull(charIndexInAlphabet + offset)
                    ?: alphabet[charIndexInAlphabet + offset - alphabet.length].let { newChar ->
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

    private fun getOffset(key: BigInteger, alphabet: String) =
        (key % alphabet.length.toBigInteger()).toInt()

    data class Result(val text: String, val offset: Int)
}