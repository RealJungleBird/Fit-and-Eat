package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyDishes() {
    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Text(text = "Мои блюда", fontSize = 30.sp)
        Row(modifier = Modifier.fillMaxWidth().height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text="Блюдо1", fontSize = 20.sp)
            Text(text="777 Ккал", fontSize = 20.sp)
        }
        Row(modifier = Modifier.fillMaxWidth().height(40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text="Блюдо2", fontSize = 20.sp)
            Text(text="7357 Ккал", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDishes()
}