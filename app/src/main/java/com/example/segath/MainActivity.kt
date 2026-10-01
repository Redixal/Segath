package com.example.segath

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.example.segath.ui.screen.*
import com.example.segath.ui.theme.*
import com.example.segath.util.NotificationHelper

enum class AppStage {
    SPLASH, ONBOARDING, LOGIN, REGISTER, FORGOT_PASSWORD, MAIN
}

class MainActivity : ComponentActivity() {
    private var showPermissionDeniedDialog by mutableStateOf(false)
    private var userEmail by mutableStateOf("")

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showPermissionDeniedDialog = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            SegathTheme {
                var appStage by remember { mutableStateOf(AppStage.SPLASH) }

                if (showPermissionDeniedDialog) {
                    AlertDialog(
                        onDismissRequest = { showPermissionDeniedDialog = false },
                        title = { Text("Permiso de Notificaciones", color = TextLight) },
                        text = {
                            Text(
                                "Has denegado el permiso de notificaciones. No podrás recibir alertas de gas y temperatura alta en la bandeja de entrada de tu teléfono.",
                                color = TextMuted
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { showPermissionDeniedDialog = false }) {
                                Text("Entendido", color = NeonGreen)
                            }
                        },
                        containerColor = DarkSurface
                    )
                }

                when (appStage) {
                    AppStage.SPLASH -> {
                        SplashScreen(
                            onSplashFinished = {
                                appStage = AppStage.ONBOARDING
                            }
                        )
                    }
                    AppStage.ONBOARDING -> {
                        OnboardingScreen(
                            onFinishOnboarding = {
                                appStage = AppStage.LOGIN
                            }
                        )
                    }
                    AppStage.LOGIN -> {
                        LoginScreen(
                            onLoginSuccess = { email ->
                                userEmail = email
                                checkAndRequestNotificationPermission()
                                appStage = AppStage.MAIN
                            },
                            onSkipLogin = {
                                userEmail = "Invitado"
                                checkAndRequestNotificationPermission()
                                appStage = AppStage.MAIN
                            },
                            onNavigateToRegister = {
                                appStage = AppStage.REGISTER
                            },
                            onNavigateToForgotPassword = {
                                appStage = AppStage.FORGOT_PASSWORD
                            }
                        )
                    }
                    AppStage.REGISTER -> {
                        RegisterScreen(
                            onRegisterSuccess = { email ->
                                userEmail = email
                                checkAndRequestNotificationPermission()
                                appStage = AppStage.MAIN
                            },
                            onNavigateToLogin = {
                                appStage = AppStage.LOGIN
                            }
                        )
                    }
                    AppStage.FORGOT_PASSWORD -> {
                        ForgotPasswordScreen(
                            onBackToLogin = {
                                appStage = AppStage.LOGIN
                            }
                        )
                    }
                    AppStage.MAIN -> {
                        MainScreen(
                            userEmail = userEmail,
                            onLogout = {
                                userEmail = ""
                                appStage = AppStage.LOGIN
                            }
                        )
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
