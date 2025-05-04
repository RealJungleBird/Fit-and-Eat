package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyDishes() {
    val dishes = listOf(
        Dish("Кротовуха", 9999),
        Dish("Дубайский шоколад", 1337),
        Dish("Котлетки с пюрешкой", 2025)
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(/*15*/0.dp)) {
        itemsIndexed(dishes) { index, dish ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if(index % 2 == 0) Color(0xffdddddd) else Color(0xffffffff)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clickable { },

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
                    Text(text = dish.dishName,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1F)
                            .padding(end = 4.dp))
                    Text(text = "${dish.dishCalories} Ккал",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End))
                }
            }
        }
    }
}

data class Dish(val dishName: String, val dishCalories: Int) {}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishClickedDialog() {
    val openDialog = remember { mutableStateOf(false) }

    BasicAlertDialog (
        onDismissRequest = {openDialog.value = false},
        content = {Text("fndkjfnskjdnfkjsndkjg")}
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDishes()
}