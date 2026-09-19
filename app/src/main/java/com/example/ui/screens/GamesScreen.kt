package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryData
import com.example.data.model.StoreCategory
import com.example.ui.ApexStoreViewModel
import com.example.ui.components.AppGridCard
import com.example.ui.components.CategoryFilterChip
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun GamesScreen(
    viewModel: ApexStoreViewModel,
    modifier: Modifier = Modifier
) {
    val games by viewModel.publishedGames.collectAsState()
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    val filteredGames = remember(games, selectedCategoryId) {
        if (selectedCategoryId == null) games
        else games.filter { it.category == selectedCategoryId }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "ألعاب الأندرويد",
                color = ApexTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "أقوى ألعاب الأكشن والسباقات والاستراتيجية بدعم كامل لملفات XAPK والـ OBB",
                color = ApexTextSecondary,
                fontSize = 12.sp
            )
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryFilterChip(
                    category = StoreCategory("all_games", "جميع الألعاب", "All", "all", com.example.data.model.AppType.GAME),
                    isSelected = selectedCategoryId == null,
                    onClick = { selectedCategoryId = null }
                )
            }
            items(CategoryData.gameCategories) { cat ->
                CategoryFilterChip(
                    category = cat,
                    isSelected = selectedCategoryId == cat.id,
                    onClick = {
                        selectedCategoryId = if (selectedCategoryId == cat.id) null else cat.id
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of Games
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredGames) { game ->
                AppGridCard(
                    app = game,
                    onClick = { viewModel.openAppDetail(game) },
                    onDownloadClick = {
                        viewModel.startDownload(
                            game,
                            if (game.xapkUrl.isNotBlank()) "XAPK" else "APK"
                        )
                    }
                )
            }
        }
    }
}
