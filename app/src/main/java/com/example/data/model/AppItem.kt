package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AppType {
    APP,
    GAME
}

enum class DownloadSource {
    UPLOAD,
    DIRECT_LINK
}

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String, // "APP" or "GAME"
    val developer: String,
    val shortDescription: String,
    val description: String,
    val version: String,
    val size: String, // e.g., "48 MB", "1.4 GB"
    val packageName: String,
    val androidVersion: String, // e.g., "Android 9.0+"
    val category: String,
    val subCategory: String = "",
    val ageRating: String = "3+",
    val iconUrl: String = "",
    val bannerUrl: String = "",
    val screenshots: String = "", // Comma-separated URLs or names
    val apkUrl: String = "",
    val xapkUrl: String = "",
    val downloadSource: String = DownloadSource.DIRECT_LINK.name,
    val downloadCount: Long = 0,
    val rating: Float = 4.5f,
    val ratingCount: Int = 120,
    val published: Boolean = true,
    val isFeatured: Boolean = false,
    val createdBy: String = "admin@apexstore.com",
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)
