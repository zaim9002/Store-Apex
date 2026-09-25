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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppType
import com.example.ui.ApexStoreViewModel
import com.example.ui.StoreNavigationTab
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

data class CategoryItemUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val appType: AppType?
)

@Composable
fun CategoriesScreen(
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        CategoryItemUi("games_all", "ألعاب", "كل الألعاب", Icons.Default.SportsEsports, Color(0xFF8B5CF6), AppType.GAME),
        CategoryItemUi("apps_all", "تطبيقات", "كل التطبيقات", Icons.Default.Apps, Color(0xFF3B82F6), AppType.APP),
        CategoryItemUi("tools", "أدوات", "Tools & Utilities", Icons.Default.Build, Color(0xFF06B6D4), AppType.APP),
        CategoryItemUi("entertainment", "ترفيه", "Movies & Fun", Icons.Default.Movie, Color(0xFFEC4899), AppType.APP),
        CategoryItemUi("photography", "تصوير", "Photo & Video", Icons.Default.CameraAlt, Color(0xFF6366F1), AppType.APP),
        CategoryItemUi("social", "تواصل واجتماعي", "Chat & Friends", Icons.Default.Chat, Color(0xFF10B981), AppType.APP),
        CategoryItemUi("education", "تعليم", "Learning & Books", Icons.Default.School, Color(0xFFA855F7), AppType.APP),
        CategoryItemUi("business", "أعمال", "Office & Productivity", Icons.Default.Business, Color(0xFFF97316), AppType.APP),
        CategoryItemUi("music", "موسيقى وصوتيات", "Music & Podcasts", Icons.Default.MusicNote, Color(0xFFE11D48), AppType.APP),
        CategoryItemUi("health", "صحة ورياضة", "Health & Fitness", Icons.Default.Favorite, Color(0xFFEF4444), AppType.APP),
        CategoryItemUi("action", "أكشن وحروب", "Action Games", Icons.Default.SportsKabaddi, Color(0xFFF59E0B), AppType.GAME),
        CategoryItemUi("adventure", "مغامرات وعوالم", "Adventure & RPG", Icons.Default.Gamepad, Color(0xFF14B8A6), AppType.GAME),
        CategoryItemUi("racing", "سباق وسرعة", "Racing & Cars", Icons.Default.DirectionsCar, Color(0xFF3B82F6), AppType.GAME),
        CategoryItemUi("puzzle", "ألغاز وذكاء", "Puzzle & Mind", Icons.Default.Extension, Color(0xFF8B5CF6), AppType.GAME),
        CategoryItemUi("sports", "كرة ورياضات", "Sports Games", Icons.Default.FitnessCenter, Color(0xFF22C55E), AppType.GAME),
        CategoryItemUi("featured", "مميز ومختار", "Editor's Choice", Icons.Default.Star, Color(0xFFFBBF24), null)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Top Header
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
                        contentDescription = "رجوع",
                        tint = ApexTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "التصنيفات",
                    color = ApexTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 2-Column Grid matching Reference Image
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { cat ->
                CategoryGridCard(
                    category = cat,
                    onClick = {
                        if (cat.id == "games_all") {
                            viewModel.navigateTo(StoreNavigationTab.GAMES)
                        } else if (cat.id == "apps_all") {
                            viewModel.navigateTo(StoreNavigationTab.APPS)
                        } else {
                            viewModel.searchTypeFilter.value = cat.appType
                            viewModel.searchCategoryFilter.value = if (cat.id == "featured") null else cat.id
                            viewModel.navigateTo(StoreNavigationTab.SEARCH)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryGridCard(
    category: CategoryItemUi,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(category.iconColor.copy(alpha = 0.16f))
                    .border(1.dp, category.iconColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = category.name,
                color = ApexTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
