package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun DaySummary() {
    Column {
        Text(text = "Всего употреблено:")
        Text(text="168" + " Ккал",
            fontSize = 35.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DaySummary()
}