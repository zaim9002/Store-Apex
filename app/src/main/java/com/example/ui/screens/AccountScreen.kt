package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserRole
import com.example.ui.ApexStoreViewModel
import com.example.ui.StoreNavigationTab
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexRed
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTertiary
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun AccountScreen(
    viewModel: ApexStoreViewModel,
    onNavigateTab: (StoreNavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentAdminProfile by viewModel.currentAdminProfile.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
            .verticalScroll(scrollState)
            .padding(bottom = 90.dp)
    ) {
        // Top Header
        Text(
            text = "الملف الشخصي والحساب",
            color = ApexTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // User Profile Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUser.avatar.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" })
                        .crossfade(true)
                        .build(),
                    contentDescription = currentUser.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, ApexPrimary, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentUser.name,
                        color = ApexTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentUser.email,
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Role Badge
                    val (roleColor, roleText) = when (currentUser.role) {
                        UserRole.SUPER_ADMIN.name -> ApexAmber to "المدير التنفيذي (Super Admin)"
                        UserRole.ADMIN.name -> ApexSecondary to "مشرف متجر (Admin)"
                        else -> ApexTertiary to "مستخدم عادي (User)"
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(roleColor.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = roleText,
                            color = roleColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Prominent Admin Dashboard Button (Only visible for Admins & Super Admins)
        if (currentUser.role != UserRole.USER.name) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ApexSecondary.copy(alpha = 0.12f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, ApexSecondary.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateTab(StoreNavigationTab.ADMIN_DASHBOARD) }
                    .testTag("account_admin_dashboard_button")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ApexSecondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "لوحة التحكم والإدارة (Admin Dashboard)",
                            color = ApexTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "إدارة التطبيقات، المشرفين، سجل النشاط، والإحصائيات",
                            color = ApexTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = ApexSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Demo Role Switcher (Allows testing all 3 roles instantly)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "تبديل الحساب التجريبي واختبار الصلاحيات",
            color = ApexTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "اختر أحد الحسابات لاختبار الصلاحيات الأمنية وقاعدة البيانات:",
            color = ApexTextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoleSwitchChip(
                title = "Super Admin",
                subtitle = "المدير العام",
                isSelected = currentUser.role == UserRole.SUPER_ADMIN.name,
                accentColor = ApexAmber,
                onClick = { viewModel.switchUserRole(UserRole.SUPER_ADMIN) },
                modifier = Modifier.weight(1f)
            )
            RoleSwitchChip(
                title = "Admin",
                subtitle = "مشرف",
                isSelected = currentUser.role == UserRole.ADMIN.name,
                accentColor = ApexSecondary,
                onClick = { viewModel.switchUserRole(UserRole.ADMIN) },
                modifier = Modifier.weight(1f)
            )
            RoleSwitchChip(
                title = "User",
                subtitle = "مستخدم",
                isSelected = currentUser.role == UserRole.USER.name,
                accentColor = ApexTertiary,
                onClick = { viewModel.switchUserRole(UserRole.USER) },
                modifier = Modifier.weight(1f)
            )
        }

        // User Navigation Actions
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "الخدمات والإعدادات",
            color = ApexTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                AccountOptionRow(
                    icon = Icons.Default.Download,
                    title = "سجل التحميلات",
                    subtitle = "عرض الملفات التي تم تحميلها",
                    onClick = { onNavigateTab(StoreNavigationTab.DOWNLOADS) }
                )
                AccountOptionRow(
                    icon = Icons.Default.Favorite,
                    title = "التطبيقات المفضلة",
                    subtitle = "قائمة التطبيقات والألعاب المحفوظة",
                    onClick = { onNavigateTab(StoreNavigationTab.FAVORITES) }
                )
                AccountOptionRow(
                    icon = Icons.Default.Security,
                    title = "الأمان وقواعد البيانات",
                    subtitle = "حماية مشددة وقواعد وصول على مستوى النظام",
                    onClick = {}
                )
                AccountOptionRow(
                    icon = Icons.Default.Info,
                    title = "حول متجر APEX STORE",
                    subtitle = "الإصدار 1.0.0 (Production Ready)",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun RoleSwitchChip(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.2f) else ApexSurfaceCard)
            .border(1.dp, if (isSelected) accentColor else ApexBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = if (isSelected) accentColor else ApexTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = subtitle,
                color = ApexTextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun AccountOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(ApexSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ApexPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = ApexTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Text(
                text = subtitle,
                color = ApexTextSecondary,
                fontSize = 11.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // In RTL arrow back points left/forward
            contentDescription = null,
            tint = ApexTextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
