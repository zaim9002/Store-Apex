package com.example.data.model

data class StoreCategory(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val iconName: String,
    val type: AppType
)

object CategoryData {
    val appCategories = listOf(
        StoreCategory("tools", "أدوات", "Tools", "build", AppType.APP),
        StoreCategory("social", "اجتماعي", "Social", "chat", AppType.APP),
        StoreCategory("productivity", "إنتاجية", "Productivity", "task_alt", AppType.APP),
        StoreCategory("education", "تعليم", "Education", "school", AppType.APP),
        StoreCategory("photography", "تصوير", "Photography", "photo_camera", AppType.APP),
        StoreCategory("music", "موسيقى", "Music", "headphones", AppType.APP),
        StoreCategory("video", "فيديو", "Video", "movie", AppType.APP),
        StoreCategory("entertainment", "ترفيه", "Entertainment", "theater_comedy", AppType.APP),
        StoreCategory("business", "أعمال", "Business", "business_center", AppType.APP),
        StoreCategory("health", "صحة", "Health", "favorite", AppType.APP),
        StoreCategory("personalization", "تخصيص", "Personalization", "palette", AppType.APP),
        StoreCategory("internet", "إنترنت", "Internet", "language", AppType.APP),
        StoreCategory("other_app", "أخرى", "Other", "category", AppType.APP)
    )

    val gameCategories = listOf(
        StoreCategory("action", "أكشن", "Action", "sports_kabaddi", AppType.GAME),
        StoreCategory("adventure", "مغامرات", "Adventure", "explore", AppType.GAME),
        StoreCategory("racing", "سباق", "Racing", "sports_score", AppType.GAME),
        StoreCategory("sports", "رياضة", "Sports", "sports_soccer", AppType.GAME),
        StoreCategory("puzzle", "ألغاز", "Puzzle", "extension", AppType.GAME),
        StoreCategory("strategy", "استراتيجية", "Strategy", "military_tech", AppType.GAME),
        StoreCategory("rpg", "RPG", "RPG", "shield", AppType.GAME),
        StoreCategory("simulation", "محاكاة", "Simulation", "flight", AppType.GAME),
        StoreCategory("arcade", "Arcade", "Arcade", "videogame_asset", AppType.GAME),
        StoreCategory("other_game", "أخرى", "Other", "games", AppType.GAME)
    )

    fun getAllCategories(): List<StoreCategory> = appCategories + gameCategories
}
