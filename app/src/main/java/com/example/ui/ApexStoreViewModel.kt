package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InitialData
import com.example.data.model.ActivityLogEntity
import com.example.data.model.AdminEntity
import com.example.data.model.AppEntity
import com.example.data.model.AppType
import com.example.data.model.DownloadEntity
import com.example.data.model.StoreSettings
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.ApexStoreRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class StoreNavigationTab {
    HOME,
    GAMES,
    APPS,
    UPDATES,
    CATEGORIES,
    FAVORITES,
    DOWNLOADS,
    ACCOUNT,
    SEARCH,
    ADMIN_LOGIN,
    ADMIN_DASHBOARD
}

enum class AdminTab {
    OVERVIEW,
    APPS,
    GAMES,
    USERS,
    ADMINS,
    LOGS,
    SETTINGS
}

enum class SortOrder {
    NEWEST,
    MOST_DOWNLOADED,
    HIGHEST_RATED
}

class ApexStoreViewModel(application: Application) : AndroidViewModel(application) {
    val repository = ApexStoreRepository(application.applicationContext)

    // Current Navigation Screen / Tab
    private val _currentTab = MutableStateFlow(StoreNavigationTab.HOME)
    val currentTab: StateFlow<StoreNavigationTab> = _currentTab.asStateFlow()

    // Splash Screen State
    private val _isSplashActive = MutableStateFlow(true)
    val isSplashActive: StateFlow<Boolean> = _isSplashActive.asStateFlow()

    // Selected App for Detail Screen (null means not in detail screen)
    private val _selectedApp = MutableStateFlow<AppEntity?>(null)
    val selectedApp: StateFlow<AppEntity?> = _selectedApp.asStateFlow()

    // Add / Edit App Screen State (null means not on Add/Edit screen)
    private val _editingApp = MutableStateFlow<AppEntity?>(null)
    val editingApp: StateFlow<AppEntity?> = _editingApp.asStateFlow()

    // Admin Dashboard Active Tab
    private val _adminTab = MutableStateFlow(AdminTab.OVERVIEW)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    // Messages & Alerts
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    // User Session
    val currentUser = repository.currentUser
    val currentAdminProfile = repository.currentAdminProfile
    val storeSettings = repository.storeSettings

