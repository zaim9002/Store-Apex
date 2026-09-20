package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

object PackageInstallerHelper {

    /**
     * Installs an APK or XAPK file using Android FileProvider and PackageInstaller Intent.
     * Handles Android 7.0+ content:// URI securely and requests REQUEST_INSTALL_PACKAGES if needed.
     */
    fun installPackage(context: Context, file: File, onStatus: (String) -> Unit): Boolean {
        if (!file.exists()) {
            onStatus("الملف غير موجود في الذاكرة!")
            return false
        }

        val fileName = file.name.lowercase()

        // Check Unknown Sources Permission for Android 8.0 (Oreo) +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!context.packageManager.canRequestPackageInstalls()) {
                onStatus("يرجى تفعيل إذن تثبيت التطبيقات من مصادر غير معروفة للتطبيق")
                val manageIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(manageIntent)
                return false
            }
        }

        return if (fileName.endsWith(".xapk")) {
            installXapk(context, file, onStatus)
        } else {
            installApk(context, file, onStatus)
        }
    }

    /**
     * Extracts and installs an APK file via FileProvider Intent.
     */
    fun installApk(context: Context, apkFile: File, onStatus: (String) -> Unit): Boolean {
        return try {
            val authority = "${context.packageName}.fileprovider"
            val apkUri: Uri = FileProvider.getUriForFile(context, authority, apkFile)

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(installIntent)
            onStatus("جاري فتح مثبت الحزم لنظام أندرويد...")
            true
        } catch (e: Exception) {
            onStatus("فشل بدء التثبيت: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Handles XAPK archive: extracts the primary APK and OBB / split APKs to cache,
     * then triggers installation of the primary APK.
     */
    fun installXapk(context: Context, xapkFile: File, onStatus: (String) -> Unit): Boolean {
        return try {
            onStatus("جاري فك ضغط حزمة XAPK واستخراج ملف APK...")
            val extractedDir = File(context.cacheDir, "extracted_${System.currentTimeMillis()}")
            if (!extractedDir.exists()) extractedDir.mkdirs()

            var primaryApk: File? = null

            ZipInputStream(FileInputStream(xapkFile)).use { zis ->
                var entry = zis.nextEntry
                while (entry != null) {
                    val entryName = entry.name
                    // Prevent path traversal vulnerability (Zip Slip)
                    val destinationFile = File(extractedDir, entryName)
                    val canonicalDest = destinationFile.canonicalPath
                    if (!canonicalDest.startsWith(extractedDir.canonicalPath)) {
                        throw SecurityException("محاولة اختراق أمني (Zip Path Traversal) تم منعها!")
                    }

                    if (entry.isDirectory) {
                        destinationFile.mkdirs()
                    } else {
                        destinationFile.parentFile?.mkdirs()
                        FileOutputStream(destinationFile).use { fos ->
                            zis.copyTo(fos)
                        }

                        if (entryName.endsWith(".apk", ignoreCase = true) && primaryApk == null) {
                            primaryApk = destinationFile
                        }
                    }
                    zis.closeEntry()
                    entry = zis.nextEntry
                }
            }

            if (primaryApk != null && primaryApk!!.exists()) {
                installApk(context, primaryApk!!, onStatus)
            } else {
                onStatus("لم يتم العثور على ملف APK صالح داخل حزمة XAPK!")
                false
            }
        } catch (e: Exception) {
            onStatus("فشل فك حزمة XAPK: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Saves a stream (e.g. from File Picker Uri or downloaded file) to local app storage.
     */
    fun saveStreamToStorage(context: Context, inputStream: InputStream, targetFileName: String): File {
        val packagesDir = File(context.filesDir, "packages")
        if (!packagesDir.exists()) packagesDir.mkdirs()
        val targetFile = File(packagesDir, targetFileName)
        FileOutputStream(targetFile).use { out ->
            inputStream.copyTo(out)
        }
        return targetFile
    }
}
