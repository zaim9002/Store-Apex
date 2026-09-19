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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 2. Hero Featured Banner (Highlighted item)
        item {
            HeroBannerSection(
                featuredApp = featuredApps.firstOrNull(),
                onOpenApp = { app -> viewModel.openAppDetail(app) },
                onDownload = { app -> viewModel.startDownload(app) }
            )
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
    onOpenApp: (AppEntity) -> Unit,
    onDownload: (AppEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(210.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexPrimary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .clickable { featuredApp?.let { onOpenApp(it) } }
    ) {
        // Banner Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(featuredApp?.bannerUrl?.ifBlank { "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800" })
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            ApexBackground.copy(alpha = 0.85f),
                            ApexBackground.copy(alpha = 0.98f)
                        ),
                        startY = 50f
                    )
                )
        )

        // Text & Action overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ApexPrimary)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "مميز اليوم ★",
                    color = ApexBackground,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = featuredApp?.name ?: "APEX STORE - متجر التطبيقات والألعاب",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = featuredApp?.shortDescription ?: "تحميل مباشر لملفات APK و XAPK بسرعة فائقة وبأعلى مستويات الأمان",
                color = ApexTextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { featuredApp?.let { onDownload(it) } },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexPrimary,
                        contentColor = ApexBackground
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "تحميل مباشر ⬇", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { featuredApp?.let { onOpenApp(it) } },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexSurfaceVariant.copy(alpha = 0.85f),
                        contentColor = ApexTextPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(text = "التفاصيل", fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
