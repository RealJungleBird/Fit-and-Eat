package su.junglebird.fiteat.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.data.database.entities.DailyMenuItem
import su.junglebird.fiteat.viewmodels.DaySummaryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DaySummary(viewModel: DaySummaryViewModel = hiltViewModel()) {
    // состояния данных
    val dailyMenuItems by viewModel.dailyMenuItems.collectAsState() // список пунктов меню
    val dailyDishes by viewModel.dailyDishes.collectAsState() // список блюд для текущей даты
    val allDishes by viewModel.dishes.collectAsState() // все доступные блюда
    val totalCalories by viewModel.dailyCalories.collectAsState()
    var showSelector by remember { mutableStateOf(false) } // активен ли диалог выбора


    // основной макет экрана
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
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            Text("Статистика за $currentDate")
            Text(
                text = "Всего употреблено:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$totalCalories Ккал",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Меню на сегодня:",
                style = MaterialTheme.typography.titleMedium
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
            TextButton(
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