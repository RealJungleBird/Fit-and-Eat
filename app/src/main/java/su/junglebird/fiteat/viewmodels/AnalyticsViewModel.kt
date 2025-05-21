package su.junglebird.fiteat.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import su.junglebird.fiteat.data.repository.DailyMenuItemRepository
import javax.inject.Inject
import su.junglebird.fiteat.data.database.entities.DayCalories
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val menuItemRepository: DailyMenuItemRepository
): ViewModel() {

    // Текущая дата с отслеживанием изменений
    private val _currentDate = mutableStateOf(Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    )
    val currentDate get() = _currentDate.value

    private val _currentMonthYear = MutableStateFlow(getCurrentMonth())
    private val _chartEntries = MutableStateFlow<List<Entry>>(emptyList())
    val chartEntries: StateFlow<List<Entry>> = _chartEntries.asStateFlow()
    val currentMonthYear: StateFlow<String> = _currentMonthYear.asStateFlow()

    init {
        _currentMonthYear
            .flatMapLatest { monthYear ->
                menuItemRepository.getMonthlyCalories(monthYear)
                    .map { data -> prepareChartData(data, monthYear) }
            }
            .onEach { entries -> _chartEntries.value = entries }
            .launchIn(viewModelScope)
    }

    // Получение данных для графика
    fun getChartData(monthYear: String): Flow<List<Entry>> {
        return menuItemRepository.getMonthlyCalories(monthYear).map { data ->
            prepareChartData(data, monthYear)
        }
    }

    // Преобразование DailyCalories в Entry (для MPAndroidChart)
    private fun prepareChartData(
        dailyCalories: List<DayCalories>,
        monthYear: String
    ): List<Entry> {
        // Генерация всех дней месяца (1, 2, ..., 31)
        val allDays = generateAllDaysInMonth(monthYear)

        // Создание Map: дата -> калории
        val caloriesMap = dailyCalories.associate { it.date to it.totalCalories }

        // Заполнение данных (включая дни без записей)
        return allDays.map { day ->
            val date = formatDate(monthYear, day)
            val calories = caloriesMap[date] ?: 0
            Entry(day.toFloat(), calories.toFloat())
        }
    }

    // Форматирование даты в "yyyy-MM-dd" (для сопоставления с базой)
    private fun formatDate(monthYear: String, day: Int): String {
        return "$monthYear-${day.toString().padStart(2, '0')}"
    }

    // Генерация списка дней месяца
    private fun generateAllDaysInMonth(monthYear: String): List<Int> {
        val yearMonth = YearMonth.parse(monthYear, DateTimeFormatter.ofPattern("yyyy-MM"))
        return (1..yearMonth.lengthOfMonth()).toList()
    }

    private fun getCurrentMonth(): String {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
            .let { YearMonth.of(it.year, it.month) }
            .format(DateTimeFormatter.ofPattern("yyyy-MM"))
    }

    fun changeMonth(monthOffset: Int) {
        val current = YearMonth.parse(_currentMonthYear.value, DateTimeFormatter.ofPattern("yyyy-MM"))
        val newMonth = current.plusMonths(monthOffset.toLong())
        _currentMonthYear.value = newMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    }

    fun setDate(newDate: LocalDate) {
        _currentDate.value = newDate
    }

}