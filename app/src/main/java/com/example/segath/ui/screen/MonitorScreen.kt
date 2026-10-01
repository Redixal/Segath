package com.example.segath.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
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
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

@Composable
fun MonitorScreen(
    isCelsius: Boolean,
    onSimulateAlert: (String, String, Boolean) -> Unit
) {
    var gasPpm by remember { mutableFloatStateOf(145.2f) }
    var temperatureC by remember { mutableFloatStateOf(29.5f) }
    var isSimulatingSpike by remember { mutableStateOf(false) }

    LaunchedEffect(isSimulatingSpike) {
        while (true) {
            delay(2000L)
            if (!isSimulatingSpike) {
                gasPpm = (130f + Random.nextFloat() * 30f)
                temperatureC = (28.0f + Random.nextFloat() * 4.0f)
            }
        }
    }

    val gasStatus = when {
        gasPpm > 300f -> "¡PELIGRO: Gas Crítico!"
        gasPpm > 200f -> "Advertencia: Niveles elevados"
        else -> "Niveles Normales"
    }
    val gasColor = if (gasPpm > 200f) AlertRed else DarkGreenPrimary

    val tempStatus = when {
        temperatureC > 38f -> "¡ALERTA: Temperatura Alta!"
        temperatureC > 33f -> "Precaución: Calor moderado"
        else -> "Temperatura Estable"
    }
    val tempColor = if (temperatureC > 33f) WarningOrange else DarkGreenPrimary

    val displayTemp = if (isCelsius) temperatureC else (temperatureC * 9f / 5f) + 32f
    val tempUnit = if (isCelsius) "°C" else "°F"

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
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "ESTADO DEL SISTEMA",
                        style = MaterialTheme.typography.labelMedium,
                        color = mutedColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Arduino Módulo Conectado",
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkGreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Simulación en Tiempo Real (Placeholder)",
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedColor
                    )
                }
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(DarkGreenPrimary)
                )
            }
        }

        SensorCard(
            title = "Sensor de Gas (MQ-2)",
            icon = Icons.Default.Warning,
            value = String.format(Locale.getDefault(), "%.1f PPM", gasPpm),
            status = gasStatus,
            statusColor = gasColor,
            progress = (gasPpm / 500f).coerceIn(0f, 1f),
            accentColor = gasColor,
            surfaceColor = surfaceColor,
            variantColor = variantColor,
            textColor = textColor,
            mutedColor = mutedColor
        )

        SensorCard(
            title = "Sensor de Temperatura ($tempUnit)",
            icon = Icons.Default.Info,
            value = String.format(Locale.getDefault(), "%.1f %s", displayTemp, tempUnit),
            status = tempStatus,
            statusColor = tempColor,
            progress = (temperatureC / 60f).coerceIn(0f, 1f),
            accentColor = tempColor,
            surfaceColor = surfaceColor,
            variantColor = variantColor,
            textColor = textColor,
            mutedColor = mutedColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dev Simulation Button 1: Gas Alert
        Button(
            onClick = {
                gasPpm = 360.0f
                isSimulatingSpike = true
                onSimulateAlert("¡Fuga de Gas Detectada!", "Concentración crítica de gas (360 PPM).", true)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simular Fuga de Gas (Dev)")
        }

        // Dev Simulation Button 2: Heat Alert
        Button(
            onClick = {
                temperatureC = 42.0f
                isSimulatingSpike = true
                onSimulateAlert("¡Temperatura Extrema!", "Calor peligroso detectado (42.0°C).", true)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = WarningOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simular Calor Extremo (Dev)")
        }

        Button(
            onClick = {
                isSimulatingSpike = false
                gasPpm = 135.0f
                temperatureC = 27.5f
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = variantColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Restablecer Valores Normales", color = textColor)
        }
    }
}

@Composable
fun SensorCard(
    title: String,
    icon: ImageVector,
    value: String,
    status: String,
    statusColor: Color,
    progress: Float,
    accentColor: Color,
    surfaceColor: Color,
    variantColor: Color,
    textColor: Color,
    mutedColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(variantColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = accentColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = accentColor,
                trackColor = variantColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "En línea",
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedColor
                )
            }
        }
    }
}
