package su.junglebird.fiteat.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

















//    val chartData: StateFlow<List<Pair<Float, Float>>> = snapshotFlow { selectedMonth }
//        .flatMapLatest { month ->
//            menuItemRepository.getMonthlyCalories(month.toString())
//        }
//        .map { dailyData ->
//            processDataForChart(dailyData, selectedMonth)
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList()
//        )

    // Обработка данных: заполнение пропущенных дней нулями
//    private fun processDataForChart(
//        data: List<DayCalories>,
//        monthYear: String
//    ): List<Int> {
//        val daysInMonth = getDaysInMonth(monthYear)
//        return (1..daysInMonth).map { day ->
//            data.find { it.day == day.toString() }?.calories ?: 0
//        }
//    }
//
//    private fun getCurrentMonth(): String {
//        return SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
//    }
//
//    // Getting days in month number
//    private fun getDaysInMonth(monthYear: String): Int {
//        val calendar = Calendar.getInstance().apply {
//            val (year, month) = monthYear.split("-").map { it.toInt() }
//            set(year, month - 1, 1)
//        }
//        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//    }

    fun changeMonth(monthOffset: Int) {
        _currentDate.value = currentDate.plus(monthOffset, DateTimeUnit.MONTH)
    }

    fun setDate(newDate: LocalDate) {
        _currentDate.value = newDate
    }

}