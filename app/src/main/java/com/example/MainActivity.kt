package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.ApexStoreApp
import com.example.ui.theme.ApexBackground
import com.example.ui.theme.ApexStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApexStoreTheme(isRtl = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ApexBackground
                ) {
                    ApexStoreApp()
                }
            }
        }
    }
}
