package uz.shahriyor.uzinfocom_task.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.shahriyor.uzinfocom_task.R
import uz.shahriyor.uzinfocom_task.domain.Poi

private val OpenColor = Color(0xFF2E7D32)
private val ClosedColor = Color(0xFFC62828)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiDetailsSheet(
    poi: Poi,
    onDismiss: () -> Unit
) {
    val category = PoiCategory.from(poi.category)

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = poi.name, style = MaterialTheme.typography.headlineSmall)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(category.color), CircleShape)
                )
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = poi.rating?.let { stringResource(R.string.poi_rating, it) }
                    ?: stringResource(R.string.poi_no_rating),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(text = poi.address, style = MaterialTheme.typography.bodyLarge)

            Text(
                text = stringResource(if (poi.openNow) R.string.poi_open else R.string.poi_closed),
                color = if (poi.openNow) OpenColor else ClosedColor,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
