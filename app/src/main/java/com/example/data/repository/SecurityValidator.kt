package com.example.data.repository

import com.example.data.model.AdminEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole

class AccessDeniedException(message: String) : SecurityException(message)

object SecurityValidator {
    /**
     * Strict Database & API Security Enforcement:
     * Validates whether the caller has the required role and granular permissions.
     * Prevents regular users from modifying data or invoking admin operations.
     */
    fun requireSuperAdmin(user: UserEntity?) {
        if (user == null || user.role != UserRole.SUPER_ADMIN.name) {
            throw AccessDeniedException("خطأ أمني 403: تم رفض الوصول. هذه العملية مقتصرة حصرياً على المدير الرئيسي (Super Admin).")
        }
    }

    fun requireAdminOrSuperAdmin(user: UserEntity?, adminProfile: AdminEntity? = null, action: String = "") {
        if (user == null) {
            throw AccessDeniedException("خطأ أمني 401: يجب تسجيل الدخول للوصول إلى هذه الوظيفة.")
        }
        if (user.role == UserRole.SUPER_ADMIN.name) {
            return // Super Admin has unrestricted full access
        }
        if (user.role != UserRole.ADMIN.name) {
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
