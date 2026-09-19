package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.ApexStoreViewModel
import com.example.ui.components.AppGridCard
import com.example.ui.components.CategoryFilterChip
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@Composable
fun AppsScreen(
    viewModel: ApexStoreViewModel,
    modifier: Modifier = Modifier
) {
    val apps by viewModel.publishedApps.collectAsState()
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    val filteredApps = remember(apps, selectedCategoryId) {
        if (selectedCategoryId == null) apps
        else apps.filter { it.category == selectedCategoryId }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = "تطبيقات الأندرويد",
                color = ApexTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "اكتشف أحدث الأدوات وتطبيقات الإنتاجية والتواصل",
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
                    category = com.example.data.model.StoreCategory("all", "الكل", "All", "all", com.example.data.model.AppType.APP),
                    isSelected = selectedCategoryId == null,
                    onClick = { selectedCategoryId = null }
                )
            }
            items(CategoryData.appCategories) { cat ->
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

        // Grid of Apps
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredApps) { app ->
                AppGridCard(
                    app = app,
                    onClick = { viewModel.openAppDetail(app) },
                    onDownloadClick = { viewModel.startDownload(app) }
                )
            }
        }
    }
}
