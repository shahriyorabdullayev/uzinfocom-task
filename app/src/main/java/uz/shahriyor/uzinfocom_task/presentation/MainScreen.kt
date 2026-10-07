package uz.shahriyor.uzinfocom_task.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

private val TASHKENT = Point(41.311081, 69.279737)
private const val DEFAULT_ZOOM = 12f

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var mapView by remember { mutableStateOf<MapView?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        YandexMapView(
            modifier = Modifier.fillMaxSize(),
            onReady = { mv ->
                mv.mapWindow.map.move(CameraPosition(TASHKENT, DEFAULT_ZOOM, 0f, 0f))
                mapView = mv
            }
        )
        AppNavGraph(mapView = mapView)
    }
}
