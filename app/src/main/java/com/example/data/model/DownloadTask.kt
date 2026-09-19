package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED
}

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey
    val id: String,
    val appId: String,
    val appName: String,
    val appIcon: String,
    val version: String,
    val fileType: String, // "APK" or "XAPK"
    val fileSize: String,
    val status: String = DownloadStatus.QUEUED.name,
    val progress: Float = 0f, // 0.0 to 1.0
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 10485760L,
    val speed: String = "0 MB/s",
    val localUri: String = "",
    val errorMessage: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
