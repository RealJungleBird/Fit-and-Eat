package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import su.junglebird.fiteat.data.database.entities.CustomDish
import su.junglebird.fiteat.viewmodels.MyDishesViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDishes(viewModel: MyDishesViewModel = hiltViewModel()) {
    val dishes by viewModel.dishes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои блюда") })
        }
    ) { padding ->

    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            items(dishes) { dish ->
                EditableDishCard(
                    dish = dish,
                    onEditClick = { viewModel.startEditing(it) },
                    onDelete = { viewModel.deleteDish(it) }
                )
            }
        }

        FloatingActionButton(
            onClick = { viewModel.createNewItem() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add dish")
        }
    }

    viewModel.editedDish?.let { dish ->
        EditDishDialog(
            dish = dish,
            onDismiss = { viewModel.startEditing(null) },
            onSave = { updatedDish -> viewModel.saveDish(updatedDish)}
        )
    }
}


@Composable
fun EditableDishCard(
    dish: CustomDish,
    onEditClick: (CustomDish) -> Unit,
    onDelete: (CustomDish) -> Unit,
    isDarker: Boolean = false
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(colors = CardDefaults.cardColors(
            //containerColor = if(index % 2 == 0) Color(0xffdddddd) else Color(0xffffffff)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),

        shape = RoundedCornerShape(0)
    ) {
        Row(
            modifier = Modifier
                .padding(PaddingValues(10.dp, 0.dp))
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = dish.name,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1F)
                    .padding(end = 4.dp))
            Text(text = "${dish.calories} Ккал",
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.wrapContentWidth(Alignment.End))
            IconButton(onClick = { onEditClick(dish) }) { Icon(Icons.Default.Edit, "Edit") }
            IconButton(
                onClick = { showDeleteDialog = true }
            ) {
                Icon(
                    Icons.Default.Delete,
                    "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if(showDeleteDialog) {
        DeleteDishDialog(
            dish = dish,
            onConfirm =  {
                onDelete(dish)
                showDeleteDialog = false
            },
            onDismiss =  { showDeleteDialog = false }
        )
    }

}

@Composable
fun EditDishDialog(
    dish: CustomDish,
    onDismiss: () -> Unit,
    onSave: (CustomDish) -> Unit
) {
    var editedDishName by remember(dish) { mutableStateOf(dish.name) }
    var editedDishCalories by remember(dish) { mutableStateOf(dish.calories.toString()) }

    AlertDialog(
        title = { Text(if(dish.id == 0L) "Создание блюда" else "Редактирование блюда") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    maxLines = 1,
                    value = editedDishName,
                    onValueChange = {editedDishName = it},
                    label = { Text("Название блюда") }
                )
                OutlinedTextField(
                    maxLines = 1,
                    value = editedDishCalories,
                    onValueChange = {editedDishCalories = it.filter { c -> c.isDigit() }},
                    label = {Text("Калории")},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onSave(dish.copy(name = editedDishName, calories = editedDishCalories.toIntOrNull() ?: 0)) },
                enabled = editedDishName.isNotBlank() && editedDishCalories.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}


@Composable
fun DeleteDishDialog(
    dish: CustomDish,
    onConfirm: (CustomDish) -> Unit,
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
            Text("Вы уверены, что хотите удалить блюдо ${dish.name}?")
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(dish) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Нет")
            }
        }
    )
}