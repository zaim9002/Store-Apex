package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.ApexBottomBar
import com.example.ui.components.ApexTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.AddEditAppScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AppDetailScreen
import com.example.ui.screens.AppsScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.GamesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.ApexBackground
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ApexStoreApp(
    viewModel: ApexStoreViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val isSplashActive by viewModel.isSplashActive.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedApp by viewModel.selectedApp.collectAsState()
    val editingApp by viewModel.editingApp.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val downloads by viewModel.downloads.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Listen for snackbar notifications
    LaunchedEffect(viewModel) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    if (isSplashActive) {
        SplashScreen()
        return
    }

    // Detail Screen Overlay
    if (selectedApp != null) {
        AppDetailScreen(
            app = selectedApp!!,
            viewModel = viewModel,
            onBackClick = { viewModel.closeAppDetail() }
        )
        return
    }

    // Add / Edit App Screen Overlay
    if (editingApp != null) {
        AddEditAppScreen(
            app = editingApp!!,
            viewModel = viewModel,
            onBackClick = { viewModel.closeAddEditApp() }
        )
        return
    }

    // Fullscreen Search
    if (currentTab == StoreNavigationTab.SEARCH) {
        SearchScreen(
            viewModel = viewModel,
            onBackClick = { viewModel.navigateTo(StoreNavigationTab.HOME) }
        )
        return
    }

    // Fullscreen Downloads
    if (currentTab == StoreNavigationTab.DOWNLOADS) {
        DownloadsScreen(
            viewModel = viewModel,
            onBackClick = { viewModel.navigateTo(StoreNavigationTab.HOME) }
        )
        return
    }

    // Fullscreen Admin Dashboard
    if (currentTab == StoreNavigationTab.ADMIN_DASHBOARD) {
        AdminDashboardScreen(
            viewModel = viewModel,
            onBackClick = { viewModel.navigateTo(StoreNavigationTab.HOME) }
        )
        return
    }

    // Standard Storefront Scaffold with TopBar and BottomBar
    val isAdmin = currentUser.role != UserRole.USER.name
    val activeDownloadsCount = downloads.count { it.status == com.example.data.model.DownloadStatus.DOWNLOADING.name }

    Scaffold(
        topBar = {
            ApexTopBar(
                currentUser = currentUser,
                activeDownloadsCount = activeDownloadsCount,
                onSearchClick = { viewModel.navigateTo(StoreNavigationTab.SEARCH) },
                onDownloadsClick = { viewModel.navigateTo(StoreNavigationTab.DOWNLOADS) },
                onAccountClick = { viewModel.navigateTo(StoreNavigationTab.ACCOUNT) },
                onAdminDashboardClick = { viewModel.navigateTo(StoreNavigationTab.ADMIN_DASHBOARD) }
            )
        },
        bottomBar = {
            ApexBottomBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.navigateTo(it) },
                isAdmin = isAdmin
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ApexBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ApexBackground)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "store_tab_transition"
            ) { tab ->
                when (tab) {
                    StoreNavigationTab.HOME -> HomeScreen(viewModel = viewModel, onNavigateTab = { viewModel.navigateTo(it) })
                    StoreNavigationTab.APPS -> AppsScreen(viewModel = viewModel)
                    StoreNavigationTab.GAMES -> GamesScreen(viewModel = viewModel)
                    StoreNavigationTab.FAVORITES -> FavoritesScreen(viewModel = viewModel)
                    StoreNavigationTab.ACCOUNT -> AccountScreen(viewModel = viewModel, onNavigateTab = { viewModel.navigateTo(it) })
                    else -> HomeScreen(viewModel = viewModel, onNavigateTab = { viewModel.navigateTo(it) })
                }
            }
        }
    }
}
