package su.junglebird.fiteat.ui.screens

import android.icu.util.Currency
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import su.junglebird.fiteat.viewmodels.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(viewModel: ProfileViewModel) {
    val userInfo by viewModel.userInfo.collectAsState(initial = null)

    var gender: Boolean = false
    var gender_name: String = if(gender) "Мужской" else "Женский"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Профиль",
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
            item {
                userInfo?.let { info ->
                    Text(text = "Формула Миффлина-Сан Жеора")
                    ParameterCard(
                        label = "Пол",
                        value = if(info.gender) "Мужской" else "Женский",
                        onClick = { gender = !gender }
                    )
                    ParameterCard(
                        label = "Дата рождения",
                        value = info.dateOfBirth,
                        onClick = {  }
                    )
                    ParameterCard(
                        "Рост",
                        "${info.height} см",
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun ParameterCard(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label
            )
            Text(
                text = label,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateEditDialog(
    currentDate: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val initialDate = parseData(currentDate, formatter) ?: Calendar.getInstance()

    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.timeInMillis
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("") },
        text = { DatePicker() }
    )
}