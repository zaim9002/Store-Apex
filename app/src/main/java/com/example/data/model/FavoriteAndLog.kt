package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites", primaryKeys = ["appId", "userId"])
data class FavoriteEntity(
    val appId: String,
    val userId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val action: String, // "ADD_APP", "EDIT_APP", "DELETE_APP", "PUBLISH_APP", "UNPUBLISH_APP", "UPLOAD_FILE", "ADD_ADMIN", "DELETE_ADMIN", "UPDATE_PERMISSIONS"
    val actionDetails: String,
    val targetName: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class StoreSettings(
    val storeName: String = "APEX STORE",
    val logoText: String = "APEX STORE",
    val downloadWifiOnly: Boolean = false,
    val autoResumeDownloads: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val maxConcurrentDownloads: Int = 3,
    val defaultLanguage: String = "ar",
    val allowPublicRegistration: Boolean = true,
    val requireAdminApprovalForApps: Boolean = false
)
