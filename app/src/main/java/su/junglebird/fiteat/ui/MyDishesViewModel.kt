package su.junglebird.fiteat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import su.junglebird.fiteat.data.CustomDish
import su.junglebird.fiteat.data.CustomDishRepository
import javax.inject.Inject

@HiltViewModel
class MyDishesViewModel @Inject constructor(
    private val repository: CustomDishRepository
): ViewModel() {
    val items = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var editedItem by mutableStateOf<CustomDish?>(null)
        private set

    fun updateEditedItem(item: CustomDish?) {
        editedItem = item
    }

    fun updateItem(item: CustomDish) = viewModelScope.launch {
        repository.updateItem(item)
        editedItem = null
    }

    fun createNewItem() {
        editedItem = CustomDish(
            id = 0,
            name = "",
            calories = 0
        )
    }
}