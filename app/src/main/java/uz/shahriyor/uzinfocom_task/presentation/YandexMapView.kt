package uz.shahriyor.uzinfocom_task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.mapview.MapView

@Composable
fun YandexMapView(
    modifier: Modifier = Modifier,
    onReady: (MapView) -> Unit = {}
) {
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember { MapView(ctx) }

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = { }
    )

    DisposableEffect(mapView, lifecycleOwner) {
        onReady(mapView)
        var isMapStarted = false
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart()
                    isMapStarted = true
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    isMapStarted = false
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            if (isMapStarted) {
                mapView.onStop()
                isMapStarted = false
            }
        }
    }
}