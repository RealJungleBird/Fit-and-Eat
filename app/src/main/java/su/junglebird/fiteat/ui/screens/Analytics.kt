package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Analytics() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Аналитика")}
            )
        }
    ) { padding ->
        Column {

        }
    }
}