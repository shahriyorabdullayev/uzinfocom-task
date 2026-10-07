package uz.shahriyor.uzinfocom_task.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import org.koin.androidx.compose.koinViewModel
import uz.shahriyor.uzinfocom_task.R

private const val MY_LOCATION_ZOOM = 16f

@Composable
fun HomeScreen(
    mapView: MapView?,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel()
) {
    val pois by viewModel.visiblePois.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val selectedPoi by viewModel.selectedPoi.collectAsState()

    if (mapView != null) {
        PoiLayer(
            mapView = mapView,
            pois = pois,
            onPoiClick = viewModel::selectPoi
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        CategoryFilter(
            categories = categories,
            selected = selectedCategories,
            onToggle = viewModel::toggleCategory,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }

    if (mapView != null) {
        MapButtons(
            mapView = mapView,
            modifier = modifier.fillMaxSize()
        )
    }

    selectedPoi?.let { poi ->
        PoiDetailsSheet(poi = poi, onDismiss = viewModel::clearSelection)
    }
}

@Composable
private fun MapButtons(
    mapView: MapView,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val map = mapView.mapWindow.map

    val userLocationLayer = remember(mapView) {
        val existing = mapView.getTag(R.id.user_location_layer) as? UserLocationLayer
        if (existing != null) {
            existing
        } else {
            MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow).also {
                mapView.setTag(R.id.user_location_layer, it)
            }
        }
    }
    val locationManager = remember { MapKitFactory.getInstance().createLocationManager() }
    val locationListener = remember(mapView) {
        object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                map.move(
                    CameraPosition(location.position, MY_LOCATION_ZOOM, 0f, 0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )
            }

            override fun onLocationStatusUpdated(status: LocationStatus) = Unit
        }
    }

    fun myCurrentLocation() {
        userLocationLayer.isVisible = true
        locationManager.requestSingleUpdate(locationListener)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) myCurrentLocation()
    }

    fun zoomBy(delta: Float) {
        val position = map.cameraPosition
        map.move(
            CameraPosition(position.target, position.zoom + delta, position.azimuth, position.tilt),
            Animation(Animation.Type.SMOOTH, 0.3f),
            null
        )
    }

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SmallFloatingActionButton(onClick = { zoomBy(1f) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_zoom_in),
                    contentDescription = null
                )
            }
            SmallFloatingActionButton(onClick = { zoomBy(-1f) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_zoom_out),
                    contentDescription = null
                )
            }

            SmallFloatingActionButton(
                onClick = {
                    val granted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) {
                        myCurrentLocation()
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    contentDescription = null
                )
            }
        }

    }
}

@Composable
private fun CategoryFilter(
    categories: List<PoiCategory>,
    selected: Set<PoiCategory>,
    onToggle: (PoiCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.key }) { category ->
            FilterChip(
                selected = category in selected,
                onClick = { onToggle(category) },
                label = { Text(category.label) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(category.color), CircleShape)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}
