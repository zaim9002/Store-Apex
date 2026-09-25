package com.example.data.repository

import com.example.data.model.AdminEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole

class AccessDeniedException(message: String) : SecurityException(message)

object SecurityValidator {
    const val SUPER_ADMIN_EMAIL = "zaim9002@gmail.com"
    const val ADMIN_EMAIL_PRIMARY = "robew56802@vendprop.com"

    fun isSuperAdmin(user: UserEntity?): Boolean {
        if (user == null) return false
        val role = user.role.trim().lowercase()
        val email = user.email.trim().lowercase()
        val hasSuperAdminRole = role == UserRole.SUPER_ADMIN.roleKey || role == "super_admin" || role == "super admin"
        return (hasSuperAdminRole && email == SUPER_ADMIN_EMAIL) || (email == SUPER_ADMIN_EMAIL)
    }

    fun isAdminOrSuperAdmin(user: UserEntity?, adminProfile: AdminEntity? = null): Boolean {
        if (user == null) return false
        if (isSuperAdmin(user)) return true
        val role = user.role.trim().lowercase()
        val email = user.email.trim().lowercase()
        if (email == ADMIN_EMAIL_PRIMARY) return true
        val isAdminRole = role == UserRole.ADMIN.roleKey || role == "admin" ||
                role == UserRole.MODERATOR.roleKey || role == "moderator"
        if (!isAdminRole) return false
        if (adminProfile != null && adminProfile.status != "ACTIVE") return false
        return true
    }

    /**
     * Strict Database & API Security Enforcement:
     * Validates whether the caller has the required role and granular permissions.
     * Prevents regular users or non-authorized accounts from modifying data or invoking admin operations.
     */
    fun requireSuperAdmin(user: UserEntity?) {
        if (user == null || !isSuperAdmin(user)) {
            throw AccessDeniedException("خطأ أمني 403: تم رفض الوصول. هذه العملية مقتصرة حصرياً على المدير العام الرئيسي ($SUPER_ADMIN_EMAIL).")
        }
    }

    fun requireAdminOrSuperAdmin(user: UserEntity?, adminProfile: AdminEntity? = null, action: String = "") {
        if (user == null) {
            throw AccessDeniedException("خطأ أمني 401: يجب تسجيل الدخول للوصول إلى هذه الوظيفة.")
        }
        if (isSuperAdmin(user)) {
            return // Super Admin has unrestricted full access
        }
        val role = user.role.lowercase()
        val isAdminRole = role == UserRole.ADMIN.roleKey || role == "admin" ||
                role == UserRole.MODERATOR.roleKey || role == "moderator"
        if (!isAdminRole) {
            throw AccessDeniedException("خطأ أمني 403: تم رفض العملية. حسابك مسجل كمستخدم عادي وليس لديك صلاحيات الإدارة.")
        }

        if (adminProfile != null) {
            if (adminProfile.status != "ACTIVE") {
                throw AccessDeniedException("خطأ أمني 403: تم تعطيل حساب المشرف الخاص بك من قبل الإدارة.")
            }
            when (action) {
                "ADD" -> if (!adminProfile.canAddApp) throw AccessDeniedException("ليس لديك صلاحية إضافة تطبيقات جديدة.")
                "EDIT" -> if (!adminProfile.canEditApp) throw AccessDeniedException("ليس لديك صلاحية تعديل بيانات التطبيقات.")
                "DELETE" -> if (!adminProfile.canDeleteApp) throw AccessDeniedException("ليس لديك صلاحية حذف التطبيقات من المتجر.")
                "PUBLISH" -> if (!adminProfile.canPublish) throw AccessDeniedException("ليس لديك صلاحية نشر أو إلغاء نشر التطبيقات.")
                "UPLOAD" -> if (!adminProfile.canUploadFiles) throw AccessDeniedException("ليس لديك صلاحية رفع ملفات APK أو XAPK.")
                "MANAGE_ADMINS" -> if (!adminProfile.canManageAdmins) throw AccessDeniedException("ليس لديك صلاحية إدارة المشرفين.")
            }
        }
    }
}
