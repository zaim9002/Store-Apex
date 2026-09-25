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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.data.model.CategoryData
import com.example.ui.ApexStoreViewModel
import com.example.ui.StoreNavigationTab
import com.example.ui.components.AppGridCard
import com.example.ui.components.AppRankedCard
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTertiary
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun HomeScreen(
    viewModel: ApexStoreViewModel,
    onNavigateTab: (StoreNavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredApps by viewModel.featuredApps.collectAsState()
    val latestApps by viewModel.publishedApps.collectAsState()
    val latestGames by viewModel.publishedGames.collectAsState()
    val mostDownloaded by viewModel.mostDownloaded.collectAsState()
    val downloads by viewModel.downloads.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Quick Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ApexSurfaceCard)
                    .border(1.dp, ApexBorder.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .clickable { onNavigateTab(StoreNavigationTab.SEARCH) }
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = ApexPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ابحث عن التطبيقات، الألعاب، وحزم APK / XAPK...",
                        color = ApexTextMuted,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // 2. Hero Featured Banner (Highlighted item)
        item {
            val heroApp = featuredApps.firstOrNull() ?: latestApps.firstOrNull() ?: latestGames.firstOrNull()
            val heroDownload = heroApp?.let { app -> downloads.find { it.appId == app.id } }
            HeroBannerSection(
                featuredApp = heroApp,
                downloadState = heroDownload,
                onOpenApp = { app -> viewModel.openAppDetail(app) },
                onDownload = { app -> viewModel.startDownload(app) }
            )
        }

        // 2.5 Quick Category Shortcuts (matching Reference Image)
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickShortcutButton(
                    title = "تحديثات",
                    icon = Icons.Default.SystemUpdate,
                    color = Color(0xFF10B981),
                    onClick = { onNavigateTab(StoreNavigationTab.UPDATES) },
                    modifier = Modifier.weight(1f)
                )
                QuickShortcutButton(
                    title = "ألعاب",
                    icon = Icons.Default.SportsEsports,
                    color = Color(0xFF3B82F6),
                    onClick = { onNavigateTab(StoreNavigationTab.GAMES) },
                    modifier = Modifier.weight(1f)
                )
                QuickShortcutButton(
                    title = "تطبيقات",
                    icon = Icons.Default.Apps,
                    color = Color(0xFF8B5CF6),
                    onClick = { onNavigateTab(StoreNavigationTab.APPS) },
                    modifier = Modifier.weight(1f)
                )
                QuickShortcutButton(
                    title = "تصنيفات",
                    icon = Icons.Default.Category,
                    color = Color(0xFFEC4899),
                    onClick = { onNavigateTab(StoreNavigationTab.CATEGORIES) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 2.6 Section: Most Downloaded (الأكثر تحميلاً) - 4-column compact items with Install button
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "الأكثر تحميلاً",
                actionTitle = "المزيد",
                onActionClick = { onNavigateTab(StoreNavigationTab.APPS) }
            )
            val topApps = if (mostDownloaded.isNotEmpty()) mostDownloaded.take(8) else latestApps.take(8)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(topApps) { app ->
                    val downloadItem = downloads.find { it.appId == app.id }
                    CompactTopAppCard(
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onInstallClick = { viewModel.startDownload(app) },
                        downloadState = downloadItem
                    )
                }
            }
        }

        // 3. Section: Featured Apps (التطبيقات المميزة)
        item {
            SectionHeader(
                title = "التطبيقات المميزة",
                actionTitle = "عرض الكل",
                onActionClick = { onNavigateTab(StoreNavigationTab.APPS) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(featuredApps.filter { it.type == "APP" }) { app ->
                    val downloadItem = downloads.find { it.appId == app.id }
                    AppGridCard(
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onDownloadClick = { viewModel.startDownload(app) },
                        downloadState = downloadItem,
                        modifier = Modifier.width(185.dp)
                    )
                }
            }
        }

        // 4. Section: Featured Games (الألعاب المميزة)
        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(
                title = "الألعاب المميزة",
                actionTitle = "عرض الألعاب",
                onActionClick = { onNavigateTab(StoreNavigationTab.GAMES) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(featuredApps.filter { it.type == "GAME" }) { game ->
                    val downloadItem = downloads.find { it.appId == game.id }
                    AppGridCard(
                        app = game,
                        onClick = { viewModel.openAppDetail(game) },
                        onDownloadClick = { viewModel.startDownload(game, if (game.xapkUrl.isNotBlank()) "XAPK" else "APK") },
                        downloadState = downloadItem,
                        modifier = Modifier.width(185.dp)
                    )
                }
            }
        }

        // 5. Section: Latest Apps (أحدث التطبيقات المضافة)
        item {
            Spacer(modifier = Modifier.height(22.dp))
            SectionHeader(
                title = "أحدث التطبيقات",
                actionTitle = "عرض الكل",
                onActionClick = { onNavigateTab(StoreNavigationTab.APPS) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(latestApps.take(8)) { app ->
                    val downloadItem = downloads.find { it.appId == app.id }
                    AppGridCard(
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onDownloadClick = { viewModel.startDownload(app) },
                        downloadState = downloadItem,
                        modifier = Modifier.width(185.dp)
                    )
                }
            }
        }

        // 6. Section: Latest Games (أحدث الألعاب المضافة)
        item {
            Spacer(modifier = Modifier.height(22.dp))
            SectionHeader(
                title = "أحدث الألعاب",
                actionTitle = "عرض الكل",
                onActionClick = { onNavigateTab(StoreNavigationTab.GAMES) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(latestGames.take(8)) { game ->
                    val downloadItem = downloads.find { it.appId == game.id }
                    AppGridCard(
                        app = game,
                        onClick = { viewModel.openAppDetail(game) },
                        onDownloadClick = { viewModel.startDownload(game) },
                        downloadState = downloadItem,
                        modifier = Modifier.width(185.dp)
                    )
                }
            }
        }

        // 7. Section: Most Downloaded Top Charts (الأكثر تحميلاً)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "الأكثر تحميلاً (Top Charts)",
                icon = Icons.Default.LocalFireDepartment,
                iconTint = ApexAmber
            )
        }

        itemsIndexed(mostDownloaded.take(5)) { index, app ->
            val downloadItem = downloads.find { it.appId == app.id }
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                AppRankedCard(
                    rank = index + 1,
                    app = app,
                    onClick = { viewModel.openAppDetail(app) },
                    onDownloadClick = { viewModel.startDownload(app) },
                    downloadState = downloadItem
                )
            }
        }

        // 8. Section: Categories Overview
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "التصنيفات الرئيسية")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CategoryData.getAllCategories()) { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ApexSurfaceCard)
                            .border(1.dp, ApexBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.searchCategoryFilter.value = cat.id
                                onNavigateTab(if (cat.type == com.example.data.model.AppType.APP) StoreNavigationTab.APPS else StoreNavigationTab.GAMES)
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = cat.nameAr,
                            color = ApexTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroBannerSection(
    featuredApp: AppEntity?,
    downloadState: com.example.data.model.DownloadEntity? = null,
    onOpenApp: (AppEntity) -> Unit,
    onDownload: (AppEntity) -> Unit
) {
    val isDownloading = downloadState?.status == com.example.data.model.DownloadStatus.DOWNLOADING.name
    val isCompleted = downloadState?.status == com.example.data.model.DownloadStatus.COMPLETED.name
    val isXapk = featuredApp?.xapkUrl?.isNotBlank() == true || featuredApp?.packageName?.endsWith("xapk", ignoreCase = true) == true

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexPrimary.copy(alpha = 0.45f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { featuredApp?.let { onOpenApp(it) } }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background Banner Image with Blur/Gradient
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(featuredApp?.bannerUrl?.ifBlank { "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800" })
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
            )

            // Multi-stop Gradient Overlay ensuring text is perfectly readable
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                ApexBackground.copy(alpha = 0.78f),
                                ApexBackground.copy(alpha = 0.96f),
                                ApexBackground
                            )
                        )
                    )
            )

            // Content Container with App Icon, Title, Description, and Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Top Tag Row: "مميز اليوم ★" + "APK / XAPK" badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ApexPrimary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "★ مميز اليوم في المتجر",
                            color = ApexBackground,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isXapk) ApexSecondary.copy(alpha = 0.25f) else ApexTertiary.copy(alpha = 0.25f))
                                .border(1.dp, if (isXapk) ApexSecondary else ApexTertiary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isXapk) "XAPK" else "APK",
                                color = if (isXapk) ApexSecondary else ApexTertiary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // App Identity Row: Clearly visible App Icon + Title & Developer & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Distinct, clearly sized App Icon
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(featuredApp?.iconUrl?.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200" })
                            .crossfade(true)
                            .build(),
                        contentDescription = featuredApp?.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(ApexSurfaceVariant)
                            .border(1.5.dp, ApexPrimary.copy(alpha = 0.6f), RoundedCornerShape(15.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = featuredApp?.name ?: "APEX STORE - متجر التطبيقات والألعاب",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 2,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = featuredApp?.developer ?: "APEX Technologies",
                                color = ApexTextSecondary,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = ApexAmber,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = String.format("%.1f", featuredApp?.rating ?: 4.8f),
                                    color = ApexAmber,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Clear, complete short description (fully readable, not truncated abruptly)
                Text(
                    text = featuredApp?.shortDescription ?: "تحميل مباشر لملفات وتطبيقات APK و XAPK بسرعة فائقة وبأعلى مستويات الأمان والفحص التلقائي.",
                    color = ApexTextSecondary,
                    fontSize = 12.5.sp,
                    maxLines = 2,
                    lineHeight = 17.sp,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row: Download & Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { featuredApp?.let { onDownload(it) } },
                        enabled = !isDownloading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) ApexTertiary else ApexPrimary,
                            contentColor = ApexBackground
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(42.dp)
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ApexBackground,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "جاري التحميل...", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        } else if (isCompleted) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "تثبيت الحزمة ✓", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(17.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            val sizeText = featuredApp?.size?.let { " ($it)" } ?: ""
                            Text(
                                text = "تحميل مباشر ⬇$sizeText",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { featuredApp?.let { onOpenApp(it) } },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApexSurfaceVariant,
                            contentColor = ApexTextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(42.dp)
                    ) {
                        Text(text = "تفاصيل التطبيق", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconTint: Color = ApexPrimary,
    actionTitle: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = title,
                color = ApexTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (actionTitle != null && onActionClick != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onActionClick() }
            ) {
                Text(
                    text = actionTitle,
                    color = ApexPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = ApexPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickShortcutButton(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
        modifier = modifier
            .height(74.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(color.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = ApexTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun CompactTopAppCard(
    app: AppEntity,
    onClick: () -> Unit,
    onInstallClick: () -> Unit,
    downloadState: com.example.data.model.DownloadEntity? = null
) {
    val isDownloading = downloadState?.status == com.example.data.model.DownloadStatus.DOWNLOADING.name
    val isCompleted = downloadState?.status == com.example.data.model.DownloadStatus.COMPLETED.name

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .width(84.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(app.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150" })
                    .crossfade(true)
                    .build(),
                contentDescription = app.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = app.name,
                color = ApexTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = ApexAmber,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = String.format("%.1f", app.rating),
                    color = ApexTextSecondary,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onInstallClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) ApexTertiary else ApexPrimary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 1.5.dp,
                        modifier = Modifier.size(10.dp)
                    )
                } else {
                    Text(
                        text = if (isCompleted) "تم" else "تثبيت",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
