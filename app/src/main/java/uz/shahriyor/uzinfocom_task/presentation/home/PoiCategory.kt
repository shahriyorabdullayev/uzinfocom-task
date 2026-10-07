package uz.shahriyor.uzinfocom_task.presentation.home

import androidx.annotation.StringRes
import uz.shahriyor.uzinfocom_task.R

enum class PoiCategory(val key: String, val label: String, val color: Int) {
    CAFE("cafe", "Cafe", 0xFF8D6E63.toInt()),
    RESTAURANT("restaurant", "Restoran", 0xFFE64A19.toInt()),
    SUPERMARKET("supermarket", "SuperMarket", 0xFF43A047.toInt()),
    ATM("atm", "Bonkamat", 0xFF1E88E5.toInt()),
    PHARMACY("pharmacy", "Dorixona", 0xFF00ACC1.toInt()),
    PARKING("parking", "Parkovka", 0xFF5E35B1.toInt()),
    FUEL("fuel", "Yoqilg'i", 0xFFFFB300.toInt()),
    HOSPITAL("hospital", "Kasalxona", 0xFFD81B60.toInt()),
    OTHER("other", "Boshqa", 0xFF757575.toInt());

    companion object {
        fun from(key: String): PoiCategory = entries.firstOrNull { it.key == key } ?: OTHER
    }
}
