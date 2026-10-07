package uz.shahriyor.uzinfocom_task.presentation.order

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import uz.shahriyor.uzinfocom_task.domain.Point
import uz.shahriyor.uzinfocom_task.domain.bearingDegrees
import uz.shahriyor.uzinfocom_task.domain.distanceMeters
import uz.shahriyor.uzinfocom_task.domain.filterAccurate
import uz.shahriyor.uzinfocom_task.domain.interpolate
import uz.shahriyor.uzinfocom_task.domain.interpolateAngle
import uz.shahriyor.uzinfocom_task.domain.repository.MapRepository

class OrderViewModel(
    repository: MapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    private var points: List<Point> = emptyList()
    private var headings: FloatArray = FloatArray(0)
    private var playbackJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getTrack().collect { track ->
                setTrack(track.filterAccurate(30.0))
            }
        }
    }

    fun togglePlayback() {
        if (_uiState.value.isPlaying) pause() else play()
    }

    fun setFollowCar(follow: Boolean) {
        _uiState.update { it.copy(followCar = follow) }
    }

    private fun setTrack(track: List<Point>) {
        points = track
        headings = computeHeadings(track)
        val duration = if (track.size < 2) 0L else {
            track.last().time - track.first().time + (track.last().time - track[track.lastIndex - 1].time)
        }
        _uiState.update { buildFrame(it.copy(durationMs = duration), it.positionMs.coerceAtMost(duration)) }
    }

    private fun play() {
        if (points.size < 2) return
        if (_uiState.value.positionMs >= _uiState.value.durationMs) {
            _uiState.update { buildFrame(it, 0) }
        }
        _uiState.update { it.copy(isPlaying = true, followCar = true) }
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            var lastTick = SystemClock.elapsedRealtime()
            while (isActive) {
                delay(FRAME_DELAY_MS)
                val now = SystemClock.elapsedRealtime()
                val elapsed = (now - lastTick) * _uiState.value.speedMultiplier
                lastTick = now
                _uiState.update { state ->
                    val position = (state.positionMs + elapsed).coerceAtMost(state.durationMs)
                    buildFrame(state, position).copy(isPlaying = position < state.durationMs)
                }
                if (!_uiState.value.isPlaying) break
            }
        }
    }

    private fun pause() {
        playbackJob?.cancel()
        playbackJob = null
        _uiState.update { it.copy(isPlaying = false) }
    }

    private fun buildFrame(state: OrderUiState, positionMs: Long): OrderUiState {
        if (points.isEmpty()) return state.copy(positionMs = positionMs, car = null, traveledPath = emptyList())

        val now = points.first().time + positionMs
        val index = lastReceivedIndex(now)
        val last = points[index]
        val previous = points.getOrNull(index - 1)

        val car = if (previous == null) {
            CarState(last.lat, last.lng, headings[index], last.speed, last.accuracy)
        } else {
            val segmentMs = (last.time - previous.time).coerceAtLeast(1)
            val fraction = ((now - last.time).toDouble() / segmentMs).coerceIn(0.0, 1.0)
            val turnFraction = (fraction / TURN_FRACTION).coerceAtMost(1.0).toFloat()
            CarState(
                lat = interpolate(previous.lat, last.lat, fraction),
                lng = interpolate(previous.lng, last.lng, fraction),
                bearing = interpolateAngle(headings[index - 1], headings[index], turnFraction),
                speedMps = last.speed,
                accuracyMeters = last.accuracy
            )
        }

        val path = ArrayList<Point>(index + 1)
        for (i in 0 until index) path.add(points[i])
        path.add(Point(now, car.lat, car.lng, car.accuracyMeters, car.speedMps))

        return state.copy(
            positionMs = positionMs,
            car = car,
            traveledPath = path,
            isSignalLost = index < points.lastIndex && now - last.time > SIGNAL_TIMEOUT_MS
        )
    }

    private fun lastReceivedIndex(time: Long): Int {
        var low = 0
        var high = points.lastIndex
        while (low < high) {
            val mid = (low + high + 1) / 2
            if (points[mid].time <= time) low = mid else high = mid - 1
        }
        return low
    }

    private fun computeHeadings(track: List<Point>): FloatArray {
        val result = FloatArray(track.size)
        for (i in 1 until track.size) {
            val from = track[i - 1]
            val to = track[i]
            result[i] = if (distanceMeters(from.lat, from.lng, to.lat, to.lng) < MIN_MOVE_METERS) {
                result[i - 1]
            } else {
                bearingDegrees(from.lat, from.lng, to.lat, to.lng)
            }
        }
        if (track.size > 1) result[0] = result[1]
        return result
    }

    private companion object {
        const val MIN_MOVE_METERS = 2.0
        const val SIGNAL_TIMEOUT_MS = 6_000L
        const val FRAME_DELAY_MS = 16L
        const val TURN_FRACTION = 0.3
    }
}