    // Store Catalog Streams
    val publishedApps = repository.getPublishedApps().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val publishedGames = repository.getPublishedGames().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val featuredApps = repository.getFeaturedApps().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val mostDownloaded = repository.getMostDownloaded().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favorites = repository.getFavorites().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val downloads = repository.getAllDownloads().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Admin-only Streams
    val allAppsAdmin = repository.getAllAppsAdmin().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allUsers = repository.getAllUsers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allAdmins = repository.getAllAdmins().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val activityLogs = repository.getRecentLogs().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Stats
    val totalAppsCount = repository.getTotalAppsCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val appsOnlyCount = repository.getAppsOnlyCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val gamesOnlyCount = repository.getGamesOnlyCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val publishedCount = repository.getPublishedCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val draftCount = repository.getDraftCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val totalDownloadsCount = repository.getTotalDownloadsCount().stateIn(viewModelScope, SharingStarted.Lazily, 0L)
    val totalUsersCount = repository.getTotalUsersCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)
    val activeAdminsCount = repository.getActiveAdminsCount().stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Upload state
    val uploadState = repository.uploadState

    // Search & Filter State
    val searchQuery = MutableStateFlow("")
    val searchTypeFilter = MutableStateFlow<AppType?>(null)
    val searchCategoryFilter = MutableStateFlow<String?>(null)
    val searchSortOrder = MutableStateFlow(SortOrder.NEWEST)

    private data class SearchFilters(
        val query: String,
        val type: AppType?,
        val category: String?,
        val sort: SortOrder
    )

    private val searchFilters = combine(
        searchQuery,
        searchTypeFilter,
        searchCategoryFilter,
        searchSortOrder
    ) { query, type, category, sort ->
        SearchFilters(query, type, category, sort)
    }

    val searchResults: StateFlow<List<AppEntity>> = combine(
        searchFilters,
        publishedApps,
        publishedGames
    ) { filters, apps, games ->
        val all = apps + games
        val filtered = all.filter { app ->
            val matchesQuery = filters.query.isBlank() ||
                app.name.contains(filters.query, ignoreCase = true) ||
                app.developer.contains(filters.query, ignoreCase = true) ||
                app.packageName.contains(filters.query, ignoreCase = true) ||
                app.category.contains(filters.query, ignoreCase = true)
            val matchesType = filters.type == null || app.type == filters.type.name
            val matchesCat = filters.category == null || app.category == filters.category
            matchesQuery && matchesType && matchesCat
        }

        when (filters.sort) {
            SortOrder.NEWEST -> filtered.sortedByDescending { it.createdAt }
            SortOrder.MOST_DOWNLOADED -> filtered.sortedByDescending { it.downloadCount }
            SortOrder.HIGHEST_RATED -> filtered.sortedByDescending { it.rating }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Backstack for Android back button navigation
    private val tabBackStack = mutableListOf<StoreNavigationTab>()

    init {
        // Dismiss splash after brief display
        viewModelScope.launch {
            kotlinx.coroutines.delay(1200)
            _isSplashActive.value = false
        }
    }

    fun navigateTo(tab: StoreNavigationTab) {
        if (_currentTab.value != tab) {
            tabBackStack.add(_currentTab.value)
            if (tabBackStack.size > 30) {
                tabBackStack.removeAt(0)
            }
        }
        _currentTab.value = tab
        _selectedApp.value = null
        _editingApp.value = null
    }

    fun openAppDetail(app: AppEntity) {
        _selectedApp.value = app
    }

    fun closeAppDetail() {
        _selectedApp.value = null
    }

    fun openAddApp() {
        _editingApp.value = AppEntity(
            id = UUID.randomUUID().toString(),
            name = "",
            type = AppType.APP.name,
            developer = "",
            shortDescription = "",
            description = "",
            version = "1.0.0",
            size = "25 MB",
            packageName = "com.developer.app",
            androidVersion = "Android 8.0+",
            category = "tools",
            published = true, // Default to true so newly added apps are immediately visible
            createdBy = currentUser.value.email
        )
    }

    fun openEditApp(app: AppEntity) {
        _editingApp.value = app
    }

    fun closeAddEditApp() {
        _editingApp.value = null
        repository.resetUploadState()
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    /**
     * Handles Android back button & in-app back clicks:
     * 1. Closes Add/Edit screen if open
     * 2. Closes App Details screen if open
     * 3. Returns to Admin Overview if in a sub-tab of Admin Dashboard
     * 4. Pops the previous tab from history stack
     * 5. Returns to HOME before allowing app exit
     */
    fun handleBackPress(): Boolean {
        if (_editingApp.value != null) {
            closeAddEditApp()
            return true
        }
        if (_selectedApp.value != null) {
            closeAppDetail()
            return true
        }
        if (_currentTab.value == StoreNavigationTab.ADMIN_DASHBOARD && _adminTab.value != AdminTab.OVERVIEW) {
            _adminTab.value = AdminTab.OVERVIEW
            return true
        }
        if (tabBackStack.isNotEmpty()) {
            val previousTab = tabBackStack.removeAt(tabBackStack.lastIndex)
            _currentTab.value = previousTab
            return true
        }
        if (_currentTab.value != StoreNavigationTab.HOME) {
            _currentTab.value = StoreNavigationTab.HOME
            return true
        }
        return false
    }

    fun registerUser(name: String, email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.registerUser(name, email, pass)
            res.onSuccess { user ->
                _snackbarEvent.emit("مرحباً بك ${user.name}، تم إنشاء حسابك بنجاح")
                onResult(true, null)
            }.onFailure { err ->
                onResult(false, err.localizedMessage)
            }
        }
    }

    fun loginUser(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.loginUser(email, pass)
            res.onSuccess { user ->
                _snackbarEvent.emit("أهلاً بك مجدداً ${user.name}")
                onResult(true, null)
            }.onFailure { err ->
                onResult(false, err.localizedMessage)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _currentTab.value = StoreNavigationTab.HOME
            _snackbarEvent.emit("تم تسجيل الخروج بنجاح")
        }
    }

    fun loginAdminWithCredentials(
        email: String,
        passwordAttempt: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.loginAdminWithCredentials(email, passwordAttempt)
            result.onSuccess { user ->
                _snackbarEvent.emit("مرحباً بك مجدداً ${user.name} في لوحة الإدارة")
                onResult(true, null)
            }.onFailure { error ->
                onResult(false, error.localizedMessage ?: "فشل تسجيل الدخول")
            }
        }
    }

    fun savePackageFile(appId: String, uri: android.net.Uri, isXapk: Boolean, onDone: (java.io.File, Long) -> Unit) {
        viewModelScope.launch {
            try {
                val pair = repository.savePackageFromUri(appId, uri, isXapk)
                onDone(pair.first, pair.second)
                _snackbarEvent.emit("تم حفظ حزمة التطبيق محلياً في وحدة التخزين الآمنة")
            } catch (e: Exception) {
                _snackbarEvent.emit("تعذر حفظ الحزمة: ${e.localizedMessage}")
            }
        }
    }

    fun saveIconFile(appId: String, uri: android.net.Uri, onDone: (java.io.File) -> Unit) {
        viewModelScope.launch {
            try {
                val file = repository.saveIconFromUri(appId, uri)
                onDone(file)
                _snackbarEvent.emit("تم حفظ أيقونة التطبيق في التخزين")
            } catch (e: Exception) {
                _snackbarEvent.emit("تعذر حفظ الأيقونة: ${e.localizedMessage}")
            }
        }
    }

    fun saveScreenshotFile(appId: String, uri: android.net.Uri, index: Int, onDone: (java.io.File) -> Unit) {
        viewModelScope.launch {
            try {
                val file = repository.saveScreenshotFromUri(appId, uri, index)
                onDone(file)
                _snackbarEvent.emit("تم حفظ لقطة الشاشة في التخزين")
            } catch (e: Exception) {
                _snackbarEvent.emit("تعذر حفظ لقطة الشاشة: ${e.localizedMessage}")
            }
        }
    }

    fun uploadPackageToFirebase(
        appId: String,
        fileName: String,
        fileUri: android.net.Uri,
        isXapk: Boolean,
        fileSizeBytes: Long,
        onDone: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val url = repository.uploadPackageToFirebase(appId, fileName, fileUri, isXapk, fileSizeBytes) {}
                onDone(url)
                _snackbarEvent.emit("تم رفع حزمة التطبيق بنجاح إلى Firebase Storage")
            } catch (e: Exception) {
                _snackbarEvent.emit("تنبيه الرفع: ${e.localizedMessage}")
            }
        }
    }

    fun uploadImageToFirebase(
        appId: String,
        category: String,
        uri: android.net.Uri,
        onDone: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val fileName = "${category}_${System.currentTimeMillis()}.png"
                val url = repository.uploadImageToFirebase(appId, category, fileName, uri)
                onDone(url)
            } catch (e: Exception) {
                onDone(uri.toString())
            }
        }
    }

    fun startDownload(app: AppEntity, fileType: String = "APK") {
        viewModelScope.launch {
            try {
                repository.startDownload(app, fileType)
                _snackbarEvent.emit("بدأ تحميل $fileType لتطبيق ${app.name}...")
            } catch (e: Exception) {
                _snackbarEvent.emit("تعذر بدء التحميل: ${e.localizedMessage}")
            }
        }
    }

    fun retryDownload(download: DownloadEntity) {
        viewModelScope.launch {
            try {
                repository.retryDownload(download)
                _snackbarEvent.emit("إعادة محاولة تحميل ${download.appName}...")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشلت إعادة المحاولة: ${e.localizedMessage}")
            }
        }
    }

    fun deleteDownload(downloadId: String) {
        viewModelScope.launch {
            repository.deleteDownload(downloadId)
            _snackbarEvent.emit("تمت إزالة عنصر التحميل")
        }
    }

    fun toggleFavorite(app: AppEntity) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.id == app.id }
            repository.toggleFavorite(app.id, isFav)
            _snackbarEvent.emit(if (isFav) "تمت الإزالة من المفضلة" else "تمت الإضافة إلى المفضلة ❤️")
        }
    }

    fun saveApp(app: AppEntity) {
        viewModelScope.launch {
            try {
                val directApp = repository.getAppByIdDirect(app.id)
                if (directApp != null) {
                    repository.updateApp(app)
                    _snackbarEvent.emit("تم تحديث التطبيق بنجاح")
                } else {
                    repository.addApp(app)
                    _snackbarEvent.emit("تمت إضافة التطبيق بنجاح إلى المتجر")
                }
                closeAddEditApp()
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل الحفظ: ${e.localizedMessage}")
            }
        }
    }

    fun deleteApp(app: AppEntity) {
        viewModelScope.launch {
            try {
                repository.deleteApp(app)
                _snackbarEvent.emit("تم حذف التطبيق بنجاح")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل الحذف: ${e.localizedMessage}")
            }
        }
    }

    fun togglePublishStatus(app: AppEntity) {
        viewModelScope.launch {
            try {
                val newStatus = !app.published
                repository.setPublishStatus(app.id, app.name, newStatus)
                _snackbarEvent.emit(if (newStatus) "تم نشر التطبيق في المتجر" else "تم إلغاء النشر وتحويله لمسودة")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل تغيير حالة النشر: ${e.localizedMessage}")
            }
        }
    }

    fun addAdmin(
        email: String,
        name: String,
        password: String = "ApexAdmin@2026",
        role: String = UserRole.ADMIN.roleKey,
        canAdd: Boolean = true,
        canEdit: Boolean = true,
        canDelete: Boolean = false,
        canPublish: Boolean = true,
        canUpload: Boolean = true,
        canManageAdmins: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                repository.addAdminByEmail(
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
                _snackbarEvent.emit("تمت إضافة المشرف بنجاح وتعيين الصلاحيات وكلمة المرور")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل إضافة المشرف: ${e.localizedMessage}")
            }
        }
    }

    fun toggleAdminStatus(admin: AdminEntity) {
        viewModelScope.launch {
            try {
                val newStatus = if (admin.status == "ACTIVE") "DISABLED" else "ACTIVE"
                repository.updateAdminStatus(admin.id, admin.email, newStatus)
                _snackbarEvent.emit("تم تغيير حالة المشرف إلى: $newStatus")
            } catch (e: Exception) {
                _snackbarEvent.emit("خطأ: ${e.localizedMessage}")
            }
        }
    }

    fun deleteAdmin(admin: AdminEntity) {
        viewModelScope.launch {
            try {
                repository.deleteAdmin(admin)
                _snackbarEvent.emit("تم حذف المشرف وإلغاء صلاحياته")
            } catch (e: Exception) {
                _snackbarEvent.emit("خطأ: ${e.localizedMessage}")
            }
        }
    }

    fun updateAdminPermissions(admin: AdminEntity) {
        viewModelScope.launch {
            try {
                repository.updateAdminPermissions(admin)
                _snackbarEvent.emit("تم تحديث صلاحيات المشرف")
            } catch (e: Exception) {
                _snackbarEvent.emit("خطأ: ${e.localizedMessage}")
            }
        }
    }

    fun updateUserStatus(user: UserEntity) {
        viewModelScope.launch {
            try {
                val newStatus = if (user.status == "ACTIVE") "SUSPENDED" else "ACTIVE"
                repository.updateUserStatus(user.id, user.email, newStatus)
                _snackbarEvent.emit("تم تحديث حالة المستخدم إلى: $newStatus")
            } catch (e: Exception) {
                _snackbarEvent.emit("خطأ: ${e.localizedMessage}")
            }
        }
    }

    fun simulateUpload(fileName: String, sizeBytes: Long, onDone: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val url = repository.uploadPackageFile(fileName, sizeBytes) {}
                onDone(url)
                _snackbarEvent.emit("تم رفع الملف بنجاح وحفظه في مساحة التخزين")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل الرفع: ${e.localizedMessage}")
            }
        }
    }

    fun updateSettings(settings: StoreSettings) {
        viewModelScope.launch {
            try {
                repository.updateStoreSettings(settings)
                _snackbarEvent.emit("تم حفظ إعدادات المتجر بنجاح")
            } catch (e: Exception) {
                _snackbarEvent.emit("فشل حفظ الإعدادات: ${e.localizedMessage}")
            }
        }
    }

    fun resetStoreToCleanStart(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = repository.resetStoreToCleanStart()
                res.fold(
                    onSuccess = { msg ->
                        _snackbarEvent.emit(msg)
                        onResult(true, msg)
                    },
                    onFailure = { err ->
                        val msg = err.localizedMessage ?: "حدث خطأ أثناء تفريغ المتجر"
                        _snackbarEvent.emit(msg)
                        onResult(false, msg)
                    }
                )
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "فشلت العملية"
                _snackbarEvent.emit(msg)
                onResult(false, msg)
            }
        }
    }
}
