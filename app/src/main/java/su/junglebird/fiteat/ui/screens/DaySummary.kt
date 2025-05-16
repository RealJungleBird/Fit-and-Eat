package su.junglebird.fiteat.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.LocalDate
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import su.junglebird.fiteat.viewmodels.DaySummaryViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DaySummary(viewModel: DaySummaryViewModel = hiltViewModel()) {
    // Состояния данных
    val dailyMenuItems by viewModel.dailyMenuItems.collectAsState() // Список пунктов меню
    val dailyDishes by viewModel.dailyDishes.collectAsState() // Список блюд для текущей даты
    val allDishes by viewModel.dishes.collectAsState() // Все доступные блюда
    val totalCalories by viewModel.dailyCalories.collectAsState() // Общее число калорий за день

    // Состояния для управления UI
    var showSelector by remember { mutableStateOf(false) } // Активен ли диалог выбора блюда
    var showDatePicker by remember { mutableStateOf(false) } // Активен ли диалог выбора даты

    // Основной макет экрана
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSelector = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить блюдо",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->

        Column {
            // Панель с датой
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.changeDate(-1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Предыдущий день")
                }
                Text(
                    text = viewModel.currentDate.formatDate(),
                    modifier = Modifier
                        .clickable { showDatePicker = true }
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = { viewModel.changeDate(1) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Следующий день")
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Всего употреблено:",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "$totalCalories Ккал",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Меню на сегодня:",
                style = MaterialTheme.typography.titleLarge
            )
            // список пунктов меню
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                items(dailyMenuItems) { menuItem ->
                    // поиск соответствующего блюда по ID
                    val dish = dailyDishes.firstOrNull { it.id == menuItem.dishId }

                    dish?.let {
                        DailyMenuItemCard(
                            dish = it,
                            onRemove = { viewModel.removeFromMenu(menuItem) }
                        )
                        HorizontalDivider(thickness = 1.dp)
                    }
                }
            }
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

    // Диалог выбора блюд
    if(showSelector) {
        DishSelectorDialog(
            dishes = allDishes,
            onDismiss = { showSelector = false },
            onSelect = { selectedDish ->
                viewModel.addDishToMenu(selectedDish)
                showSelector = false
            }
        )
    }
}


@Composable
fun DailyMenuItemCard(
    dish: CustomDish,
    onRemove: () -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // информация о блюде
            Column {
                Text(
                    text = dish.name
                )
                Text(
                    text = "${dish.calories} Ккал"
                )
            }
            // кнопка удаления
            IconButton(
                onClick = { showRemoveDialog = true }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if(showRemoveDialog) {
        MenuItemRemoveDialog(
            dish.name,
            onConfirm =  {
                onRemove()
                showRemoveDialog = false
            },
            onDismiss =  { showRemoveDialog = false }
        )
    }
}


@Composable
fun DishSelectorDialog(
    dishes: List<CustomDish>,
    onDismiss: () -> Unit,
    onSelect: (CustomDish) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredDishes = remember(searchQuery, dishes) {
        if(searchQuery.isBlank()) {
            dishes
        } else {
            dishes.filter { dish ->
                dish.name.trim().lowercase()
                    .contains(searchQuery.trim().lowercase())
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(24.dp),
        title = {
            Column {
                Text(
                    text = "Выберите блюдо",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Поиск блюд") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }
        },
        text = {
            if(filteredDishes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ничего не найдено",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn {
                    items(filteredDishes) { dish ->
                        Button(
                            onClick = { onSelect(dish) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        ) {
                            Text(
                                text = dish.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Отмена")
            }
        }
    )
}


@Composable
fun MenuItemRemoveDialog(
    dishName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp),
        title = {
            Text(
                text = "Подтвердите удаление",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text("Вы уверены, что хотите убрать блюдо ${dishName}?")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Нет")
            }
        }
    )
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

// Форматирование даты в строку
private fun LocalDate.formatDate(): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return formatter.format(
        Date(
            year - 1900,
            monthNumber - 1,
            dayOfMonth
        )
    )
}