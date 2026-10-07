package uz.shahriyor.uzinfocom_task.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.domain.Track

class JsonDataSeeder(
    private val context: Context,
    private val dao: TestDao,
    private val gson: Gson = Gson()
) {

    suspend fun seed() = withContext(Dispatchers.IO) {
        if (dao.poiCount() == 0) {
            val type = object : TypeToken<List<Poi>>() {}.type
            val pois: List<Poi> = gson.fromJson(readAsset(POIS_FILE), type)
            dao.insertPois(pois.map { it.toEntity() })
        }
        if (dao.trackCount() == 0) {
            val track = gson.fromJson(readAsset(TRACK_FILE), Track::class.java)
            dao.insertTrack(track.toEntities())
        }
    }

    private fun readAsset(fileName: String): String =
        context.assets.open(fileName).bufferedReader().use { it.readText() }

    private companion object {
        const val POIS_FILE = "pois.json"
        const val TRACK_FILE = "track.json"
    }
}
