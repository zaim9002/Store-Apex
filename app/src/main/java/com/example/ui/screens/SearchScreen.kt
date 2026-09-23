package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppType
import com.example.data.model.CategoryData
import com.example.ui.ApexStoreViewModel
import com.example.ui.SortOrder
import com.example.ui.components.AppRankedCard
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexBorder
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexSurfaceCard
import com.example.ui.theme.ApexSurfaceVariant
import com.example.ui.theme.ApexTextMuted
import com.example.ui.theme.ApexTextPrimary
import com.example.ui.theme.ApexTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val typeFilter by viewModel.searchTypeFilter.collectAsState()
    val categoryFilter by viewModel.searchCategoryFilter.collectAsState()
    val sortOrder by viewModel.searchSortOrder.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val downloads by viewModel.downloads.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
            .statusBarsPadding()
    ) {
        // Search Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
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

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text("ابحث عن تطبيق، لعبة، مطور، أو package...", fontSize = 12.sp, color = ApexTextMuted) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ApexPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "مسح",
                                tint = ApexTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ApexPrimary,
                    unfocusedBorderColor = ApexBorder,
                    focusedContainerColor = ApexSurfaceCard,
                    unfocusedContainerColor = ApexSurfaceCard,
                    focusedTextColor = ApexTextPrimary,
                    unfocusedTextColor = ApexTextPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("search_input_field")
            )
        }

        // Filters: Type (All, Apps, Games)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterPill(
                    label = "الكل",
                    isSelected = typeFilter == null,
                    onClick = { viewModel.searchTypeFilter.value = null }
                )
            }
            item {
                FilterPill(
                    label = "تطبيقات فقط",
                    isSelected = typeFilter == AppType.APP,
                    onClick = { viewModel.searchTypeFilter.value = AppType.APP }
                )
            }
            item {
                FilterPill(
                    label = "ألعاب فقط",
                    isSelected = typeFilter == AppType.GAME,
                    onClick = { viewModel.searchTypeFilter.value = AppType.GAME }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sort Order Pills (الأحدث، الأكثر تحميلاً، الأعلى تقييماً)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterPill(
                    label = "الأحدث",
                    isSelected = sortOrder == SortOrder.NEWEST,
                    onClick = { viewModel.searchSortOrder.value = SortOrder.NEWEST }
                )
            }
            item {
                FilterPill(
                    label = "الأكثر تحميلاً",
                    isSelected = sortOrder == SortOrder.MOST_DOWNLOADED,
                    onClick = { viewModel.searchSortOrder.value = SortOrder.MOST_DOWNLOADED }
                )
            }
            item {
                FilterPill(
                    label = "الأعلى تقييماً",
                    isSelected = sortOrder == SortOrder.HIGHEST_RATED,
                    onClick = { viewModel.searchSortOrder.value = SortOrder.HIGHEST_RATED }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "نتائج البحث (${results.size})",
                color = ApexTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Results List
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ApexTextMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "لا توجد نتائج مطابقة لبحثك",
                        color = ApexTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "جرب البحث بكلمات أخرى أو اختر تصنيفاً مختلفاً",
                        color = ApexTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 95.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(results, key = { it.id }) { app ->
                    val downloadItem = downloads.find { it.appId == app.id }
                    AppRankedCard(
                        rank = results.indexOf(app) + 1,
                        app = app,
                        onClick = { viewModel.openAppDetail(app) },
                        onDownloadClick = { viewModel.startDownload(app) },
                        downloadState = downloadItem
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) ApexPrimary.copy(alpha = 0.2f) else ApexSurfaceVariant)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) ApexPrimary else ApexTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
