package su.junglebird.fiteat.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


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

    // Поток данных, которые будут отображены в графике
    val chartData = snapshotFlow { _currentDate.value }
        .flatMapLatest { date ->
            menuItemRepository.getMonthlyCalories(date.toString())
        }
        .map { dailyDataRaw ->  // Преобразование DayCalories в Pair для последующей передачи в график
            processDataForChart(dailyDataRaw)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    // Обработка данных: заполнение пропущенных дней нулями
    private fun processDataForChart(
        data: List<DayCalories>,
//        monthYear: String
    ): List<Pair<Float, Float>> {
        // Преобразование DayCalories в Pair
        return data.map { elem ->
            Pair(
                elem.day.toFloat(),
                elem.calories.toFloat()
            )
        }
//        val daysInMonth = getDaysInMonth(monthYear)
//        return (1..daysInMonth).map { day ->
//            data.find { it.day == day.toString() }?.calories ?: 0
//        }
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