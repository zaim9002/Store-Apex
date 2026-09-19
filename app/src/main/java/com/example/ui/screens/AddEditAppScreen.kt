package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppEntity
import com.example.data.model.AppType
import com.example.data.model.DownloadSource
import com.example.ui.ApexStoreViewModel
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

@Composable
fun AddEditAppScreen(
    app: AppEntity,
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(app.name) }
    var type by remember { mutableStateOf(app.type) }
    var developer by remember { mutableStateOf(app.developer) }
    var shortDesc by remember { mutableStateOf(app.shortDescription) }
    var fullDesc by remember { mutableStateOf(app.description) }
    var version by remember { mutableStateOf(app.version) }
    var size by remember { mutableStateOf(app.size) }
    var packageName by remember { mutableStateOf(app.packageName) }
    var androidVersion by remember { mutableStateOf(app.androidVersion) }
    var category by remember { mutableStateOf(app.category) }
    var iconUrl by remember { mutableStateOf(app.iconUrl) }
    var bannerUrl by remember { mutableStateOf(app.bannerUrl) }
    var screenshots by remember { mutableStateOf(app.screenshots) }
    var apkUrl by remember { mutableStateOf(app.apkUrl) }
    var xapkUrl by remember { mutableStateOf(app.xapkUrl) }
    var downloadSource by remember { mutableStateOf(app.downloadSource) }
    var published by remember { mutableStateOf(app.published) }
    var isFeatured by remember { mutableStateOf(app.isFeatured) }

    val uploadState by viewModel.uploadState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Header
        Surface(color = ApexSurface, tonalElevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(ApexSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "إلغاء",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = if (app.name.isBlank()) "إضافة تطبيق/لعبة جديدة" else "تعديل: ${app.name}",
                    color = ApexTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        val toSave = app.copy(
                            name = name,
                            type = type,
                            developer = developer,
                            shortDescription = shortDesc,
                            description = fullDesc,
                            version = version,
                            size = size,
                            packageName = packageName,
                            androidVersion = androidVersion,
                            category = category,
                            iconUrl = iconUrl,
                            bannerUrl = bannerUrl,
                            screenshots = screenshots,
                            apkUrl = apkUrl,
                            xapkUrl = xapkUrl,
                            downloadSource = downloadSource,
                            published = published,
                            isFeatured = isFeatured
                        )
                        viewModel.saveApp(toSave)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = ApexBackground),
                    modifier = Modifier.testTag("save_app_submit_button")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حفظ", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Form fields
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Type Selector (App or Game)
            item {
                Text(text = "نوع العنصر", color = ApexTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TypeSelectButton(
                        label = "📱 تطبيق (App)",
                        isSelected = type == AppType.APP.name,
                        onClick = { type = AppType.APP.name },
                        modifier = Modifier.weight(1f)
                    )
                    TypeSelectButton(
                        label = "🎮 لعبة (Game)",
                        isSelected = type == AppType.GAME.name,
                        onClick = { type = AppType.GAME.name },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Basic Info
            item {
                FormTextField(value = name, onValueChange = { name = it }, label = "اسم التطبيق / اللعبة *", testTag = "form_app_name")
            }
            item {
                FormTextField(value = developer, onValueChange = { developer = it }, label = "اسم المطور أو الشركة المطورة *")
            }
            item {
                FormTextField(value = packageName, onValueChange = { packageName = it }, label = "حزمة التطبيق (Package Name مثل com.example.app) *")
            }

            // Version, Size, Android Version
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    FormTextField(value = version, onValueChange = { version = it }, label = "الإصدار (مثل 1.2.0)", modifier = Modifier.weight(1f))
                    FormTextField(value = size, onValueChange = { size = it }, label = "الحجم (مثل 45 MB)", modifier = Modifier.weight(1f))
                }
            }
            item {
                FormTextField(value = androidVersion, onValueChange = { androidVersion = it }, label = "نظام أندرويد المطلوب (مثل Android 8.0+)")
            }
            item {
                FormTextField(value = category, onValueChange = { category = it }, label = "التصنيف (tools, social, racing, action, games...)")
            }

            // Descriptions
            item {
                FormTextField(value = shortDesc, onValueChange = { shortDesc = it }, label = "الوصف المختصر (يظهر في البطاقات)")
            }
            item {
                FormTextField(value = fullDesc, onValueChange = { fullDesc = it }, label = "الوصف الكامل والمميزات", maxLines = 5)
            }

            // Media URLs
            item {
                Text(text = "الوسائط والصور", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FormTextField(value = iconUrl, onValueChange = { iconUrl = it }, label = "رابط الأيقونة (Icon URL)")
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(value = bannerUrl, onValueChange = { bannerUrl = it }, label = "رابط البانر المميز (Banner URL)")
                Spacer(modifier = Modifier.height(8.dp))
                FormTextField(value = screenshots, onValueChange = { screenshots = it }, label = "لقطات الشاشة (روابط مفصولة بفواصل)")
            }

            // Storage & Package Upload Simulation
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "ملف الحزمة والتخزين (APK / XAPK)", color = ApexTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "يدعم نظام APEX رفع ملفات APK و XAPK مع استئناف التحميل وفحص التوافق",
                            color = ApexTextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Upload button trigger
                        Button(
                            onClick = {
                                val simulatedFileName = if (type == "GAME") "${name.ifBlank { "game" }.lowercase().replace(" ", "_")}.xapk"
                                else "${name.ifBlank { "app" }.lowercase().replace(" ", "_")}.apk"
                                val simulatedSizeBytes = if (type == "GAME") 524288000L else 47185920L

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
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "رفع ملف الحزمة سحابياً (APK / XAPK)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Upload Progress Bar
                        if (uploadState.isUploading) {
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { uploadState.progress },
                                color = ApexPrimary,
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "جاري الرفع ${(uploadState.progress * 100).toInt()}%", color = ApexPrimary, fontSize = 11.sp)
                                Text(text = uploadState.speed, color = ApexTextSecondary, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FormTextField(value = apkUrl, onValueChange = { apkUrl = it }, label = "رابط ملف APK المباشر")
                        Spacer(modifier = Modifier.height(8.dp))
                        FormTextField(value = xapkUrl, onValueChange = { xapkUrl = it }, label = "رابط ملف XAPK المباشر (اختياري للألعاب الكبيرة)")
                    }
                }
            }

            // Publish & Featured Toggles
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "نشر في المتجر مباشرة", color = ApexTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "جعله مرئياً وقابلاً للتحميل لجميع المستخدمين", color = ApexTextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = published,
                                onCheckedChange = { published = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ApexTertiary, checkedTrackColor = ApexTertiary.copy(alpha = 0.3f))
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "تمييز التطبيق (Featured)", color = ApexTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "إبرازه في الصفحة الرئيسية والبانر العلوي", color = ApexTextSecondary, fontSize = 11.sp)
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

            item {
                Spacer(modifier = Modifier.height(30.dp))
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
            .background(if (isSelected) ApexPrimary.copy(alpha = 0.2f) else ApexSurfaceCard)
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
    testTag: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        maxLines = maxLines,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ApexPrimary,
            unfocusedBorderColor = ApexBorder,
            focusedContainerColor = ApexSurfaceCard,
            unfocusedContainerColor = ApexSurfaceCard,
            focusedTextColor = ApexTextPrimary,
            unfocusedTextColor = ApexTextPrimary
        ),
        modifier = if (testTag.isNotBlank()) modifier.testTag(testTag) else modifier
    )
}
