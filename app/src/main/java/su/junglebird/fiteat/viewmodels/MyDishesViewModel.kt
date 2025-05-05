package su.junglebird.fiteat.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.repository.CustomDishRepository
import javax.inject.Inject

@HiltViewModel
class MyDishesViewModel @Inject constructor(
    private val repository: CustomDishRepository
): ViewModel() {
    val dishes = repository.allDishes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var editedDish by mutableStateOf<CustomDish?>(null)
        private set

    fun startEditing(dish: CustomDish?) {
        editedDish = dish
    }

    // Начать добавление нового блюда
    fun createNewItem() {
        editedDish = CustomDish(
            name = "",
            calories = 0
        )
    }

    // Сохранить / обновить блюдо
    fun saveDish(dish: CustomDish) = viewModelScope.launch {
        if(dish.id == 0L) {
            repository.insert(dish)
        }
        else {
            repository.update(dish)
        }
        editedDish = null
    }

    fun deleteDish(item: CustomDish) = viewModelScope.launch {
        repository.delete(item)
    }
}