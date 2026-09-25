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
            role = UserRole.SUPER_ADMIN.roleKey,
            avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
            passwordHash = com.example.data.util.SecurityHelper.hashPassword("Apex@SuperAdmin2026"),
            status = "ACTIVE"
        ),
        UserEntity(
            id = "user-admin-robew",
            name = "مشرف النظام (Admin)",
            email = "robew56802@vendprop.com",
            role = UserRole.ADMIN.roleKey,
            avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            passwordHash = com.example.data.util.SecurityHelper.hashPassword("alexjjop8@6"),
            status = "ACTIVE"
        ),
        UserEntity(
            id = "user-admin-gjhh",
            name = "مشرف النظام (gjhh611)",
            email = "gjhh611@gmail.com",
            role = UserRole.ADMIN.roleKey,
            avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200",
            passwordHash = com.example.data.util.SecurityHelper.hashPassword("Apex@Admin2026"),
            status = "ACTIVE"
        )
    )

    val admins = listOf(
        AdminEntity(
            id = "admin-super-profile",
            userId = "user-super-admin",
            email = "zaim9002@gmail.com",
            name = "المدير العام (Super Admin)",
            role = UserRole.SUPER_ADMIN.roleKey,
            canAddApp = true,
            canEditApp = true,
            canDeleteApp = true,
            canPublish = true,
            canUploadFiles = true,
            canManageAdmins = true,
            status = "ACTIVE"
        ),
        AdminEntity(
            id = "admin-robew-profile",
            userId = "user-admin-robew",
            email = "robew56802@vendprop.com",
            name = "مشرف النظام (Admin)",
            role = UserRole.ADMIN.roleKey,
            canAddApp = true,
            canEditApp = true,
            canDeleteApp = true,
            canPublish = true,
            canUploadFiles = true,
            canManageAdmins = false,
            status = "ACTIVE"
        ),
        AdminEntity(
            id = "admin-gjhh-profile",
            userId = "user-admin-gjhh",
            email = "gjhh611@gmail.com",
            name = "مشرف النظام (gjhh611)",
            role = UserRole.ADMIN.roleKey,
            canAddApp = true,
            canEditApp = true,
            canDeleteApp = true,
            canPublish = true,
            canUploadFiles = true,
            canManageAdmins = false,
            status = "ACTIVE"
        )
    )

    // Clean start: No fake apps or demo posts
    val initialApps: List<AppEntity> = emptyList()
}
