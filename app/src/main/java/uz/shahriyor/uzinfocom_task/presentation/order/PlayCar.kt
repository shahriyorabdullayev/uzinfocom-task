package uz.shahriyor.uzinfocom_task.presentation.order

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.shahriyor.uzinfocom_task.R
import java.util.Locale
import kotlin.math.roundToInt

private const val MPS_TO_KMH = 3.6

@Composable
fun PlayCar(
    state: OrderUiState,
    onTogglePlayback: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledIconButton(onClick = onTogglePlayback) {
                    Icon(
                        painter = painterResource(if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = stringResource(if (state.isPlaying) R.string.playback_pause else R.string.playback_play)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(
                            R.string.playback_speed_kmh,
                            ((state.car?.speedMps ?: 0.0) * MPS_TO_KMH).roundToInt()
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${formatTime(state.positionMs)} / ${formatTime(state.durationMs)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    return String.format(Locale.US, "%02d:%02d", totalSeconds / 60, totalSeconds % 60)
}
