package com.example.data.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    private const val TAG = "FirebaseManager"

    const val PROJECT_ID = "apex-store-3126f"
    const val PROJECT_NUMBER = "735467450775"
    const val STORAGE_BUCKET = "apex-store-3126f.firebasestorage.app"
    const val APPLICATION_ID = "1:735467450775:android:a1b2c3d4e5f6a7b8c9d0e1"
    const val API_KEY = "AIzaSyApexStoreFirebaseCloudServiceKeyDefault"

    // Collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_APPS = "apps"
    const val COLLECTION_ADMINS = "admins"
    const val COLLECTION_CATEGORIES = "categories"
    const val COLLECTION_DOWNLOADS = "downloads"

    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                val options = FirebaseOptions.Builder()
                    .setProjectId(PROJECT_ID)
                    .setApplicationId(APPLICATION_ID)
                    .setApiKey(API_KEY)
                    .setStorageBucket(STORAGE_BUCKET)
                    .setGcmSenderId(PROJECT_NUMBER)
                    .build()
                FirebaseApp.initializeApp(context, options)
                Log.d(TAG, "Firebase initialized with custom options for $PROJECT_ID")
            } else {
                Log.d(TAG, "Firebase already initialized via auto-init")
            }

            // Configure Firestore offline persistence
            try {
                val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build()
                firestore.firestoreSettings = settings
            } catch (e: Exception) {
                Log.w(TAG, "Failed to apply custom Firestore settings: ${e.message}")
            }

            isInitialized = true
        } catch (e: Exception) {
            Log.e(TAG, "Error during Firebase init: ${e.message}", e)
        }
    }

    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    val firestore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    val storage: FirebaseStorage
        get() = try {
            FirebaseStorage.getInstance("gs://$STORAGE_BUCKET")
        } catch (e: Exception) {
            FirebaseStorage.getInstance()
        }

    val usersCollection: CollectionReference
        get() = firestore.collection(COLLECTION_USERS)

    val appsCollection: CollectionReference
        get() = firestore.collection(COLLECTION_APPS)

    val adminsCollection: CollectionReference
        get() = firestore.collection(COLLECTION_ADMINS)

    val categoriesCollection: CollectionReference
        get() = firestore.collection(COLLECTION_CATEGORIES)

    val downloadsCollection: CollectionReference
        get() = firestore.collection(COLLECTION_DOWNLOADS)
}
