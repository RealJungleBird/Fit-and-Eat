package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import su.junglebird.fiteat.data.database.entities.DailyDish

@Composable
fun DaySummary() {
    Column {
        Text(text = "Всего употреблено:")
        Text(text="168" + " Ккал",
            fontSize = 35.sp)

        Text(text="Меню на сегодня:")
        LazyColumn {  }
    }
}



@Composable
fun DishCard(
    dish: DailyDish,
    onRemove: (DailyDish) -> Unit
) {
    Card() {
        Row(
            modifier = Modifier
                .padding(PaddingValues(10.dp, 0.dp))
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "",
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1F).padding(end=4.dp))
        }
    }

}





@Preview(showBackground = true)
@Composable
fun Preview() {
    DaySummary()
}