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

    val initialApps = emptyList<AppEntity>()
}
