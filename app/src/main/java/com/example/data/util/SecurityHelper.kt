package com.example.data.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Secure password hashing utility using SHA-256 with salt.
 * Ensures passwords are never stored or compared in plain text.
 */
object SecurityHelper {
    private const val DEFAULT_SALT = "APEX_STORE_SECURITY_SALT_2026_@&^%"

    fun hashPassword(password: String, salt: String = DEFAULT_SALT): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val inputWithSalt = "$salt:$password:$salt"
            val hashBytes = md.digest(inputWithSalt.toByteArray(Charsets.UTF_8))
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            // Fallback
            password.hashCode().toString()
        }
    }

    fun verifyPassword(password: String, storedHash: String, salt: String = DEFAULT_SALT): Boolean {
        if (storedHash.isBlank() || password.isBlank()) return false
        val computedHash = hashPassword(password, salt)
        return computedHash.equals(storedHash, ignoreCase = true)
    }
}
