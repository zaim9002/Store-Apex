package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val roleKey: String, val displayNameAr: String) {
    SUPER_ADMIN("super_admin", "المدير العام (Super Admin)"),
    ADMIN("admin", "مدير محتوى (Admin)"),
    MODERATOR("moderator", "مشرف (Moderator)"),
    USER("user", "مستخدم عادي (User)");

    companion object {
        fun fromKey(key: String): UserRole {
            return entries.firstOrNull {
                it.roleKey.equals(key, ignoreCase = true) || it.name.equals(key, ignoreCase = true)
            } ?: USER
        }
    }
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
    val role: String = UserRole.USER.roleKey, // "super_admin", "admin", "moderator", "user"
    val avatar: String = "",
    val passwordHash: String = "",
    val status: String = AccountStatus.ACTIVE.name,
    val createdAt: Long = System.currentTimeMillis()
) {
    val isSuperAdmin: Boolean
        get() = role.equals(UserRole.SUPER_ADMIN.roleKey, ignoreCase = true) || role.equals(UserRole.SUPER_ADMIN.name, ignoreCase = true)

    val isAdmin: Boolean
        get() = isSuperAdmin || role.equals(UserRole.ADMIN.roleKey, ignoreCase = true) || role.equals(UserRole.ADMIN.name, ignoreCase = true)

    val isModerator: Boolean
        get() = isAdmin || role.equals(UserRole.MODERATOR.roleKey, ignoreCase = true) || role.equals(UserRole.MODERATOR.name, ignoreCase = true)

    val canAccessAdminPanel: Boolean
        get() = isSuperAdmin || isAdmin || isModerator

    val canManageAdmins: Boolean
        get() = isSuperAdmin

    fun toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "role" to role,
            "avatar" to avatar,
            "passwordHash" to passwordHash,
            "status" to status,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromFirestoreMap(data: Map<String, Any?>, idFallback: String = ""): UserEntity {
            return UserEntity(
                id = (data["id"] as? String) ?: idFallback,
                name = (data["name"] as? String) ?: "",
                email = (data["email"] as? String) ?: "",
                role = (data["role"] as? String) ?: UserRole.USER.roleKey,
                avatar = (data["avatar"] as? String) ?: "",
                passwordHash = (data["passwordHash"] as? String) ?: "",
                status = (data["status"] as? String) ?: AccountStatus.ACTIVE.name,
                createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}

@Entity(tableName = "admins")
data class AdminEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val email: String,
    val name: String,
    val role: String = UserRole.ADMIN.roleKey, // "super_admin", "admin", "moderator"
    val canAddApp: Boolean = true,
    val canEditApp: Boolean = true,
    val canDeleteApp: Boolean = false,
    val canPublish: Boolean = true,
    val canUploadFiles: Boolean = true,
    val canManageAdmins: Boolean = false,
    val status: String = AccountStatus.ACTIVE.name, // "ACTIVE", "SUSPENDED"
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "email" to email,
            "name" to name,
            "role" to role,
            "canAddApp" to canAddApp,
            "canEditApp" to canEditApp,
            "canDeleteApp" to canDeleteApp,
            "canPublish" to canPublish,
            "canUploadFiles" to canUploadFiles,
            "canManageAdmins" to canManageAdmins,
            "status" to status,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromFirestoreMap(data: Map<String, Any?>, idFallback: String = ""): AdminEntity {
            return AdminEntity(
                id = (data["id"] as? String) ?: idFallback,
                userId = (data["userId"] as? String) ?: idFallback,
                email = (data["email"] as? String) ?: "",
                name = (data["name"] as? String) ?: "",
                role = (data["role"] as? String) ?: UserRole.ADMIN.roleKey,
                canAddApp = (data["canAddApp"] as? Boolean) ?: true,
                canEditApp = (data["canEditApp"] as? Boolean) ?: true,
                canDeleteApp = (data["canDeleteApp"] as? Boolean) ?: false,
                canPublish = (data["canPublish"] as? Boolean) ?: true,
                canUploadFiles = (data["canUploadFiles"] as? Boolean) ?: true,
                canManageAdmins = (data["canManageAdmins"] as? Boolean) ?: false,
                status = (data["status"] as? String) ?: AccountStatus.ACTIVE.name,
                createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}
