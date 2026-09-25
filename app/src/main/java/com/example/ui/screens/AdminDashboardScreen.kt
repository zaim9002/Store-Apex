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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.ActivityLogEntity
import com.example.data.model.AdminEntity
import com.example.data.model.AppEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.AdminTab
import com.example.ui.ApexStoreViewModel
import com.example.ui.theme.ApexAmber
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexRed
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTertiary
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val activeTab by viewModel.adminTab.collectAsState()
    val isSuperAdmin = currentUser.isSuperAdmin

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Header
        Surface(
            color = ApexSurface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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
                        contentDescription = "رجوع للمتجر",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "لوحة تحكم الإدارة",
                            color = ApexTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSuperAdmin) ApexAmber else ApexSecondary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isSuperAdmin) "Super Admin" else "Admin",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "إدارة تطبيقات وألعاب APEX STORE والصلاحيات",
                        color = ApexTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Tabs Row
        val tabs = mutableListOf(
            AdminTab.OVERVIEW to "نظرة عامة",
            AdminTab.APPS to "التطبيقات والألعاب",
            AdminTab.USERS to "المستخدمين"
        )
        if (isSuperAdmin) {
            tabs.add(AdminTab.ADMINS to "المشرفين")
        }
        tabs.add(AdminTab.LOGS to "سجل النشاط")
        tabs.add(AdminTab.SETTINGS to "الإعدادات")

        ScrollableTabRow(
            selectedTabIndex = tabs.indexOfFirst { it.first == activeTab }.coerceAtLeast(0),
            containerColor = ApexSurface,
            contentColor = ApexPrimary,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                val index = tabs.indexOfFirst { it.first == activeTab }.coerceAtLeast(0)
                if (index < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = ApexPrimary
                    )
                }
            }
        ) {
            tabs.forEach { (tab, title) ->
                Tab(
                    selected = activeTab == tab,
                    onClick = { viewModel.setAdminTab(tab) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Normal,
                            color = if (activeTab == tab) ApexPrimary else ApexTextSecondary
                        )
                    }
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                AdminTab.OVERVIEW -> AdminOverviewTab(viewModel)
                AdminTab.APPS, AdminTab.GAMES -> AdminAppsTab(viewModel)
                AdminTab.USERS -> AdminUsersTab(viewModel)
                AdminTab.ADMINS -> AdminAdminsTab(viewModel)
                AdminTab.LOGS -> AdminLogsTab(viewModel)
                AdminTab.SETTINGS -> AdminSettingsTab(viewModel)
            }
        }
    }
}

@Composable
private fun AdminOverviewTab(viewModel: ApexStoreViewModel) {
    val totalApps by viewModel.totalAppsCount.collectAsState()
    val appsCount by viewModel.appsOnlyCount.collectAsState()
    val gamesCount by viewModel.gamesOnlyCount.collectAsState()
    val publishedCount by viewModel.publishedCount.collectAsState()
    val draftCount by viewModel.draftCount.collectAsState()
    val totalDownloads by viewModel.totalDownloadsCount.collectAsState()
    val totalUsers by viewModel.totalUsersCount.collectAsState()
    val activeAdmins by viewModel.activeAdminsCount.collectAsState()
    val logs by viewModel.activityLogs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "الإحصائيات المباشرة (Live Analytics)",
                color = ApexTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Stats Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(title = "إجمالي التطبيقات", value = "$appsCount", accentColor = ApexPrimary, modifier = Modifier.weight(1f))
                    MetricCard(title = "إجمالي الألعاب", value = "$gamesCount", accentColor = ApexSecondary, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(title = "إجمالي التحميلات", value = "$totalDownloads", accentColor = ApexTertiary, modifier = Modifier.weight(1f))
                    MetricCard(title = "المستخدمين المسجلين", value = "$totalUsers", accentColor = ApexAmber, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    MetricCard(title = "تطبيقات منشورة", value = "$publishedCount", accentColor = ApexTertiary, modifier = Modifier.weight(1f))
                    MetricCard(title = "مسودات غير منشورة", value = "$draftCount", accentColor = ApexRed, modifier = Modifier.weight(1f))
                }
            }
        }

        // Recent Audit Log
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "آخر العمليات المسجلة (Activity Log)",
                    color = ApexTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { viewModel.setAdminTab(AdminTab.LOGS) }) {
                    Text("عرض الكل", color = ApexPrimary, fontSize = 12.sp)
                }
            }
        }

        items(logs.take(5)) { log ->
            ActivityLogCard(log = log)
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = ApexTextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = accentColor, fontSize = 22.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun AdminAppsTab(viewModel: ApexStoreViewModel) {
    val allApps by viewModel.allAppsAdmin.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "قائمة التطبيقات والألعاب (${allApps.size})",
                    color = ApexTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { viewModel.openAddApp() },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = ApexBackground),
                    modifier = Modifier.testTag("admin_add_app_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "إضافة جديد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(allApps) { app ->
            AdminAppItemCard(
                app = app,
                onEdit = { viewModel.openEditApp(app) },
                onDelete = { viewModel.deleteApp(app) },
                onTogglePublish = { viewModel.togglePublishStatus(app) },
                onPreview = { viewModel.openAppDetail(app) }
            )
        }
    }
}

