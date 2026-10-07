package uz.shahriyor.uzinfocom_task.presentation.order

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.flow.StateFlow
import uz.shahriyor.uzinfocom_task.presentation.map.MapIcons

private const val ACCURACY_FILL_ALPHA = 0x33
private const val ACCURACY_STROKE_ALPHA = 0x66

private class TrackObjects(
    val collection: MapObjectCollection,
    val route: PolylineMapObject,
    val accuracy: CircleMapObject,
    val car: PlacemarkMapObject
)

@Composable
fun TrackLayer(
    mapView: MapView,
    state: StateFlow<OrderUiState>,
    onUserMovedMap: () -> Unit
) {
    val context = LocalContext.current
    val primary = MaterialTheme.colorScheme.primary.toArgb()
    val currentOnUserMovedMap by rememberUpdatedState(onUserMovedMap)
    val map = mapView.mapWindow.map

    val cameraListener = remember {
        CameraListener { _, _, reason, _ ->
            if (reason == CameraUpdateReason.GESTURES) currentOnUserMovedMap()
        }
    }

    val objects = remember(map) {
        val collection = map.mapObjects.addCollection()
        val route = collection.addPolyline().apply {
            setStrokeColor(primary)
            strokeWidth = 5f
            outlineColor = android.graphics.Color.WHITE
            outlineWidth = 1f
        }
        val accuracy = collection.addCircle(Circle(Point(0.0, 0.0), 0f)).apply {
            fillColor = withAlpha(primary, ACCURACY_FILL_ALPHA)
            strokeColor = withAlpha(primary, ACCURACY_STROKE_ALPHA)
            strokeWidth = 1f
            isVisible = false
        }
        val car = collection.addPlacemark().apply {
            setIcon(MapIcons.car(context), IconStyle().setRotationType(RotationType.ROTATE).setFlat(true))
            isVisible = false
        }
        TrackObjects(collection, route, accuracy, car)
    }

    DisposableEffect(map, objects) {
        map.addCameraListener(cameraListener)
        onDispose {
            map.removeCameraListener(cameraListener)
            map.mapObjects.remove(objects.collection)
        }
    }

    LaunchedEffect(objects) {
        var centered = false
        state.collect { ui ->
            val car = ui.car ?: return@collect
            val position = Point(car.lat, car.lng)

            objects.route.geometry = Polyline(ui.traveledPath.map { Point(it.lat, it.lng) })
            objects.accuracy.geometry = Circle(position, car.accuracyMeters.toFloat())
            objects.accuracy.isVisible = true
            objects.car.geometry = position
            objects.car.direction = car.bearing
            objects.car.isVisible = true

            if (ui.followCar) {
                val camera = map.cameraPosition
                val zoom = if (centered) camera.zoom else 16f
                map.move(CameraPosition(position, zoom, camera.azimuth, camera.tilt))
                centered = true
            }
        }
    }
}

private fun withAlpha(color: Int, alpha: Int): Int = (color and 0x00FFFFFF) or (alpha shl 24)
