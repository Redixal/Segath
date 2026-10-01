package com.example.segath.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.example.segath.ui.model.AlertItem
import com.example.segath.ui.model.AlertSeverity
import com.example.segath.ui.model.AlertType
import com.example.segath.ui.theme.*
import com.example.segath.util.NotificationHelper
import java.text.SimpleDateFormat
import java.util.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Monitor : Screen("monitor", "Monitoreo", Icons.Default.Home)
    data object Alerts : Screen("alerts", "Alertas", Icons.Default.Notifications)
    data object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userEmail: String,
    isDarkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    isCelsius: Boolean,
    onCelsiusChanged: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Monitor) }
    var isLoading by remember { mutableStateOf(false) }
    var isErrorState by remember { mutableStateOf(false) }

    // Persistent profile settings state across tab switches
    var pushNotifications by remember { mutableStateOf(true) }
    var emailAlerts by remember { mutableStateOf(true) }
    var highTempAlerts by remember { mutableStateOf(true) }
    
    var alertsList by remember {
        mutableStateOf(
            listOf(
                AlertItem(
                    id = "1",
                    title = "Temperatura Alta Detectada",
                    description = "El sensor de temperatura registró 39.2°C en el área de monitoreo.",
                    timestamp = "Hace 10 min",
                    severity = AlertSeverity.WARNING,
                    type = AlertType.TEMPERATURE
                ),
                AlertItem(
                    id = "2",
                    title = "Pico de Gas MQ-2",
                    description = "Se detectó una concentración moderada de gas (210 PPM).",
                    timestamp = "Hace 25 min",
                    severity = AlertSeverity.CRITICAL,
                    type = AlertType.GAS
                )
            )
        )
    }

    val unreadAlertsCount = alertsList.size
    val surfaceColor = MaterialTheme.colorScheme.surface
    val variantColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val bgColor = MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            is Screen.Monitor -> "Segath - Panel de Monitoreo"
                            is Screen.Alerts -> "Segath - Alertas"
                            is Screen.Profile -> "Segath - Mi Perfil"
                        },
                        color = textColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = surfaceColor,
                contentColor = DarkGreenPrimary
            ) {
                val items = listOf(Screen.Monitor, Screen.Alerts, Screen.Profile)
                items.forEach { screen ->
                    val selected = currentScreen == screen
                    NavigationBarItem(
                        icon = {
                            if (screen is Screen.Alerts && unreadAlertsCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = AlertRed) {
                                            Text(unreadAlertsCount.toString())
                                        }
                                    }
                                ) {
                                    Icon(screen.icon, contentDescription = screen.title)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = screen.title)
                            }
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = { currentScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkGreenPrimary,
                            selectedTextColor = DarkGreenPrimary,
                            unselectedIconColor = mutedColor,
                            unselectedTextColor = mutedColor,
                            indicatorColor = variantColor
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = bgColor
        ) {
            when {
                isLoading -> {
                    LoadingStateView(message = "Sincronizando con los sensores...")
                }
                isErrorState -> {
                    ErrorStateView(
                        title = "Pérdida de Conexión IoT",
                        message = "No se pudo conectar con el módulo Arduino. Verifique la red.",
                        onRetry = { isErrorState = false }
                    )
                }
                else -> {
                    when (currentScreen) {
                        is Screen.Monitor -> {
                            MonitorScreen(
                                isCelsius = isCelsius,
                                onSimulateAlert = { title, desc, isCritical ->
                                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    val currentTime = timeFormat.format(Date())
                                    val newAlert = AlertItem(
                                        id = UUID.randomUUID().toString(),
                                        title = title,
                                        description = desc,
                                        timestamp = currentTime,
                                        severity = if (isCritical) AlertSeverity.CRITICAL else AlertSeverity.WARNING,
                                        type = AlertType.GAS
                                    )
                                    alertsList = listOf(newAlert) + alertsList

                                    // Trigger system notification to phone's tray
                                    NotificationHelper.showSystemAlertNotification(context, title, desc)
                                }
                            )
                        }
                        is Screen.Alerts -> {
                            if (alertsList.isEmpty()) {
                                EmptyStateView(
                                    icon = Icons.Default.Warning,
                                    title = "No hay alertas registradas",
                                    message = "Todo marcha en orden. No se detectan anomalías de gas o temperatura.",
                                    actionButtonText = "Crear Alerta de Prueba",
                                    onActionClick = {
                                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                        val currentTime = timeFormat.format(Date())
                                        val testTitle = "Alerta de Prueba"
                                        val testDesc = "Generada manualmente por el usuario."
                                        alertsList = listOf(
                                            AlertItem(
                                                id = UUID.randomUUID().toString(),
                                                title = testTitle,
                                                description = testDesc,
                                                timestamp = currentTime,
                                                severity = AlertSeverity.INFO,
                                                type = AlertType.SYSTEM
                                            )
                                        )
                                        NotificationHelper.showSystemAlertNotification(context, testTitle, testDesc)
                                    }
                                )
                            } else {
                                AlertsScreen(
                                    alerts = alertsList,
                                    onClearAlerts = { alertsList = emptyList() }
                                )
                            }
                        }
                        is Screen.Profile -> {
                            ProfileScreen(
                                userEmail = userEmail,
                                pushNotifications = pushNotifications,
                                onPushChanged = { pushNotifications = it },
                                emailAlerts = emailAlerts,
                                onEmailChanged = { emailAlerts = it },
                                highTempAlerts = highTempAlerts,
                                onHighTempChanged = { highTempAlerts = it },
                                isDarkMode = isDarkMode,
                                onDarkModeChanged = onDarkModeChanged,
                                isCelsius = isCelsius,
                                onCelsiusChanged = onCelsiusChanged,
                                onLogout = onLogout
                            )
                        }
                    }
                }
            }
        }
    }
}
