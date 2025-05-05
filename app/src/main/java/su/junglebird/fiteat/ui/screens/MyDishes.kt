package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import su.junglebird.fiteat.data.CustomDish
import su.junglebird.fiteat.ui.MyDishesViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MyDishes(viewModel: MyDishesViewModel = hiltViewModel()) {
    val items by viewModel.items.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(items) { item ->
                EditableDishCard(
                    item = item,
                    onEditClick = { viewModel.updateEditedItem(it) }
                )
            }
        }

        FloatingActionButton(
            onClick = { viewModel.createNewItem() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    viewModel.editedItem?.let { item ->
        EditDishDialog(
            item = item,
            onDismiss = { viewModel.updateEditedItem(null) },
            onSave = { updatedItem ->



                viewModel.updateItem(updatedItem)}
        )
    }









//    val dishes = mutableListOf(
//        Dish("Кротовуха", "9999"),
//        Dish("Дубайский шоколад", "1337"),
//        Dish("Котлетки с пюрешкой", "2025")
//    )
//
//    LazyColumn(verticalArrangement = Arrangement.spacedBy(/*15*/0.dp)) {
//        itemsIndexed(dishes) { index, item ->
//            val currentItem = remember(item) {item}
//            var showDialog by remember { mutableStateOf(currentItem.isDialogShown) }
//
//            Card(
//                colors = CardDefaults.cardColors(
//                    containerColor = if(index % 2 == 0) Color(0xffdddddd) else Color(0xffffffff)
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(70.dp)
//                    .clickable { showDialog = true },
//
//                shape = RoundedCornerShape(0)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(PaddingValues(10.dp, 0.dp))
//                        .fillMaxWidth()
//                        .height(70.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(text = item.dishName,
//                        fontSize = 20.sp,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier
//                            .weight(1F)
//                            .padding(end = 4.dp))
//                    Text(text = "${item.dishCalories} Ккал",
//                        fontSize = 20.sp,
//                        modifier = Modifier
//                            .wrapContentWidth(Alignment.End))
//                }
//            }
//
//            if(showDialog) {
//                var editedDishName by remember(item) { mutableStateOf(item.dishName) }
//                var editedDishCalories by remember(item) { mutableStateOf(item.dishCalories) }
//
//                AlertDialog(
//                    onDismissRequest = {showDialog = false},
//                    confirmButton = {
//                        TextButton(onClick = {
//                            showDialog = false
//                            currentItem.isDialogShown = false
//                            currentItem.dishName = editedDishName
//                            currentItem.dishCalories = editedDishCalories
//                            //onItemUpdated(currentItem)
//                        }) { Text("Сохранить") }
//                    },
//                    title = {Text("Редактирование блюда")},
//                    text = {
//                        Column(
//                            verticalArrangement = Arrangement.spacedBy(10.dp)
//                        ) {
//                        TextField(
//                            maxLines = 1,
//                            value = editedDishName,
//                            onValueChange = {editedDishName = it}
//                        )
//                        TextField(
//                            maxLines = 1,
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            value = editedDishCalories,
//                            onValueChange = {editedDishCalories = it}
//                        )}
//                    }
//                )
//            }
//        }
//    }

//    Text(text=dishes.toString(), modifier = Modifier.padding(top = 250.dp))
}

data class Dish(var dishName: String, var dishCalories: String, var isDialogShown: Boolean = false) {}

@Composable
fun EditableDishCard(item: CustomDish, onEditClick: (CustomDish) -> Unit, isDarker: Boolean = false) {
    Card(
        colors = CardDefaults.cardColors(
            //containerColor = if(index % 2 == 0) Color(0xffdddddd) else Color(0xffffffff)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onEditClick(item) },

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
            Text(text = item.name,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1F)
                    .padding(end = 4.dp))
            Text(text = "${item.calories} Ккал",
                fontSize = 20.sp,
                modifier = Modifier
                    .wrapContentWidth(Alignment.End))
        }
    }

}

@Composable
fun EditDishDialog(
    item: CustomDish,
    onDismiss: () -> Unit,
    onSave: (CustomDish) -> Unit
) {
    var editedDishName by remember(item) { mutableStateOf(item.name) }
    var editedDishCalories by remember(item) { mutableStateOf(item.calories.toString()) }

    AlertDialog(
        title = {Text("Редактирование блюда")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = editedDishName,
                    onValueChange = {editedDishName = it},
//                    label = "Название блюда",
//                    maxLines = 1,
                )
//                TextField(
//                    maxLines = 1,
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                    value = editedDishCalories,
//                    onValueChange = {editedDishCalories = it}
/*                )*/}
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onSave(item.copy(name = editedDishName,
                    calories = editedDishCalories.toInt()))
            }) { Text("Сохранить") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDishes()
}