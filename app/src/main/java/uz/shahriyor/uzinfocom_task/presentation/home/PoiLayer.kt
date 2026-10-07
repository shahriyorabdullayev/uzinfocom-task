package uz.shahriyor.uzinfocom_task.presentation.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterListener
import com.yandex.mapkit.map.ClusterTapListener
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.presentation.map.MapIcons

private const val CLUSTER_RADIUS = 60.0
private const val CLUSTER_MIN_ZOOM = 15
private const val CLUSTER_ZOOM_STEP = 2f
private const val CAMERA_ANIMATION_SECONDS = 0.3f

@Composable
fun PoiLayer(
    mapView: MapView,
    pois: List<Poi>,
    onPoiClick: (Poi) -> Unit
) {
    val context = LocalContext.current
    val clusterColor = MaterialTheme.colorScheme.primary.toArgb()
    val currentOnPoiClick by rememberUpdatedState(onPoiClick)
    val map = mapView.mapWindow.map

    val clusterTapListener = remember(map) {
        ClusterTapListener { cluster ->
            val camera = map.cameraPosition
            map.move(
                CameraPosition(cluster.appearance.geometry, camera.zoom + CLUSTER_ZOOM_STEP, camera.azimuth, camera.tilt),
                Animation(Animation.Type.SMOOTH, CAMERA_ANIMATION_SECONDS),
                null
            )
            true
        }
    }
    val clusterListener = remember(map, clusterColor) {
        ClusterListener { cluster ->
            cluster.appearance.setIcon(MapIcons.cluster(context, cluster.size, clusterColor))
            cluster.addClusterTapListener(clusterTapListener)
        }
    }
    val placemarkTapListener = remember {
        MapObjectTapListener { mapObject, _ ->
            val poi = mapObject.userData as? Poi
            if (poi != null) currentOnPoiClick(poi)
            poi != null
        }
    }

    var collection by remember { mutableStateOf<ClusterizedPlacemarkCollection?>(null) }

    DisposableEffect(map, clusterListener) {
        val created = map.mapObjects.addClusterizedPlacemarkCollection(clusterListener)
        collection = created
        onDispose {
            map.mapObjects.remove(created)
            collection = null
        }
    }

    LaunchedEffect(collection, pois) {
        val target = collection ?: return@LaunchedEffect
        target.clear()
        pois.groupBy { PoiCategory.from(it.category) }.forEach { (category, group) ->
            val placemarks = target.addPlacemarks(
                group.map { Point(it.lat, it.lng) },
                MapIcons.poi(context, category.color),
                IconStyle()
            )
            placemarks.forEachIndexed { index, placemark ->
                placemark.userData = group[index]
                placemark.addTapListener(placemarkTapListener)
            }
        }
        target.clusterPlacemarks(CLUSTER_RADIUS, CLUSTER_MIN_ZOOM)
    }
}
