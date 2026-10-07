package uz.shahriyor.uzinfocom_task.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.domain.Point

interface MapRepository {

    fun getPois(): Flow<List<Poi>>

    fun getTrack(): Flow<List<Point>>
}
