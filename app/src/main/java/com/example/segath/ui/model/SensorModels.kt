package com.example.segath.ui.model

data class SensorState(
    val gasPpm: Float = 120.5f,
    val temperatureC: Float = 28.4f,
    val photoresistorValue: Int = 640, // 0 - 1023 Arduino analog reading
    val isSystemArmed: Boolean = true,
    val lastUpdated: String = "Just now"
)

enum class AlertSeverity {
    CRITICAL, WARNING, INFO
}

enum class AlertType {
    GAS, TEMPERATURE, SYSTEM
}

data class AlertItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val severity: AlertSeverity,
    val type: AlertType,
    val isRead: Boolean = false
)
