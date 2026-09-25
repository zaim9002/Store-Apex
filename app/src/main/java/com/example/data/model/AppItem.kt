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
    val type: String = AppType.APP.name, // "APP" or "GAME"
    val developer: String = "APEX Studios",
    val shortDescription: String = "",
    val description: String = "",
    val version: String = "1.0.0",
    val size: String = "25 MB", // e.g., "48 MB", "1.4 GB"
    val packageName: String = "com.apexstore.app",
    val androidVersion: String = "Android 8.0+", // e.g., "Android 9.0+"
    val category: String = "tools",
    val subCategory: String = "",
    val ageRating: String = "3+",
    val iconUrl: String = "",
    val bannerUrl: String = "",
    val screenshots: String = "", // Comma-separated URLs or stored locally
    val apkUrl: String = "",
    val xapkUrl: String = "",
    val fileUrl: String = "", // Direct download file URL in Firebase Storage
    val fileFormat: String = "APK", // "APK" or "XAPK"
    val websiteUrl: String = "", // Official or publisher website URL
    val downloadSource: String = DownloadSource.DIRECT_LINK.name,
    val downloadCount: Long = 0,
    val rating: Float = 4.5f,
    val ratingCount: Int = 120,
    val published: Boolean = true,
    val isFeatured: Boolean = false,
    val createdBy: String = "admin",
    val status: String = "ACTIVE",
    val updatedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Returns a clean list of screenshot URLs
     */
    fun getScreenshotList(): List<String> {
        if (screenshots.isBlank()) return emptyList()
        return screenshots.split(",").map { it.trim() }.filter { it.isNotBlank() }
    }

    /**
     * Resolves the effective download URL (prioritizing fileUrl, apkUrl, or xapkUrl)
     */
    val effectiveDownloadUrl: String
        get() = when {
            fileUrl.isNotBlank() -> fileUrl
            apkUrl.isNotBlank() -> apkUrl
            xapkUrl.isNotBlank() -> xapkUrl
            else -> websiteUrl
        }

    fun toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "type" to type,
            "developer" to developer,
            "shortDescription" to shortDescription,
            "description" to description,
            "version" to version,
            "size" to size,
            "packageName" to packageName,
            "androidVersion" to androidVersion,
            "category" to category,
            "subCategory" to subCategory,
            "ageRating" to ageRating,
            "iconUrl" to iconUrl,
            "bannerUrl" to bannerUrl,
            "screenshots" to getScreenshotList(),
            "fileUrl" to fileUrl.ifBlank { apkUrl.ifBlank { xapkUrl } },
            "fileFormat" to fileFormat,
            "websiteUrl" to websiteUrl,
            "apkUrl" to apkUrl,
            "xapkUrl" to xapkUrl,
            "downloadSource" to downloadSource,
            "downloadCount" to downloadCount,
            "rating" to rating.toDouble(),
            "ratingCount" to ratingCount,
            "published" to published,
            "featured" to isFeatured,
            "createdBy" to createdBy,
            "status" to status,
            "updatedAt" to updatedAt,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromFirestoreMap(data: Map<String, Any?>, fallbackId: String = ""): AppEntity {
            val rawScreenshots = data["screenshots"]
            val screenshotsStr = when (rawScreenshots) {
                is List<*> -> rawScreenshots.filterNotNull().joinToString(",") { it.toString() }
                is String -> rawScreenshots
                else -> ""
            }

            val fileUrlVal = (data["fileUrl"] as? String) ?: ""
            val apkUrlVal = (data["apkUrl"] as? String) ?: fileUrlVal
            val xapkUrlVal = (data["xapkUrl"] as? String) ?: ""
            val fileFormatVal = (data["fileFormat"] as? String) ?: if (xapkUrlVal.isNotBlank()) "XAPK" else "APK"

            return AppEntity(
                id = (data["id"] as? String) ?: fallbackId,
                name = (data["name"] as? String) ?: "",
                type = (data["type"] as? String) ?: AppType.APP.name,
                developer = (data["developer"] as? String) ?: "APEX Developer",
                shortDescription = (data["shortDescription"] as? String) ?: "",
                description = (data["description"] as? String) ?: "",
                version = (data["version"] as? String) ?: "1.0.0",
                size = (data["size"] as? String) ?: "25 MB",
                packageName = (data["packageName"] as? String) ?: "com.apexstore.app",
                androidVersion = (data["androidVersion"] as? String) ?: "Android 8.0+",
                category = (data["category"] as? String) ?: "tools",
                subCategory = (data["subCategory"] as? String) ?: "",
                ageRating = (data["ageRating"] as? String) ?: "3+",
                iconUrl = (data["iconUrl"] as? String) ?: "",
                bannerUrl = (data["bannerUrl"] as? String) ?: "",
                screenshots = screenshotsStr,
                apkUrl = apkUrlVal,
                xapkUrl = xapkUrlVal,
                fileUrl = fileUrlVal.ifBlank { apkUrlVal },
                fileFormat = fileFormatVal,
                websiteUrl = (data["websiteUrl"] as? String) ?: "",
                downloadSource = (data["downloadSource"] as? String) ?: DownloadSource.DIRECT_LINK.name,
                downloadCount = (data["downloadCount"] as? Number)?.toLong() ?: 0L,
                rating = ((data["rating"] as? Number)?.toFloat()) ?: 4.5f,
                ratingCount = ((data["ratingCount"] as? Number)?.toInt()) ?: 0,
                published = (data["published"] as? Boolean) ?: true,
                isFeatured = (data["featured"] as? Boolean) ?: ((data["isFeatured"] as? Boolean) ?: false),
                createdBy = (data["createdBy"] as? String) ?: "admin",
                status = (data["status"] as? String) ?: "ACTIVE",
                updatedAt = (data["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                createdAt = (data["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}
