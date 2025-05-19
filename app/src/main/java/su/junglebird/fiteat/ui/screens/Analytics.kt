package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Analytics() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Аналитика",
                        fontWeight = FontWeight.Black
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
        ) {

        }
    }
}