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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AppEntity
import com.example.data.model.DownloadStatus
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    app: AppEntity,
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()
    val downloads by viewModel.downloads.collectAsState()
    val isFavorite = favorites.any { it.id == app.id }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    val downloadItem = downloads.find { it.appId == app.id }
    val isDownloading = downloadItem?.status == DownloadStatus.DOWNLOADING.name
    val isCompleted = downloadItem?.status == DownloadStatus.COMPLETED.name

    val screenshotList = remember(app.screenshots) {
        if (app.screenshots.isBlank()) {
            listOf(
                "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600",
                "https://images.unsplash.com/photo-1511512578047-dfb367046420?w=600",
                "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=600"
            )
        } else {
            app.screenshots.split(",").map { it.trim() }.filter { it.isNotBlank() }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(app.name, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = ApexTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(app) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "المفضلة",
                            tint = if (isFavorite) Color.Red else ApexTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ApexSurface,
                    titleContentColor = ApexTextPrimary
                )
            )
        },
        bottomBar = {
            // Persistent Bottom Download Bar
            Surface(
                color = ApexSurface,
                tonalElevation = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = ApexBorder.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // APK Download Button
                    if (app.apkUrl.isNotBlank() || app.xapkUrl.isBlank()) {
                        Button(
                            onClick = { viewModel.startDownload(app, "APK") },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) ApexTertiary else ApexPrimary,
                                contentColor = ApexBackground
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("detail_download_apk_button")
                        ) {
                            if (isDownloading) {
                                CircularProgressIndicator(strokeWidth = 2.5.dp, color = ApexBackground, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "جارٍ التحميل...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            } else if (isCompleted) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "تم التثبيت ✓", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            } else {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "تحميل APK ⬇", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                    Text(text = app.size, fontSize = 10.sp, color = ApexBackground.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }

                    // XAPK Download Button (if available)
                    if (app.xapkUrl.isNotBlank()) {
                        Button(
                            onClick = { viewModel.startDownload(app, "XAPK") },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ApexSecondary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("detail_download_xapk_button")
                        ) {
                            if (isDownloading) {
                                CircularProgressIndicator(strokeWidth = 2.5.dp, color = Color.White, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "جارٍ التحميل...", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            } else {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "تحميل XAPK ⬇", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                    Text(text = app.size, fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f))
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = ApexBackground,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header Info Card: Icon, Name, Developer, Category, Badges
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(app.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200" })
                            .crossfade(true)
                            .build(),
                        contentDescription = app.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, ApexBorder, RoundedCornerShape(18.dp))
                            .background(ApexSurfaceVariant)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = app.name,
                            color = ApexTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = app.developer,
                            color = ApexPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ApexSurfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (app.type == "GAME") "لعبة" else "تطبيق",
                                    color = ApexTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ApexTertiary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "آمن ومفحوص ✓",
                                    color = ApexTertiary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Specs Bar (Rating, Downloads, Size, Age Rating)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ApexSurfaceCard)
                        .border(1.dp, ApexBorder.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SpecColumnItem(
                        label = "التقييم",
                        value = "${String.format("%.1f", app.rating)} ★",
                        valueColor = ApexAmber
                    )
                    SpecDivider()
                    SpecColumnItem(
                        label = "التحميلات",
                        value = "${app.downloadCount / 1000}k+",
                        valueColor = ApexPrimary
                    )
                    SpecDivider()
                    SpecColumnItem(
                        label = "الحجم",
                        value = app.size,
                        valueColor = ApexTextPrimary
                    )
                    SpecDivider()
                    SpecColumnItem(
                        label = "الفئة العمرية",
                        value = app.ageRating,
                        valueColor = ApexTextSecondary
                    )
                }
            }

            // Screenshots Gallery
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "لقطات الشاشة والمعاينة",
                    color = ApexTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(screenshotList) { url ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                            contentDescription = "معاينة",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(190.dp)
                                .height(115.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, ApexBorder.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .background(ApexSurfaceVariant)
                        )
                    }
                }
            }

            // About this App / Description
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "عن هذا التطبيق",
                        color = ApexTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isDescriptionExpanded) app.description else app.shortDescription,
                        color = ApexTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = if (isDescriptionExpanded) "عرض أقل ▲" else "قراءة المزيد ▼",
                        color = ApexPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            // Technical Specifications Table with LTR protection for tech strings
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "المعلومات التقنية ومتطلبات التشغيل",
                            color = ApexTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        DetailRow(label = "الإصدار الحالي", value = app.version, isLtr = true)
                        DetailRow(label = "حزمة التطبيق (Package Name)", value = app.packageName, isLtr = true)
                        DetailRow(label = "إصدار أندرويد المطلوب", value = app.androidVersion, isLtr = true)
                        DetailRow(label = "المطور", value = app.developer)
                        DetailRow(label = "نوع الملف المتوفر", value = if (app.xapkUrl.isNotBlank()) "APK + XAPK (مع OBB)" else "APK قياسي")
                        DetailRow(label = "التصنيف", value = app.category)
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecColumnItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = ApexTextMuted, fontSize = 10.sp)
    }
}

@Composable
private fun SpecDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(26.dp)
            .background(ApexBorder.copy(alpha = 0.5f))
    )
}

@Composable
private fun DetailRow(label: String, value: String, isLtr: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = ApexTextMuted, fontSize = 12.sp)
        if (isLtr) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Text(
                    text = value,
                    color = ApexTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        } else {
            Text(
                text = value,
                color = ApexTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
