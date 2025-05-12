package su.junglebird.fiteat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import su.junglebird.fiteat.data.repository.CustomDishRepository
import su.junglebird.fiteat.data.repository.DailyMenuItemRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DaySummaryViewModel @Inject constructor(
    private val dishRepository: CustomDishRepository,
    private val menuItemRepository: DailyMenuItemRepository
): ViewModel() {

    // Текущая дата в формате "yyyy-MM-dd"
    private val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Поток данных для списка всех блюд
    val dishes = dishRepository.allDishes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Поток данных для сегодняшнего меню
    val dailyMenuItems = menuItemRepository.getItemsForDate(currentDate).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )

    val dailyDishes = menuItemRepository.getDishesForDate(currentDate)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Добавление блюда в меню
    fun addDishToMenu(dish: CustomDish) = viewModelScope.launch {
        menuItemRepository.addToMenu(currentDate, dish.id)
    }

    // Удаление блюда из дневного меню
    fun removeFromMenu(item: DailyMenuItem) = viewModelScope.launch {
        menuItemRepository.removeFromMenu(item)
    }
}