package su.junglebird.fiteat.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import su.junglebird.fiteat.data.repository.CustomDishRepository
import su.junglebird.fiteat.data.repository.DailyMenuItemRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DaySummaryViewModel @Inject constructor(
    private val dishRepository: CustomDishRepository,
    private val menuItemRepository: DailyMenuItemRepository
): ViewModel() {

    private val _currentDate = mutableStateOf(Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val currentDate get() = _currentDate.value

    fun changeDate(daysOffset: Int) {
        _currentDate.value = currentDate.plus(daysOffset, DateTimeUnit.DAY)
    }

    fun setDate(newDate: LocalDate) {
        _currentDate.value = newDate
    }

    // Поток данных для списка всех доступных блюд
    val dishes = dishRepository.allDishes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Поток данных для сегодняшнего меню
    val dailyMenuItems = snapshotFlow { _currentDate.value }
        .flatMapLatest { date ->
            menuItemRepository.getItemsForDate(date.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Поток данных для пунктов меню на указанную дату
    val dailyDishes = snapshotFlow { _currentDate.value }
        .flatMapLatest { date ->
            menuItemRepository.getDishesForDate(date.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Поток данных для подсчёта общего числа калорий за указанную дату
    val dailyCalories = snapshotFlow { _currentDate.value }
        .flatMapLatest { date ->
            menuItemRepository.getDailyCalories(date.toString())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Добавление блюда в меню
    fun addDishToMenu(dish: CustomDish) = viewModelScope.launch {
        menuItemRepository.addToMenu(currentDate.toString(), dish.id)
    }

    // Удаление блюда из дневного меню
    fun removeFromMenu(item: DailyMenuItem) = viewModelScope.launch {
        menuItemRepository.removeFromMenu(item)
    }
}