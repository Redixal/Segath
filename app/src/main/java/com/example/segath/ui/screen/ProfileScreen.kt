package com.example.segath.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.segath.ui.theme.*

@Composable
fun ProfileScreen(
    userEmail: String,
    pushNotifications: Boolean,
    onPushChanged: (Boolean) -> Unit,
    emailAlerts: Boolean,
    onEmailChanged: (Boolean) -> Unit,
    highTempAlerts: Boolean,
    onHighTempChanged: (Boolean) -> Unit,
    isDarkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    isCelsius: Boolean,
    onCelsiusChanged: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val variantColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val mutedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val bgColor = MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(variantColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = DarkGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = if (userEmail.isNotBlank()) userEmail.substringBefore("@") else "Usuario Segath",
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (userEmail.isNotBlank()) userEmail else "Invitado / Sin correo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedColor
                    )
                }
            }
        }

        // Section: App Appearance & Units
        Text(
            text = "Configuración General",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreenPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingSwitchRow(
                    title = "Modo Oscuro",
                    subtitle = "Tema negro sombrío activo",
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChanged,
                    textColor = textColor,
                    mutedColor = mutedColor
                )
                HorizontalDivider(color = variantColor)
                SettingSwitchRow(
                    title = "Unidad de Temperatura",
                    subtitle = if (isCelsius) "Mostrando en Grados Celsius (°C)" else "Mostrando en Fahrenheit (°F)",
                    checked = isCelsius,
                    onCheckedChange = onCelsiusChanged,
                    checkedText = "°C",
                    uncheckedText = "°F",
                    textColor = textColor,
                    mutedColor = mutedColor
                )
            }
        }

        // Section: Preferences
        Text(
            text = "Preferencias de Alertas y Notificaciones",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreenPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingSwitchRow(
                    title = "Notificaciones Push",
                    subtitle = "Alertas instantáneas en dispositivo",
                    checked = pushNotifications,
                    onCheckedChange = onPushChanged,
                    textColor = textColor,
                    mutedColor = mutedColor
                )
                HorizontalDivider(color = variantColor)
                SettingSwitchRow(
                    title = "Alertas por Correo",
                    subtitle = "Reportes de gas y temperatura al mail",
                    checked = emailAlerts,
                    onCheckedChange = onEmailChanged,
                    textColor = textColor,
                    mutedColor = mutedColor
                )
                HorizontalDivider(color = variantColor)
                SettingSwitchRow(
                    title = "Alertas de Alta Temperatura",
                    subtitle = "Notificar cuando supere los 35°C",
                    checked = highTempAlerts,
                    onCheckedChange = onHighTempChanged,
                    textColor = textColor,
                    mutedColor = mutedColor
                )
            }
        }

        // Section: General & Legal
        Text(
            text = "Información y Legal",
            style = MaterialTheme.typography.titleMedium,
            color = DarkGreenPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingActionRow(
                    icon = Icons.Default.Description,
                    title = "Términos de Servicio",
                    textColor = textColor,
                    mutedColor = mutedColor,
                    onClick = { /* Open terms */ }
                )
                HorizontalDivider(color = variantColor)
                SettingActionRow(
                    icon = Icons.Default.PrivacyTip,
                    title = "Política de Privacidad",
                    textColor = textColor,
                    mutedColor = mutedColor,
                    onClick = { /* Open privacy */ }
                )
                HorizontalDivider(color = variantColor)
                SettingActionRow(
                    icon = Icons.Default.Info,
                    title = "Versión de la App",
                    subtitle = "v1.0.0 (Build 2026)",
                    textColor = textColor,
                    mutedColor = mutedColor,
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkedText: String? = null,
    uncheckedText: String? = null,
    textColor: Color,
    mutedColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = textColor, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = mutedColor)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (checkedText != null && uncheckedText != null) {
                Text(
                    text = if (checked) checkedText else uncheckedText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = DarkGreenPrimary,
                    uncheckedThumbColor = mutedColor,
                    uncheckedTrackColor = LightSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun SettingActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    textColor: Color,
    mutedColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = DarkGreenPrimary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyLarge, color = textColor, fontWeight = FontWeight.SemiBold)
                    if (subtitle != null) {
                        Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = mutedColor)
                    }
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = mutedColor)
        }
    }
}
