package com.example.koorsach

import java.math.BigInteger
import java.util.*

class CeasarCipher(var alphabet: String) {
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