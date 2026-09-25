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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.AppEntity
import com.example.ui.ApexStoreViewModel
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

data class UpdateAppItem(
    val id: String,
    val name: String,
    val developer: String,
    val version: String,
    val size: String,
    val iconUrl: String
)

@Composable
fun UpdatesScreen(
    viewModel: ApexStoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val publishedApps by viewModel.publishedApps.collectAsState()
    val updatedMap = remember { mutableStateMapOf<String, Boolean>() }

    // Combine published apps and popular system items (as seen in reference mockup)
    val updateList = remember(publishedApps) {
        val predefined = listOf(
            UpdateAppItem("up_yt", "YouTube", "Google LLC", "v19.16.36", "48 MB", "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=150"),
            UpdateAppItem("up_ig", "Instagram", "Meta Platforms", "v308.0.0", "55 MB", "https://images.unsplash.com/photo-1611262588024-d12430b98920?w=150"),
            UpdateAppItem("up_wa", "WhatsApp", "Meta Platforms", "v2.25.27.78", "42 MB", "https://images.unsplash.com/photo-1614680376593-902f749f7ffc?w=150"),
            UpdateAppItem("up_tt", "TikTok", "TikTok Pte. Ltd.", "v37.5.0", "78 MB", "https://images.unsplash.com/photo-1611605698335-8b1569810432?w=150"),
            UpdateAppItem("up_gc", "Google Chrome", "Google LLC", "v131.0.6778.200", "85 MB", "https://images.unsplash.com/photo-1573804633927-bfcbcd909acd?w=150")
        )
        val fromStore = publishedApps.map {
            UpdateAppItem(it.id, it.name, it.developer, "v${it.version}", it.size, it.iconUrl)
        }
        (fromStore + predefined).distinctBy { it.name }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexBackground)
    ) {
        // Top Bar
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

                Column {
                    Text(
                        text = "التحديثات",
                        color = ApexTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "التطبيقات والألعاب التي تحتاج إلى تحديث",
                        color = ApexTextSecondary,
                        fontSize = 11.5.sp
                    )
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(updateList, key = { it.id }) { item ->
                val isUpdated = updatedMap[item.id] == true

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ApexSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ApexBorder.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.iconUrl.ifBlank { "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150" })
                                .crossfade(true)
                                .build(),
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ApexSurfaceVariant)
                                .border(1.dp, ApexBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                color = ApexTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.developer,
                                color = ApexTextSecondary,
                                fontSize = 11.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${item.version} • ${item.size}",
                                color = ApexPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = {
                                updatedMap[item.id] = true
                            },
                            enabled = !isUpdated,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isUpdated) ApexTertiary.copy(alpha = 0.15f) else ApexPrimary,
                                contentColor = if (isUpdated) ApexTertiary else Color.White
                            ),
                            modifier = Modifier.height(38.dp)
                        ) {
                            if (isUpdated) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("محدّث", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Text("تحديث", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
