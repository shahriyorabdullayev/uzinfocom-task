package uz.shahriyor.uzinfocom_task.presentation.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.mapview.MapView
import org.koin.androidx.compose.koinViewModel
import uz.shahriyor.uzinfocom_task.R

@Composable
fun OrderScreen(
    mapView: MapView?,
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (mapView != null) {
        TrackLayer(
            mapView = mapView,
            state = viewModel.uiState,
            onUserMovedMap = { viewModel.setFollowCar(false) }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (state.isSignalLost) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(R.string.playback_signal_lost),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!state.followCar && state.car != null) {
                SmallFloatingActionButton(onClick = { viewModel.setFollowCar(true) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_my_location),
                        contentDescription = stringResource(R.string.playback_follow)
                    )
                }
            }
            PlayCar(
                state = state,
                onTogglePlayback = viewModel::togglePlayback,
            )
        }
    }
}
