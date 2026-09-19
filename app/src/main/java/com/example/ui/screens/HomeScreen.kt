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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Featured Banner
        item {
            HeroBannerSection(
                featuredApp = featuredApps.firstOrNull(),
                onOpenApp = { app -> viewModel.openAppDetail(app) },
                onDownload = { app -> viewModel.startDownload(app) }
            )
        }

        // Section: Featured Apps (التطبيقات المميزة)
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
                    AppGridCard(
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onDownloadClick = { viewModel.startDownload(app) },
                        modifier = Modifier.width(170.dp)
                    )
                }
            }
        }

        // Section: Featured Games (الألعاب المميزة)
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
                    AppGridCard(
                        app = game,
                        onClick = { viewModel.openAppDetail(game) },
                        onDownloadClick = { viewModel.startDownload(game, if (game.xapkUrl.isNotBlank()) "XAPK" else "APK") },
                        modifier = Modifier.width(170.dp)
                    )
                }
            }
        }

        // Section: Most Downloaded Top Charts (الأكثر تحميلاً)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "الأكثر تحميلاً (Top Charts)",
                icon = Icons.Default.LocalFireDepartment,
                iconTint = ApexAmber
            )
        }

        itemsIndexed(mostDownloaded.take(5)) { index, app ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                AppRankedCard(
                    rank = index + 1,
                    app = app,
                    onClick = { viewModel.openAppDetail(app) },
                    onDownloadClick = { viewModel.startDownload(app) }
                )
            }
        }

        // Section: Latest Apps (آخر التطبيقات المضافة)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "آخر التطبيقات",
                actionTitle = "المزيد",
                onActionClick = { onNavigateTab(StoreNavigationTab.APPS) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(latestApps.take(6)) { app ->
                    AppGridCard(
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onDownloadClick = { viewModel.startDownload(app) },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }

        // Section: Latest Games (آخر الألعاب)
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(
                title = "آخر الألعاب",
                actionTitle = "المزيد",
                onActionClick = { onNavigateTab(StoreNavigationTab.GAMES) }
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(latestGames.take(6)) { game ->
                    AppGridCard(
                        app = game,
                        onClick = { viewModel.openAppDetail(game) },
                        onDownloadClick = { viewModel.startDownload(game) },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }

        // Section: Categories Overview
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "تصفح حسب التصنيف")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(CategoryData.getAllCategories().take(10)) { cat ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ApexSurfaceCard)
                            .border(1.dp, ApexBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.searchCategoryFilter.value = cat.id
                                onNavigateTab(StoreNavigationTab.APPS)
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
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(ApexSurfaceCard)
            .border(1.dp, ApexPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
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
                text = featuredApp?.name ?: "APEX STORE - تطبيقات وألعاب أصلية",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = featuredApp?.shortDescription ?: "تحميل مباشر لملفات APK و XAPK بسرعة فائقة وبدون إعلانات مزعجة",
                color = ApexTextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { featuredApp?.let { onDownload(it) } },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexPrimary,
                        contentColor = ApexBackground
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "تحميل مباشر", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { featuredApp?.let { onOpenApp(it) } },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexSurfaceVariant.copy(alpha = 0.8f),
                        contentColor = ApexTextPrimary
                    ),
                    modifier = Modifier.height(36.dp)
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
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = ApexPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