@Composable
private fun AdminAppItemCard(
    app: AppEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePublish: () -> Unit,
    onPreview: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("تأكيد حذف التطبيق", color = ApexTextPrimary) },
            text = { Text("هل أنت متأكد من حذف ${app.name}؟ سيتم حذفه بالكامل من قاعدة البيانات.", color = ApexTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ApexRed)
                ) {
                    Text("حذف نهائي")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("إلغاء", color = ApexTextSecondary)
                }
            },
            containerColor = ApexSurfaceCard
        )
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(app.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=120" })
                        .crossfade(true)
                        .build(),
                    contentDescription = app.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ApexSurfaceVariant)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = app.name,
                            color = ApexTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (app.published) ApexTertiary.copy(alpha = 0.15f) else ApexRed.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (app.published) "منشور ✓" else "مسودة",
                                color = if (app.published) ApexTertiary else ApexRed,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "${app.type} • v${app.version} • ${app.size} • ${app.downloadCount} تحميل",
                        color = ApexTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onTogglePublish,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (app.published) ApexSurfaceVariant else ApexTertiary,
                        contentColor = if (app.published) ApexTextPrimary else Color.White
                    ),
                    modifier = Modifier.weight(1f).height(34.dp)
                ) {
                    Text(text = if (app.published) "إلغاء النشر" else "نشر في المتجر", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onEdit,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary.copy(alpha = 0.15f), contentColor = ApexPrimary),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "تعديل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onPreview,
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(8.dp)).background(ApexSurfaceVariant)
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "معاينة", tint = ApexTextSecondary, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(8.dp)).background(ApexRed.copy(alpha = 0.12f))
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = ApexRed, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun AdminUsersTab(viewModel: ApexStoreViewModel) {
    val users by viewModel.allUsers.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "قائمة المستخدمين المسجلين (${users.size})",
                color = ApexTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(users) { user ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.avatar.ifBlank { "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100" })
                            .crossfade(true)
                            .build(),
                        contentDescription = user.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp).clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = user.name, color = ApexTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "${user.email} • ${user.role}", color = ApexTextSecondary, fontSize = 11.sp)
                    }

                    Button(
                        onClick = { viewModel.updateUserStatus(user) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.status == "ACTIVE") ApexRed.copy(alpha = 0.15f) else ApexTertiary.copy(alpha = 0.15f),
                            contentColor = if (user.status == "ACTIVE") ApexRed else ApexTertiary
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = if (user.status == "ACTIVE") "تعطيل" else "تفعيل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAdminsTab(viewModel: ApexStoreViewModel) {
    val admins by viewModel.allAdmins.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddAdminDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { email, name, password, role, canAdd, canEdit, canDelete, canPublish, canUpload, canManageAdmins ->
                viewModel.addAdmin(
                    email = email,
                    name = name,
                    password = password,
                    role = role,
                    canAdd = canAdd,
                    canEdit = canEdit,
                    canDelete = canDelete,
                    canPublish = canPublish,
                    canUpload = canUpload,
                    canManageAdmins = canManageAdmins
                )
                showAddDialog = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "إدارة المشرفين والصلاحيات",
                        color = ApexTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "متاح حصرياً للمدير العام (Super Admin)",
                        color = ApexAmber,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color.White),
                    modifier = Modifier.testTag("super_admin_add_admin_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "إضافة مشرف", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(admins) { admin ->
            AdminEntityCard(
                admin = admin,
                onToggleStatus = { viewModel.toggleAdminStatus(admin) },
                onDelete = { viewModel.deleteAdmin(admin) },
                onUpdatePermissions = { updated -> viewModel.updateAdminPermissions(updated) }
            )
        }
    }
}

