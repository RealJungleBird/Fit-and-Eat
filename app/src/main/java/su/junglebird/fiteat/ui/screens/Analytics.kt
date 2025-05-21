package su.junglebird.fiteat.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.datetime.LocalDate
import su.junglebird.fiteat.viewmodels.AnalyticsViewModel
import java.text.SimpleDateFormat
import java.time.MonthDay
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Analytics(viewModel: AnalyticsViewModel = hiltViewModel()) {
    // Состояния данных
    val chartData by viewModel.chartEntries.collectAsState()   // Данные для размещения на графике
    val currentMonth by viewModel.currentMonthYear.collectAsState()

    // Состояния для управления UI
    var showDatePicker by remember { mutableStateOf(false) } // Активен ли диалог выбора даты

    // Основной макет экрана
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Аналитика",
                        fontWeight = FontWeight.Black
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.changeMonth(-1) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Предыдущий день")
                    }
                    Text(
                        text = formatMonthForDisplay(currentMonth),
                        modifier = Modifier
                            .clickable { showDatePicker = true }
                            .padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(onClick = { viewModel.changeMonth(1) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, "Следующий день")
                    }
                }

                LineChartComposable(
                    entries = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(2.dp)
                )
            }
        }

        // Диалог выбора даты
        if(showDatePicker) {
            DatePickDialog(
                initialDate = viewModel.currentDate,
                onDateSelected = {
                    viewModel.setDate(it)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}



// Диалог выбора даты через системный календарь
@Composable
private fun DatePickDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        set(initialDate.year, initialDate.monthNumber - 1, initialDate.dayOfMonth)
    }

    val datePicker = DatePickerDialog(
        LocalContext.current,
        { _, year, month, day ->
            onDateSelected(LocalDate(year, month + 1, day))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePicker.setOnDismissListener { onDismiss() }
    datePicker.show()
}

// Форматирование "2023-10" -> "Октябрь 2023"
private fun formatMonthForDisplay(monthYear: String): String {
    val yearMonth = YearMonth.parse(monthYear, DateTimeFormatter.ofPattern("yyyy-MM"))
    val months = arrayOf(
        "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
    )
    return "${months[yearMonth.monthValue - 1]} ${yearMonth.year}"
}


@Composable
fun LineChartComposable(
    entries: List<Entry>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                setupChartAppearance(context, colors)  // Настройка внешнего вида
            }
        },
        update = { chart ->
            updateChartData(chart, entries, colors, context) // Обновление данных
        }
    )
}

// Настройка внешнего вида графика
private fun LineChart.setupChartAppearance(context: Context, colors: ColorScheme) {
    description.isEnabled = false
    legend.isEnabled = false
    legend.textColor = colors.onSurface.toArgb()
    setDrawBorders(true)
    setBorderColor(colors.onSurface.toArgb())


    // Настройка оси X
    xAxis.apply {
        textColor = colors.onSurface.toArgb()
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
            }
        }
    }

    // Настройка оси Y
    axisLeft.apply {
        axisMinimum = 0f
        axisMaximum = 3000f
        textColor = colors.onSurface.toArgb()
    }

    axisRight.isEnabled = false
}

// Обновление данных графтка
private fun updateChartData(
    chart: LineChart,
    entries: List<Entry>,
    colors: ColorScheme,
    context: Context
) {
    val dataSet = LineDataSet(entries, "Калории").apply {
        color = colors.primary.toArgb()
        valueTextColor = colors.onSurface.toArgb()
        setDrawCircles(true)
        lineWidth = 2f
    }

    chart.data = LineData(dataSet)
    chart.invalidate() // Перерисовать график
}