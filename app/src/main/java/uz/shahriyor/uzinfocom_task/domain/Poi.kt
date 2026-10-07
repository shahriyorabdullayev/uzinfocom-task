package uz.shahriyor.uzinfocom_task.domain

data class Poi(
    val id: String,
    val name: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val rating: Double?,
    val address: String,
    val openNow: Boolean
)