package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Profile() {
    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        Column {
            Text(text = "Профиль")
        }
    }
}