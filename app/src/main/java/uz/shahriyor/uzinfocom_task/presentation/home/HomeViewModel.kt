package uz.shahriyor.uzinfocom_task.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import uz.shahriyor.uzinfocom_task.domain.Poi
import uz.shahriyor.uzinfocom_task.domain.repository.MapRepository

class HomeViewModel(
    repository: MapRepository
) : ViewModel() {

    private val pois = repository.getPois()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedCategories = MutableStateFlow<Set<PoiCategory>>(emptySet())
    val selectedCategories: StateFlow<Set<PoiCategory>> = _selectedCategories.asStateFlow()

    private val _selectedPoi = MutableStateFlow<Poi?>(null)
    val selectedPoi: StateFlow<Poi?> = _selectedPoi.asStateFlow()

    val categories: StateFlow<List<PoiCategory>> = pois
        .map { list -> list.map { PoiCategory.from(it.category) }.distinct().sortedBy { it.ordinal } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val visiblePois: StateFlow<List<Poi>> = combine(pois, _selectedCategories) { list, selected ->
        if (selected.isEmpty()) list else list.filter { PoiCategory.from(it.category) in selected }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun toggleCategory(category: PoiCategory) {
        _selectedCategories.update { if (category in it) it - category else it + category }
    }

    fun selectPoi(poi: Poi) {
        _selectedPoi.value = poi
    }

    fun clearSelection() {
        _selectedPoi.value = null
    }
}
