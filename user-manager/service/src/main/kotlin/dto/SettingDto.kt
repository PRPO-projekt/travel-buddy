package si.travelbuddy.dto

data class SettingDto (
    val userId: Long,
    val settingKey: String,
    val settingValue: String,
)