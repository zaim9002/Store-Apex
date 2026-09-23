package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Games
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AppEntity
import com.example.data.model.DownloadEntity
import com.example.data.model.DownloadStatus
import com.example.data.model.StoreCategory
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.StoreNavigationTab
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

/**
 * Official APEX STORE Logo Badge
 * Features a modern, sharp geometric A-shield with electric cyan-violet gradient
 */
@Composable
fun ApexStoreLogoBadge(
    size: Int = 38,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.28).dp))
            .background(
                Brush.linearGradient(
                    listOf(ApexPrimary, ApexSecondary)
                )
            )
            .border(
                1.dp,
                ApexPrimary.copy(alpha = 0.5f),
                RoundedCornerShape((size * 0.28).dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "A",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = (size * 0.54).sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

/**
 * Top Header for APEX STORE
 * Guaranteed LTR isolation for brand name "APEX STORE" so it NEVER gets reversed or distorted in RTL Arabic layout.
 * Includes statusBarsPadding to prevent overlapping with system status bar icons (battery, clock, wifi).
 */
@Composable
fun ApexTopBar(
    currentUser: UserEntity,
    activeDownloadsCount: Int,
    onSearchClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onAccountClick: () -> Unit,
    onAdminDashboardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ApexSurface,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .border(width = 1.dp, color = ApexBorder.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Exact Name: [LOGO] APEX STORE (Guaranteed LTR order)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onAccountClick() }
                        .padding(vertical = 4.dp)
                ) {
                    ApexStoreLogoBadge(size = 36)

                    Spacer(modifier = Modifier.width(9.dp))

                    Column {
                        Text(
                            text = "APEX STORE",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 0.8.sp,
                            fontFamily = FontFamily.SansSerif,
                            maxLines = 1
                        )

                        // Role Badge if Admin or Super Admin
                        if (currentUser.role != UserRole.USER.name) {
                            val roleTitle = if (currentUser.role == UserRole.SUPER_ADMIN.name) "SUPER ADMIN" else "ADMIN"
                            val badgeColor = if (currentUser.role == UserRole.SUPER_ADMIN.name) ApexAmber else ApexSecondary
                            Text(
                                text = roleTitle,
                                color = badgeColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        } else {
                            Text(
                                text = "OFFICIAL STORE",
                                color = ApexPrimary.copy(alpha = 0.85f),
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // Action Icons Row: Proportioned, non-overlapping, crisp 20dp icons inside 38dp circular backgrounds
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Downloads Tracker Button with Active Badge
                Box(
                    modifier = Modifier
                        .testTag("top_bar_downloads_button")
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ApexSurfaceVariant)
                        .border(1.dp, ApexBorder.copy(alpha = 0.45f), CircleShape)
                        .clickable { onDownloadsClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "التحميلات",
                        tint = if (activeDownloadsCount > 0) ApexPrimary else ApexTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    if (activeDownloadsCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(2.dp)
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(ApexPrimary)
                                .border(1.5.dp, ApexSurface, CircleShape)
                        )
                    }
                }

                // Search Icon Button
                Box(
                    modifier = Modifier
                        .testTag("top_bar_search_button")
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ApexSurfaceVariant)
                        .border(1.dp, ApexBorder.copy(alpha = 0.45f), CircleShape)
                        .clickable { onSearchClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Admin Dashboard shortcut if authorized, or Profile Button
                if (currentUser.role != UserRole.USER.name) {
                    Box(
                        modifier = Modifier
                            .testTag("top_bar_admin_button")
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ApexSecondary.copy(alpha = 0.16f))
                            .border(1.dp, ApexSecondary.copy(alpha = 0.5f), CircleShape)
                            .clickable { onAdminDashboardClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "لوحة التحكم",
                            tint = ApexSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .testTag("top_bar_account_button")
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ApexPrimary.copy(alpha = 0.14f))
                            .border(1.dp, ApexPrimary.copy(alpha = 0.4f), CircleShape)
                            .clickable { onAccountClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "الحساب",
                            tint = ApexPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Redesigned Premium App Card
 * Fully meets user requirements:
 * [أيقونة التطبيق]
 * App Name
 * Developer Name
 * ⭐ 4.9 | 18 MB | APK / XAPK
 * [      تحميل ⬇      ]
 * [       التفاصيل      ]
 * Completely unclipped, responsive, and robust on all screen sizes.
 */
@Composable
fun AppGridCard(
    app: AppEntity,
    onClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    downloadState: DownloadEntity? = null
) {
    val isDownloading = downloadState?.status == DownloadStatus.DOWNLOADING.name
    val isCompleted = downloadState?.status == DownloadStatus.COMPLETED.name
    val isXapk = app.xapkUrl.isNotBlank() || app.packageName.endsWith("xapk", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.45f)),
        modifier = modifier
            .testTag("app_card_${app.id}")
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Top Row: App Icon + APK/XAPK Badge
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // App Icon
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(app.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150" })
                        .crossfade(true)
                        .build(),
                    contentDescription = app.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(ApexSurfaceVariant)
                        .border(1.dp, ApexBorder.copy(alpha = 0.3f), RoundedCornerShape(13.dp))
                )

                // Package Type Badge (APK in Emerald Green, XAPK in Electric Purple)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isXapk) ApexSecondary.copy(alpha = 0.18f)
                                else ApexTertiary.copy(alpha = 0.18f)
                            )
                            .border(
                                1.dp,
                                if (isXapk) ApexSecondary.copy(alpha = 0.6f)
                                else ApexTertiary.copy(alpha = 0.6f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (isXapk) "XAPK" else "APK",
                            color = if (isXapk) ApexSecondary else ApexTertiary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // App Name (Single line with ellipsis)
            Text(
                text = app.name,
                color = ApexTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Developer Name (Single line with ellipsis)
            Text(
                text = app.developer,
                color = ApexTextSecondary,
                fontSize = 11.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Meta Row: Rating + Size
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ApexAmber,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", app.rating),
                        color = ApexAmber,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "•",
                    color = ApexTextMuted,
                    fontSize = 11.sp
                )

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = app.size,
                        color = ApexTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Download Button (Full Width, Unclipped, Touch-Target Compliant)
            Button(
                onClick = onDownloadClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isCompleted -> ApexTertiary
                        isDownloading -> ApexPrimary.copy(alpha = 0.2f)
                        else -> ApexPrimary
                    },
                    contentColor = when {
                        isCompleted -> Color.White
                        isDownloading -> ApexPrimary
                        else -> ApexBackground
                    }
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .testTag("app_download_btn_${app.id}")
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = ApexPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "جارٍ التحميل...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                } else if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "تم التثبيت ✓",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "تحميل",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "تحميل ⬇",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Details Button (Full Width)
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ApexSurfaceVariant.copy(alpha = 0.7f),
                    contentColor = ApexTextPrimary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .testTag("app_details_btn_${app.id}")
            ) {
                Text(
                    text = "التفاصيل",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Ranked App Card (Top Charts & Search Results)
 * Clean horizontal layout with rank number, details, and prominent download button.
 */
@Composable
fun AppRankedCard(
    rank: Int,
    app: AppEntity,
    onClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier,
    downloadState: DownloadEntity? = null
) {
    val isDownloading = downloadState?.status == DownloadStatus.DOWNLOADING.name
    val isCompleted = downloadState?.status == DownloadStatus.COMPLETED.name
    val isXapk = app.xapkUrl.isNotBlank() || app.packageName.endsWith("xapk", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.35f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Rank Number
            Text(
                text = "$rank",
                color = if (rank <= 3) ApexPrimary else ApexTextMuted,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                modifier = Modifier.width(26.dp),
                textAlign = TextAlign.Center
            )

            // App Icon
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(app.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=120" })
                    .crossfade(true)
                    .build(),
                contentDescription = app.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceVariant)
                    .border(1.dp, ApexBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details Column
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = app.name,
                        color = ApexTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = if (isXapk) "XAPK" else "APK",
                            color = if (isXapk) ApexSecondary else ApexTertiary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    (if (isXapk) ApexSecondary else ApexTertiary).copy(alpha = 0.15f)
                                )
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${app.developer} • ${app.size}",
                    color = ApexTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ApexAmber,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%.1f", app.rating),
                        color = ApexAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${app.downloadCount / 1000}k تحميل",
                        color = ApexTextMuted,
                        fontSize = 10.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Action Button
            Button(
                onClick = onDownloadClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isCompleted -> ApexTertiary
                        isDownloading -> ApexPrimary.copy(alpha = 0.2f)
                        else -> ApexPrimary
                    },
                    contentColor = when {
                        isCompleted -> Color.White
                        isDownloading -> ApexPrimary
                        else -> ApexBackground
                    }
                ),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                modifier = Modifier
                    .height(36.dp)
                    .widthIn(min = 90.dp)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = ApexPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                } else {
                    Text(
                        text = if (isCompleted) "فتح ✓" else "تحميل ⬇",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Bottom Navigation Bar for APEX STORE
 * Responsive with proper content padding handling
 */
@Composable
fun ApexBottomBar(
    currentTab: StoreNavigationTab,
    onTabSelected: (StoreNavigationTab) -> Unit,
    isAdmin: Boolean,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = ApexSurface,
        contentColor = ApexTextPrimary,
        tonalElevation = 8.dp,
        modifier = modifier.border(width = 1.dp, color = ApexBorder.copy(alpha = 0.35f))
    ) {
        val items = mutableListOf(
            NavigationItemData(StoreNavigationTab.HOME, "الرئيسية", Icons.Default.Home, Icons.Outlined.Home),
            NavigationItemData(StoreNavigationTab.APPS, "تطبيقات", Icons.Default.Widgets, Icons.Outlined.Widgets),
            NavigationItemData(StoreNavigationTab.GAMES, "ألعاب", Icons.Default.Games, Icons.Outlined.Games),
            NavigationItemData(StoreNavigationTab.FAVORITES, "المفضلة", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder),
            NavigationItemData(StoreNavigationTab.ACCOUNT, "حسابي", Icons.Default.Person, Icons.Outlined.Person)
        )

        // For Admin users, replace Favorites or insert Admin Dashboard
        if (isAdmin) {
            items[3] = NavigationItemData(
                StoreNavigationTab.ADMIN_DASHBOARD,
                "لوحة الإدارة",
                Icons.Default.AdminPanelSettings,
                Icons.Default.AdminPanelSettings
            )
        }

        items.forEach { item ->
            val isSelected = currentTab == item.tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ApexPrimary,
                    selectedTextColor = ApexPrimary,
                    indicatorColor = ApexPrimary.copy(alpha = 0.16f),
                    unselectedIconColor = ApexTextMuted,
                    unselectedTextColor = ApexTextMuted
                )
            )
        }
    }
}

private data class NavigationItemData(
    val tab: StoreNavigationTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * Filter Chip for Categories
 */
@Composable
fun CategoryFilterChip(
    category: StoreCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) ApexPrimary.copy(alpha = 0.18f) else ApexSurfaceCard
    val borderColor = if (isSelected) ApexPrimary else ApexBorder.copy(alpha = 0.5f)
    val textColor = if (isSelected) ApexPrimary else ApexTextSecondary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.nameAr,
            color = textColor,
            fontSize = 12.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
