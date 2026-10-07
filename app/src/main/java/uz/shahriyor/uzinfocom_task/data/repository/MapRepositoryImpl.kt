package uz.shahriyor.uzinfocom_task.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.shahriyor.uzinfocom_task.data.TestDao
import uz.shahriyor.uzinfocom_task.data.toDomain
import uz.shahriyor.uzinfocom_task.data.toPoint
import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.domain.Point
import uz.shahriyor.uzinfocom_task.domain.repository.MapRepository

class MapRepositoryImpl(
    private val dao: TestDao
) : MapRepository {

    override fun getPois(): Flow<List<Poi>> =
        dao.getPois().map { list -> list.map { it.toDomain() } }

    override fun getTrack(): Flow<List<Point>> =
        dao.getTrack().map { list -> list.map { it.toPoint() } }
}