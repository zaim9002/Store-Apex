package com.example.data.local

import com.example.data.model.AdminEntity
import com.example.data.model.AppEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole

object InitialData {
    val users = listOf(
        UserEntity(
            id = "user-super-admin",
            name = "المدير العام (Super Admin)",
            email = "zaim9002@gmail.com",
            role = UserRole.SUPER_ADMIN.name,
            avatar = "",
            passwordHash = com.example.data.util.SecurityHelper.hashPassword("Apex@SuperAdmin2026"),
            status = "ACTIVE"
        )
    )

    val admins = emptyList<AdminEntity>()

    val initialApps = listOf(
        AppEntity(
            id = "app_apex_launcher",
            name = "APEX Launcher Ultra",
            type = "APP",
            developer = "Apex Core Technologies",
            shortDescription = "لانشر احترافي فائق السرعة وخفيف الوزن مع دعم كامل للثيمات والأيقونات والتخصيص المتقدم.",
            description = "لانشر APEX Launcher Ultra هو الخيار الأمثل لتسريع هاتفك وتحسين تجربة الاستخدام. يدعم تنظيم التطبيقات الذكي، وتخصيص الشبكة والودجات، مع توفير استهلاك البطارية والرام بأعلى كفاءة.",
            version = "4.8.2",
            size = "24.5 MB",
            packageName = "com.apex.launcher.ultra",
            androidVersion = "Android 8.0+",
            category = "tools",
            iconUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800",
            apkUrl = "https://apexstore.com/download/apex-launcher-4.8.2.apk",
            xapkUrl = "",
            downloadCount = 184500,
            rating = 4.8f,
            ratingCount = 14200,
            published = true,
            isFeatured = true
        ),
        AppEntity(
            id = "game_cyber_strike",
            name = "Cyber Strike: Tactical Ops",
            type = "GAME",
            developer = "Nova Interactive Studios",
            shortDescription = "لعبة تصويب تكتيكية مستقبلية بجرافيك كونسول فائق الدقة وأطوار لعب متعددة أونلاين.",
            description = "انضم إلى أقوى معارك التصويب من منظور الشخص الأول في عالم السايبربانك. أسلحة متطورة قابلة للتطوير، خرائط متنوعة، ومعارك ملحمية 5 ضد 5 في الوقت الفعلي مع دعم 120 إطار بالثانية.",
            version = "2.4.1",
            size = "1.2 GB",
            packageName = "com.novagames.cyberstrike.xapk",
            androidVersion = "Android 9.0+",
            category = "action",
            iconUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800",
            apkUrl = "",
            xapkUrl = "https://apexstore.com/download/cyber-strike-2.4.1.xapk",
            downloadCount = 340000,
            rating = 4.9f,
            ratingCount = 28500,
            published = true,
            isFeatured = true
        ),
        AppEntity(
            id = "app_pulse_vpn",
            name = "Pulse VPN - Safe & Fast Proxy",
            type = "APP",
            developer = "Pulse Security Labs",
            shortDescription = "بروكسي VPN مشفر وفائق السرعة لحماية الخصوصية وتخطي القيود الجغرافية بنقرة واحدة.",
            description = "تصفح الإنترنت بأمان كامل وتشفير عسكري من الدرجة الأولى. أكثر من 150 خادم عالمي فائق السرعة، مع سياسة صارمة لعدم الاحتفاظ بالسجلات وحماية بياناتك على شبكات الواي فاي العامة.",
            version = "3.1.0",
            size = "18.2 MB",
            packageName = "com.pulselabs.vpn.proxy",
            androidVersion = "Android 7.0+",
            category = "tools",
            iconUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1563986768609-322da13575f3?w=800",
            apkUrl = "https://apexstore.com/download/pulse-vpn-3.1.0.apk",
            xapkUrl = "",
            downloadCount = 98200,
            rating = 4.7f,
            ratingCount = 8900,
            published = true,
            isFeatured = true
        ),
        AppEntity(
            id = "game_shadow_realm",
            name = "Shadow Realm: Dark RPG",
            type = "GAME",
            developer = "Mythic Forge Games",
            shortDescription = "مغامرة آربيجي ملحمية مظلمة بنظام قتال عميق وزنزانات شاسعة مليئة بالأسرار.",
            description = "استكشف عوالم الظلال وقاتل الوحوش القديمة في تجربة تقمص أدوار لا تُنسى. تخصيص كامل للشخصيات، شجرة مهارات هائلة، وأسلحة أسطورية نادرة يمكنك جمعها وترقيتها.",
            version = "1.9.5",
            size = "850 MB",
            packageName = "com.mythicforge.shadowrealm.xapk",
            androidVersion = "Android 8.0+",
            category = "rpg",
            iconUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800",
            apkUrl = "",
            xapkUrl = "https://apexstore.com/download/shadow-realm-1.9.5.xapk",
            downloadCount = 215000,
            rating = 4.8f,
            ratingCount = 19400,
            published = true,
            isFeatured = false
        ),
        AppEntity(
            id = "app_pixel_studio",
            name = "Pixel Studio Pro - Photo Editor",
            type = "APP",
            developer = "Creative Pixel Tech",
            shortDescription = "محرر صور متقدم وفلاتر احترافية مع أدوات الذكاء الاصطناعي لإزالة الخلفية وتعديل الألوان.",
            description = "حوّل صورك العادية إلى لوحات فنية احترافية. يدعم التعديل بالطبقات، فلاتر سينمائية حديثة، وأدوات ذكية لعزل العناصر وتوضيح الصور بجودة 4K دون فقدان التفاصيل.",
            version = "5.2.1",
            size = "45.0 MB",
            packageName = "com.creativepixel.studio.pro",
            androidVersion = "Android 9.0+",
            category = "photography",
            iconUrl = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1541701494587-cb58502866ab?w=800",
            apkUrl = "https://apexstore.com/download/pixel-studio-5.2.1.apk",
            xapkUrl = "",
            downloadCount = 135000,
            rating = 4.6f,
            ratingCount = 11200,
            published = true,
            isFeatured = false
        ),
        AppEntity(
            id = "game_speed_racer",
            name = "Speed Racer X: Drift Legends",
            type = "GAME",
            developer = "Apex Racing Dynamics",
            shortDescription = "سباقات سيارات خارقة فيزيائية وتفحيط واقعي مع أشهر السيارات العالمية المعدلة.",
            description = "عش حماس سباقات الشوارع والتفحيط الحقيقي. فيزياء قيادة متطورة، أصوات محركات حقيقية مسجلة، إمكانية تعديل وتلوين السيارات بالكامل وخوض بطولات أونلاين عالمية.",
            version = "3.0.2",
            size = "920 MB",
            packageName = "com.apexracing.speedracer.xapk",
            androidVersion = "Android 9.0+",
            category = "racing",
            iconUrl = "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?w=200",
            bannerUrl = "https://images.unsplash.com/photo-1511919884226-fd3cad34687c?w=800",
            apkUrl = "",
            xapkUrl = "https://apexstore.com/download/speed-racer-3.0.2.xapk",
            downloadCount = 410000,
            rating = 4.9f,
            ratingCount = 37000,
            published = true,
            isFeatured = true
        )
    )
}
