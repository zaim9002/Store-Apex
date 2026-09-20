package com.example.util

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object PasswordHasher {
    private const val ITERATIONS = 10000
    private const val SALT_LENGTH = 16

    /**
     * Generates a secure salt and hashes the plain password with SHA-256 PBKDF-style.
     * Format: saltBase64:hashBase64
     */
    fun hashPassword(password: String): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)

        val hash = pbkdfSha256(password, salt)
        val saltB64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val hashB64 = Base64.encodeToString(hash, Base64.NO_WRAP)
        return "$saltB64:$hashB64"
    }

    /**
     * Verifies a plain password against the stored salt:hash string.
     */
    fun verifyPassword(password: String, storedHash: String): Boolean {
        return try {
            val parts = storedHash.split(":")
            if (parts.size != 2) return false

            val salt = Base64.decode(parts[0], Base64.NO_WRAP)
            val expectedHash = Base64.decode(parts[1], Base64.NO_WRAP)

            val actualHash = pbkdfSha256(password, salt)
            MessageDigest.isEqual(expectedHash, actualHash)
        } catch (_: Exception) {
            false
        }
    }

    private fun pbkdfSha256(password: String, salt: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.reset()
        digest.update(salt)
        var input = digest.digest(password.toByteArray(Charsets.UTF_8))
        for (i in 1 until ITERATIONS) {
            digest.reset()
            input = digest.digest(input)
        }
        return input
    }
}