@Composable
private fun AdminEntityCard(
    admin: AdminEntity,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit,
    onUpdatePermissions: (AdminEntity) -> Unit
) {
    var expandedPermissions by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(42.dp).clip(CircleShape).background(ApexSecondary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = ApexSecondary)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = admin.name, color = ApexTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = admin.email, color = ApexTextSecondary, fontSize = 11.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (admin.status == "ACTIVE") ApexTertiary.copy(alpha = 0.15f) else ApexRed.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (admin.status == "ACTIVE") "نشط" else "معطل",
                            color = if (admin.status == "ACTIVE") ApexTertiary else ApexRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row {
                    Button(
                        onClick = onToggleStatus,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApexSurfaceVariant,
                            contentColor = if (admin.status == "ACTIVE") ApexRed else ApexTertiary
                        ),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = if (admin.status == "ACTIVE") "إيقاف" else "تفعيل", fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(ApexRed.copy(alpha = 0.15f))
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = ApexRed, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (expandedPermissions) "إخفاء جدول الصلاحيات ▲" else "تعديل جدول الصلاحيات ▼",
                color = ApexPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { expandedPermissions = !expandedPermissions }.padding(vertical = 4.dp)
            )

            if (expandedPermissions) {
                Spacer(modifier = Modifier.height(6.dp))
                Column {
                    PermissionToggleRow("إضافة تطبيقات جديدة", admin.canAddApp) { onUpdatePermissions(admin.copy(canAddApp = it)) }
                    PermissionToggleRow("تعديل بيانات التطبيقات", admin.canEditApp) { onUpdatePermissions(admin.copy(canEditApp = it)) }
                    PermissionToggleRow("حذف التطبيقات", admin.canDeleteApp) { onUpdatePermissions(admin.copy(canDeleteApp = it)) }
                    PermissionToggleRow("نشر وإلغاء نشر التطبيقات", admin.canPublish) { onUpdatePermissions(admin.copy(canPublish = it)) }
                    PermissionToggleRow("رفع ملفات APK و XAPK", admin.canUploadFiles) { onUpdatePermissions(admin.copy(canUploadFiles = it)) }
                    PermissionToggleRow("إدارة المشرفين الآخرين", admin.canManageAdmins) { onUpdatePermissions(admin.copy(canManageAdmins = it)) }
                }
            }
        }
    }
}

@Composable
private fun PermissionToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = ApexPrimary, uncheckedColor = ApexBorder)
        )
        Text(text = label, color = ApexTextPrimary, fontSize = 12.sp)
    }
}

@Composable
private fun AddAdminDialog(
    onDismiss: () -> Unit,
    onAdd: (email: String, name: String, password: String, role: String, canAdd: Boolean, canEdit: Boolean, canDelete: Boolean, canPublish: Boolean, canUpload: Boolean, canManageAdmins: Boolean) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("ApexAdmin@2026") }
    var role by remember { mutableStateOf(UserRole.ADMIN.roleKey) }
    var canAdd by remember { mutableStateOf(true) }
    var canEdit by remember { mutableStateOf(true) }
    var canDelete by remember { mutableStateOf(false) }
    var canPublish by remember { mutableStateOf(true) }
    var canUpload by remember { mutableStateOf(true) }
    var canManageAdmins by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة مشرف جديد", color = ApexTextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("البريد الإلكتروني") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_admin_email_input")
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم المشرف") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("كلمة المرور الخاصة بالمشرف") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("add_admin_password_input")
                )
                Text(text = "تحديد الصلاحيات الممنوحة:", color = ApexTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                PermissionToggleRow("إضافة تطبيقات", canAdd) { canAdd = it }
                PermissionToggleRow("تعديل تطبيقات", canEdit) { canEdit = it }
                PermissionToggleRow("حذف تطبيقات", canDelete) { canDelete = it }
                PermissionToggleRow("نشر تطبيقات", canPublish) { canPublish = it }
                PermissionToggleRow("رفع ملفات", canUpload) { canUpload = it }
                PermissionToggleRow("إدارة المشرفين", canManageAdmins) { canManageAdmins = it }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        onAdd(email, name, password, role, canAdd, canEdit, canDelete, canPublish, canUpload, canManageAdmins)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary)
            ) {
                Text("حفظ المشرف", color = ApexBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء", color = ApexTextSecondary) }
        },
        containerColor = ApexSurfaceCard
    )
}

