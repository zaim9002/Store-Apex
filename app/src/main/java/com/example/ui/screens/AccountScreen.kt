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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
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
    val scrollState = rememberScrollState()

    val isGuest = currentUser.email == "guest@apexstore.com" || currentUser.id == "guest_user"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
            .verticalScroll(scrollState)
            .imePadding()
            .padding(bottom = 96.dp)
    ) {
        // Top Header matching Reference Image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "حسابي",
                color = ApexTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Profile Avatar & Information (matching Reference Image profile style)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(ApexSurfaceVariant)
                    .border(2.5.dp, ApexPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentUser.avatar.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" })
                        .crossfade(true)
                        .build(),
                    contentDescription = currentUser.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentUser.name,
                color = ApexTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = currentUser.email,
                color = ApexTextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Role Badge
            val (roleColor, roleText) = when {
                currentUser.isSuperAdmin -> ApexAmber to "المدير العام (Super Admin)"
                currentUser.isAdmin -> ApexSecondary to "مشرف متجر (Admin)"
                currentUser.isModerator -> ApexTertiary to "مشرف محتوى (Moderator)"
                else -> ApexPrimary to "مستخدم عادي (User)"
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(roleColor.copy(alpha = 0.15f))
                    .border(1.dp, roleColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = roleText,
                    color = roleColor,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // If Guest: Show Authentication Card (Login & Register)
        if (isGuest) {
            UserAuthCard(viewModel = viewModel)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // If Admin or Super Admin: Prominent Admin Dashboard Entry
        if (currentUser.canAccessAdminPanel) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ApexSecondary.copy(alpha = 0.14f)),
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
                            fontSize = 11.5.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // in RTL ArrowBack points Left
                        contentDescription = null,
                        tint = ApexSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Services & Menu List matching the Reference Design
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                AccountOptionRow(
                    icon = Icons.Default.Person,
                    title = "الملف الشخصي",
                    subtitle = if (isGuest) "تسجيل الدخول أو إنشاء حساب جديد" else currentUser.email,
                    onClick = {
                        if (!isGuest) {
                            // Already logged in
                        }
                    }
                )
                AccountOptionRow(
                    icon = Icons.Default.Category,
                    title = "التصنيفات",
                    subtitle = "استكشاف التطبيقات والألعاب حسب الفئة",
                    onClick = { onNavigateTab(StoreNavigationTab.CATEGORIES) }
                )
                AccountOptionRow(
                    icon = Icons.Default.Favorite,
                    title = "المفضلة",
                    subtitle = "قائمة التطبيقات والألعاب المحفوظة",
                    onClick = { onNavigateTab(StoreNavigationTab.FAVORITES) }
                )
                AccountOptionRow(
                    icon = Icons.Default.Download,
                    title = "سجل التحميلات",
                    subtitle = "عرض وتثبيت الحزم التي تم تحميلها للجهاز",
                    onClick = { onNavigateTab(StoreNavigationTab.DOWNLOADS) }
                )
                AccountOptionRow(
                    icon = Icons.Default.Settings,
                    title = "الإعدادات",
                    subtitle = "اللغة، التحديثات التلقائية، والتخزين",
                    onClick = {}
                )
                AccountOptionRow(
                    icon = Icons.Default.HelpOutline,
                    title = "المساعدة والدعم",
                    subtitle = "الأسئلة الشائعة والتواصل مع الدعم الفني",
                    onClick = {}
                )
                AccountOptionRow(
                    icon = Icons.Default.Info,
                    title = "حول التطبيق",
                    subtitle = "متجر APEX STORE الإصدار 1.0.0 (الرسمي)",
                    onClick = {}
                )
                if (isGuest) {
                    AccountOptionRow(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "تسجيل دخول الإدارة (Admin Login)",
                        subtitle = "الدخول الآمن للمشرفين والمدير العام",
                        onClick = { onNavigateTab(StoreNavigationTab.ADMIN_LOGIN) }
                    )
                }
            }
        }

        // Logout Action Button (Matching Reference Image in elegant red)
        if (!isGuest) {
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable { viewModel.logout() }
                    .padding(vertical = 12.dp)
                    .testTag("account_logout_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "تسجيل الخروج",
                    color = ApexRed,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun UserAuthCard(viewModel: ApexStoreViewModel) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Mode Selector Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { isRegisterMode = false; authError = null },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isRegisterMode) ApexPrimary else ApexSurfaceVariant,
                        contentColor = if (!isRegisterMode) Color.White else ApexTextSecondary
                    ),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Text(text = "تسجيل الدخول", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isRegisterMode = true; authError = null },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRegisterMode) ApexPrimary else ApexSurfaceVariant,
                        contentColor = if (isRegisterMode) Color.White else ApexTextSecondary
                    ),
                    modifier = Modifier.weight(1f).height(42.dp)
                ) {
                    Text(text = "حساب جديد", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isRegisterMode) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; authError = null },
                    label = { Text("الاسم الكامل", fontSize = 12.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ApexPrimary,
                        unfocusedBorderColor = ApexBorder,
                        focusedContainerColor = ApexSurfaceVariant,
                        unfocusedContainerColor = ApexSurfaceVariant,
                        focusedTextColor = ApexTextPrimary,
                        unfocusedTextColor = ApexTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("user_register_name_input")
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; authError = null },
                    label = { Text("البريد الإلكتروني", fontSize = 12.sp) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ApexPrimary,
                        unfocusedBorderColor = ApexBorder,
                        focusedContainerColor = ApexSurfaceVariant,
                        unfocusedContainerColor = ApexSurfaceVariant,
                        focusedTextColor = ApexTextPrimary,
                        unfocusedTextColor = ApexTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("user_auth_email_input")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; authError = null },
                    label = { Text("كلمة المرور", fontSize = 12.sp) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = ApexTextMuted
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ApexPrimary,
                        unfocusedBorderColor = ApexBorder,
                        focusedContainerColor = ApexSurfaceVariant,
                        unfocusedContainerColor = ApexSurfaceVariant,
                        focusedTextColor = ApexTextPrimary,
                        unfocusedTextColor = ApexTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("user_auth_password_input")
                )
            }

            val err = authError
            if (err != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ApexRed.copy(alpha = 0.12f))
                        .border(1.dp, ApexRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(text = err, color = ApexRed, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        authError = "يرجى كتابة البريد الإلكتروني"
                        return@Button
                    }
                    if (password.isBlank()) {
                        authError = "يرجى كتابة كلمة المرور"
                        return@Button
                    }
                    if (password.length < 6) {
                        authError = "كلمة المرور يجب أن تكون 6 أحرف على الأقل"
                        return@Button
                    }
                    focusManager.clearFocus()
                    isLoading = true
                    authError = null

                    if (isRegisterMode) {
                        viewModel.registerUser(name, email, password) { success, msg ->
                            isLoading = false
                            if (!success) authError = msg ?: "تعذر إنشاء الحساب، يرجى المحاولة لاحقاً"
                        }
                    } else {
                        viewModel.loginUser(email, password) { success, msg ->
                            isLoading = false
                            if (!success) authError = msg ?: "تعذر تسجيل الدخول، يرجى التحقق من البيانات"
                        }
                    }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ApexPrimary, contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("user_auth_submit_button")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isRegisterMode) "جارٍ إنشاء الحساب..." else "جارٍ تسجيل الدخول...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = if (isRegisterMode) "إنشاء الحساب الآن" else "تسجيل الدخول",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ApexSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ApexPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = ApexTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.5.sp
            )
            Text(
                text = subtitle,
                color = ApexTextSecondary,
                fontSize = 11.5.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // In RTL arrow back points left
            contentDescription = null,
            tint = ApexTextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
