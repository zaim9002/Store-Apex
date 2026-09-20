package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    SUPER_ADMIN,
    ADMIN,
    USER
}

enum class AccountStatus {
    ACTIVE,
    SUSPENDED
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val role: String, // "SUPER_ADMIN", "ADMIN", "USER"
    val avatar: String = "",
    val passwordHash: String = "", // Salted & hashed PBKDF-SHA256 password
    val status: String = AccountStatus.ACTIVE.name,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "admins")
data class AdminEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val email: String,
    val name: String,
    val canAddApp: Boolean = true,
    val canEditApp: Boolean = true,
    val canDeleteApp: Boolean = false,
    val canPublish: Boolean = true,
    val canUploadFiles: Boolean = true,
    val canManageAdmins: Boolean = false,
    val status: String = AccountStatus.ACTIVE.name, // "ACTIVE", "SUSPENDED"
    val createdAt: Long = System.currentTimeMillis()
)