@Composable
private fun AdminLogsTab(viewModel: ApexStoreViewModel) {
    val logs by viewModel.activityLogs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "سجل النشاط والعمليات (Audit Log)",
                color = ApexTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "توثيق مشفر لجميع العمليات: من قام بها، الوقت، ونوع التعديل",
                color = ApexTextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(logs) { log ->
            ActivityLogCard(log = log)
        }
    }
}

@Composable
private fun ActivityLogCard(log: ActivityLogEntity) {
    val formattedDate = remember(log.timestamp) {
        SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault()).format(Date(log.timestamp))
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier.size(34.dp).clip(CircleShape).background(ApexSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.History, contentDescription = null, tint = ApexPrimary, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = log.action, color = ApexPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(text = formattedDate, color = ApexTextMuted, fontSize = 10.sp)
                }

                Text(text = log.actionDetails, color = ApexTextPrimary, fontSize = 12.sp)

                Text(
                    text = "المسؤول: ${log.userName} (${log.userEmail})",
                    color = ApexTextSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun AdminSettingsTab(viewModel: ApexStoreViewModel) {
    val settings by viewModel.storeSettings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var storeName by remember(settings) { mutableStateOf(settings.storeName) }
    var allowRegistration by remember(settings) { mutableStateOf(settings.allowPublicRegistration) }
    var requireApproval by remember(settings) { mutableStateOf(settings.requireAdminApprovalForApps) }
    var showResetDialog by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { if (!isResetting) showResetDialog = false },
            title = {
                Text(
                    text = "تفريغ المحتوى والبدء من جديد",
                    color = ApexRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "هل أنت متأكد من حذف جميع المنشورات/التطبيقات والمشرفين الوهميين من قاعدة البيانات؟\n\nسيتم الاحتفاظ فقط بحسابات الإدارة المعتمدة (المدير العام: zaim9002@gmail.com والمشرفين: robew56802@vendprop.com و gjhh611@gmail.com) وسيبدأ المتجر بصفحة فارغة ونظيفة وجاهزة للمحتوى الحقيقي.",
                    color = ApexTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isResetting = true
                        viewModel.resetStoreToCleanStart { success, _ ->
                            isResetting = false
                            showResetDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ApexRed, contentColor = Color.White),
                    enabled = !isResetting
                ) {
                    if (isResetting) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("جارٍ التنظيف...")
                    } else {
                        Text("نعم، تفريغ وبدء من جديد")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false },
                    enabled = !isResetting
                ) {
                    Text("إلغاء", color = ApexTextSecondary)
                }
            },
            containerColor = ApexSurfaceCard
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(text = "إعدادات المتجر العامة", color = ApexTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = storeName,
            onValueChange = { storeName = it },
            label = { Text("اسم المتجر") },
            modifier = Modifier.fillMaxWidth()
        )

        PermissionToggleRow("السماح للمستخدمين الجدد بالتسجيل", allowRegistration) { allowRegistration = it }
        PermissionToggleRow("فرض مراجعة المشرفين للتطبيقات قبل النشر", requireApproval) { requireApproval = it }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                viewModel.updateSettings(
                    settings.copy(
                        storeName = storeName,
                        allowPublicRegistration = allowRegistration,
                        requireAdminApprovalForApps = requireApproval
                    )
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = ApexBackground),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            Text("حفظ الإعدادات", fontWeight = FontWeight.Bold)
        }

        if (currentUser.isSuperAdmin) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ApexRed.copy(alpha = 0.08f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, ApexRed.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = ApexRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "منطقة التصفير والبدء من جديد (Clean Start)",
                            color = ApexRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "لحذف كل التطبيقات الوهمية، والمنشورات التجريبية، والمشرفين غير المصرح لهم والبدء من جديد بمتجر نظيف وخالٍ من المحتوى الزائف.",
                        color = ApexTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showResetDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ApexRed, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth().height(40.dp).testTag("reset_store_clean_button")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تفريغ المحتوى الوهمي والبدء من جديد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
