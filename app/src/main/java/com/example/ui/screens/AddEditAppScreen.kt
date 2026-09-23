package com.example.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppEntity
import com.example.data.model.AppType
import com.example.data.model.CategoryData
import com.example.data.model.DownloadSource
import com.example.ui.ApexStoreViewModel
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTertiary
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAppScreen(
    app: AppEntity,
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf(app.name) }
    var type by remember { mutableStateOf(app.type.ifBlank { AppType.APP.name }) }
    var developer by remember { mutableStateOf(app.developer) }
    var shortDesc by remember { mutableStateOf(app.shortDescription) }
    var fullDesc by remember { mutableStateOf(app.description) }
    var version by remember { mutableStateOf(app.version.ifBlank { "1.0.0" }) }
    var size by remember { mutableStateOf(app.size.ifBlank { "25 MB" }) }
    var packageName by remember { mutableStateOf(app.packageName) }
    var androidVersion by remember { mutableStateOf(app.androidVersion.ifBlank { "Android 8.0+" }) }
    var category by remember { mutableStateOf(app.category.ifBlank { if (type == "GAME") "action" else "tools" }) }
    var iconUrl by remember { mutableStateOf(app.iconUrl) }
    var bannerUrl by remember { mutableStateOf(app.bannerUrl) }
    var screenshots by remember { mutableStateOf(app.screenshots) }
    var apkUrl by remember { mutableStateOf(app.apkUrl) }
    var xapkUrl by remember { mutableStateOf(app.xapkUrl) }
    var downloadSource by remember { mutableStateOf(app.downloadSource) }
    var published by remember { mutableStateOf(if (app.name.isBlank()) true else app.published) }
    var isFeatured by remember { mutableStateOf(app.isFeatured) }

    var selectedFileName by remember { mutableStateOf("") }
    var isExtractingApk by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val uploadState by viewModel.uploadState.collectAsState()
    val effectiveAppId = remember(app.id) {
        if (app.id.isBlank() || app.id.startsWith("new_")) "app-${System.currentTimeMillis()}" else app.id
    }

    // Photo Picker launcher for App Icon (Google Play Policy Zero-Permission)
    val iconPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { iconUri ->
            viewModel.saveIconFile(effectiveAppId, iconUri) { savedFile ->
                iconUrl = savedFile.absolutePath
            }
        }
    }

    // Photo Picker launcher for Screenshots (Google Play Policy Zero-Permission)
    val screenshotPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { shotUri ->
            val count = screenshots.split(",").filter { it.isNotBlank() }.size
            viewModel.saveScreenshotFile(effectiveAppId, shotUri, count + 1) { savedFile ->
                screenshots = if (screenshots.isBlank()) savedFile.absolutePath else "$screenshots,${savedFile.absolutePath}"
            }
        }
    }

    // File picker launcher for APK and XAPK files
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { fileUri ->
            coroutineScope.launch {
                isExtractingApk = true
                var displayName = "package.apk"
                var fileSize = 25L * 1024 * 1024

                // Extract metadata from content resolver
                withContext(Dispatchers.IO) {
                    try {
                        context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                                if (nameIdx != -1) displayName = cursor.getString(nameIdx) ?: "package.apk"
                                if (sizeIdx != -1) fileSize = cursor.getLong(sizeIdx).coerceAtLeast(1024L)
                            }
                        }

                        // Check if APK: copy to temp cache and extract info
                        if (displayName.endsWith(".apk", ignoreCase = true)) {
                            val tempFile = File(context.cacheDir, "temp_inspect_${System.currentTimeMillis()}.apk")
                            context.contentResolver.openInputStream(fileUri)?.use { input ->
                                FileOutputStream(tempFile).use { output ->
                                    input.copyTo(output)
                                }
                            }

                            val pkgInfo = context.packageManager.getPackageArchiveInfo(
                                tempFile.absolutePath,
                                PackageManager.GET_META_DATA
                            )

                            if (pkgInfo != null) {
                                withContext(Dispatchers.Main) {
                                    if (packageName.isBlank() || packageName.startsWith("com.example")) {
                                        packageName = pkgInfo.packageName ?: packageName
                                    }
                                    if (version.isBlank() || version == "1.0.0") {
                                        version = pkgInfo.versionName ?: "1.0.0"
                                    }
                                    val mbSize = String.format("%.1f MB", fileSize / (1024.0 * 1024.0))
                                    size = mbSize
                                }
                            }
                            tempFile.delete()
                        } else if (displayName.endsWith(".xapk", ignoreCase = true)) {
                            withContext(Dispatchers.Main) {
                                type = AppType.GAME.name
                                val mbSize = String.format("%.1f MB", fileSize / (1024.0 * 1024.0))
                                size = mbSize
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                selectedFileName = displayName
                isExtractingApk = false

                val isXapk = displayName.endsWith(".xapk", ignoreCase = true)
                // Save package file directly into app storage
                viewModel.savePackageFile(effectiveAppId, fileUri, isXapk) { localPackageFile, bytesWritten ->
                    if (isXapk) {
                        xapkUrl = localPackageFile.absolutePath
                    } else {
                        apkUrl = localPackageFile.absolutePath
                    }
                }

                // Trigger real-time upload progress simulation
                viewModel.simulateUpload(displayName, fileSize) { generatedUrl ->
                    if (isXapk) {
                        if (xapkUrl.isBlank()) xapkUrl = generatedUrl
                    } else {
                        if (apkUrl.isBlank()) apkUrl = generatedUrl
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Top Header
        Surface(
            color = ApexSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .border(1.dp, ApexBorder.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ApexSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "إلغاء",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (app.name.isBlank()) "إضافة تطبيق / لعبة" else "تعديل: ${app.name}",
                    color = ApexTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val finalId = if (app.id.isBlank() || app.id.startsWith("new_")) "app-${System.currentTimeMillis()}" else app.id
                        val toSave = app.copy(
                            id = finalId,
                            name = name.ifBlank { "تطبيق بدون اسم" },
                            type = type,
                            developer = developer.ifBlank { "APEX Publisher" },
                            shortDescription = shortDesc.ifBlank { "تطبيق أندرويد مميز متاح للتحميل عبر متجر APEX STORE" },
                            description = fullDesc.ifBlank { shortDesc },
                            version = version.ifBlank { "1.0.0" },
                            size = size.ifBlank { "25 MB" },
                            packageName = packageName.ifBlank { "com.apexstore.${name.lowercase().replace(" ", "")}" },
                            androidVersion = androidVersion.ifBlank { "Android 8.0+" },
                            category = category,
                            iconUrl = iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150" },
                            bannerUrl = bannerUrl.ifBlank { "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800" },
                            screenshots = screenshots,
                            apkUrl = apkUrl.ifBlank { "https://storage.apexstore.com/packages/${UUID.randomUUID()}.apk" },
                            xapkUrl = xapkUrl,
                            downloadSource = DownloadSource.UPLOAD.name,
                            published = published,
                            isFeatured = isFeatured,
                            updatedAt = System.currentTimeMillis()
                        )
                        viewModel.saveApp(toSave)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = ApexBackground),
                    modifier = Modifier.testTag("save_app_submit_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حفظ ونشر", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Form fields
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Type Selector
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "1. نوع العنصر والفئة الأساسية", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            TypeSelectButton(
                                label = "📱 تطبيق (App)",
                                isSelected = type == AppType.APP.name,
                                onClick = {
                                    type = AppType.APP.name
                                    category = "tools"
                                },
                                modifier = Modifier.weight(1f)
                            )
                            TypeSelectButton(
                                label = "🎮 لعبة (Game)",
                                isSelected = type == AppType.GAME.name,
                                onClick = {
                                    type = AppType.GAME.name
                                    category = "action"
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Dropdown
                        val availableCategories = if (type == AppType.GAME.name) CategoryData.gameCategories else CategoryData.appCategories
                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                        ) {
                            val currentCategoryName = availableCategories.find { it.id == category }?.nameAr ?: category
                            OutlinedTextField(
                                value = currentCategoryName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("التصنيف الرئيسي", fontSize = 12.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ApexPrimary,
                                    unfocusedBorderColor = ApexBorder,
                                    focusedContainerColor = ApexSurfaceVariant,
                                    unfocusedContainerColor = ApexSurfaceVariant,
                                    focusedTextColor = ApexTextPrimary,
                                    unfocusedTextColor = ApexTextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = categoryDropdownExpanded,
                                onDismissRequest = { categoryDropdownExpanded = false },
                                modifier = Modifier.background(ApexSurfaceCard)
                            ) {
                                availableCategories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.nameAr, color = ApexTextPrimary, fontSize = 13.sp) },
                                        onClick = {
                                            category = cat.id
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Package File Upload (APK / XAPK)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = ApexPrimary, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "2. رفع ملف الحزمة والتخزين السحابي (APK / XAPK)", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "اختر ملف من ذاكرة جهازك أو ابدأ رفعه مباشرة إلى خادم التخزين السحابي لـ APEX STORE مع استخراج البيانات التلقائي.",
                            color = ApexTextSecondary,
                            fontSize = 11.5.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // File Selector Button
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    filePickerLauncher.launch("*/*")
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = ApexBackground),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "اختر ملف من جهازك", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val simulatedFileName = if (type == "GAME") "${name.ifBlank { "game" }.lowercase().replace(" ", "_")}.xapk"
                                    else "${name.ifBlank { "app" }.lowercase().replace(" ", "_")}.apk"
                                    val simulatedSizeBytes = if (type == "GAME") 450L * 1024 * 1024 else 35L * 1024 * 1024

                                    selectedFileName = simulatedFileName
                                    viewModel.simulateUpload(simulatedFileName, simulatedSizeBytes) { generatedUrl ->
                                        if (simulatedFileName.endsWith(".xapk")) {
                                            xapkUrl = generatedUrl
                                        } else {
                                            apkUrl = generatedUrl
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ApexSurfaceVariant, contentColor = ApexPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder),
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "توليد رفع سحابي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (isExtractingApk) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(color = ApexPrimary, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "جارٍ استخراج حزمة APK وقراءة البيانات...", color = ApexPrimary, fontSize = 11.sp)
                            }
                        }

                        if (selectedFileName.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ApexTertiary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "الملف المختار: $selectedFileName", color = ApexTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        // Upload Progress Bar
                        if (uploadState.isUploading) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = { uploadState.progress },
                                color = ApexPrimary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(7.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "جاري الرفع ${(uploadState.progress * 100).toInt()}%", color = ApexPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = uploadState.speed, color = ApexTextSecondary, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FormTextField(
                            value = apkUrl,
                            onValueChange = { apkUrl = it },
                            label = "رابط ملف APK المباشر",
                            isLtr = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FormTextField(
                            value = xapkUrl,
                            onValueChange = { xapkUrl = it },
                            label = "رابط ملف XAPK المباشر (اختياري للألعاب الضخمة)",
                            isLtr = true
                        )
                    }
                }
            }

            // Section 3: Basic & Technical Information
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "3. المعلومات الأساسية والتقنية", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        FormTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "اسم التطبيق / اللعبة *",
                            testTag = "form_app_name"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FormTextField(
                            value = developer,
                            onValueChange = { developer = it },
                            label = "اسم المطور أو الشركة المطورة *"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FormTextField(
                            value = packageName,
                            onValueChange = { packageName = it },
                            label = "اسم الحزمة (Package Name مثل com.example.app) *",
                            isLtr = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                            FormTextField(
                                value = version,
                                onValueChange = { version = it },
                                label = "الإصدار (مثل 1.2.0)",
                                modifier = Modifier.weight(1f),
                                isLtr = true
                            )
                            FormTextField(
                                value = size,
                                onValueChange = { size = it },
                                label = "الحجم (مثل 45 MB)",
                                modifier = Modifier.weight(1f),
                                isLtr = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FormTextField(
                            value = androidVersion,
                            onValueChange = { androidVersion = it },
                            label = "نظام أندرويد المطلوب (مثل Android 8.0+)",
                            isLtr = true
                        )
                    }
                }
            }

            // Section 4: Descriptions
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "4. الوصف والمميزات", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        FormTextField(
                            value = shortDesc,
                            onValueChange = { shortDesc = it },
                            label = "الوصف المختصر (يظهر في البطاقات الرئيسية)"
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FormTextField(
                            value = fullDesc,
                            onValueChange = { fullDesc = it },
                            label = "الوصف التفصيلي وقائمة المميزات",
                            maxLines = 5
                        )
                    }
                }
            }

            // Section 5: Media & Visual Assets
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "5. الوسائط والصور المعروضة", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        // App Icon Row with Photo Picker Button
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    iconPickerLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color.White),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text(text = "🖼️ رفع أيقونة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            FormTextField(
                                value = iconUrl,
                                onValueChange = { iconUrl = it },
                                label = "مسار أو رابط الأيقونة (Icon)",
                                isLtr = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        FormTextField(
                            value = bannerUrl,
                            onValueChange = { bannerUrl = it },
                            label = "رابط بانر العرض المميز (Banner URL)",
                            isLtr = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Screenshot Row with Photo Picker Button
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    screenshotPickerLauncher.launch(
                                        androidx.activity.result.PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ApexSurfaceVariant, contentColor = ApexPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Text(text = "📸 إضافة لقطة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            FormTextField(
                                value = screenshots,
                                onValueChange = { screenshots = it },
                                label = "لقطات الشاشة (مسارات أو روابط)",
                                isLtr = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Section 6: Publishing & Visibility Options
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "6. خيارات النشر والظهور في المتجر", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "نشر فوري في المتجر", color = ApexTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "يصبح العنصر مرئياً وقابلاً للتنزيل لجميع المستخدمين فوراً", color = ApexTextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = published,
                                onCheckedChange = { published = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ApexTertiary, checkedTrackColor = ApexTertiary.copy(alpha = 0.3f))
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "تمييز التطبيق (Featured في الصفحة الرئيسية)", color = ApexTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "إبرازه في أعلى الصفحة الرئيسية وبانر اليوم المميز", color = ApexTextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = isFeatured,
                                onCheckedChange = { isFeatured = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ApexPrimary, checkedTrackColor = ApexPrimary.copy(alpha = 0.3f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeSelectButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) ApexPrimary.copy(alpha = 0.18f) else ApexSurfaceVariant)
            .border(1.dp, if (isSelected) ApexPrimary else ApexBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) ApexPrimary else ApexTextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    maxLines: Int = 1,
    testTag: String = "",
    isLtr: Boolean = false
) {
    if (isLtr) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label, fontSize = 12.sp) },
                maxLines = maxLines,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ApexPrimary,
                    unfocusedBorderColor = ApexBorder,
                    focusedContainerColor = ApexSurfaceVariant,
                    unfocusedContainerColor = ApexSurfaceVariant,
                    focusedTextColor = ApexTextPrimary,
                    unfocusedTextColor = ApexTextPrimary
                ),
                modifier = if (testTag.isNotBlank()) modifier.testTag(testTag) else modifier
            )
        }
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontSize = 12.sp) },
            maxLines = maxLines,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ApexPrimary,
                unfocusedBorderColor = ApexBorder,
                focusedContainerColor = ApexSurfaceVariant,
                unfocusedContainerColor = ApexSurfaceVariant,
                focusedTextColor = ApexTextPrimary,
                unfocusedTextColor = ApexTextPrimary
            ),
            modifier = if (testTag.isNotBlank()) modifier.testTag(testTag) else modifier
        )
    }
}
