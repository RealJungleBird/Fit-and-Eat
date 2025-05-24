package su.junglebird.fiteat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlinx.datetime.yearsUntil
import su.junglebird.fiteat.viewmodels.ProfileViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(viewModel: ProfileViewModel = hiltViewModel()) {
    val userInfo by viewModel.userInfo.collectAsState(initial = null)

    var showDateDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showMassDialog by remember { mutableStateOf(false) }

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
        userInfo?.let { info ->

            var result = 10*info.mass + 6.25*info.height + 5*calculateAge(info.dateOfBirth) + if(info.gender) (+5) else (-161)

            LazyColumn(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
            ) {
                item {
                    Text(text = "Формула Миффлина-Сан Жеора")
                    Text(text = "Ваша дневная норма:")
                    Text(
                        text = "$result Ккал"
                    )
                    ParameterCard(
                        label = "Пол",
                        value = if (info.gender) "Мужской" else "Женский",
                        onClick = {
                            viewModel.updateUserInfo(info.copy(gender = !info.gender))
                        }
                    )
                    ParameterCard(
                        label = "Дата рождения",
                        value = info.dateOfBirth,
                        onClick = { showDateDialog = true }
                    )
                    ParameterCard(
                        label = "Рост",
                        value = "${info.height} см",
                        onClick = { showHeightDialog = true }
                    )
                    ParameterCard(
                        label = "Вес",
                        value = "${info.mass} кг",
                        onClick = { showMassDialog = true }
                    )
                }
            }

            if(showDateDialog) {
                BirthDatePickerDialog(
                    currentDate = info.dateOfBirth,
                    onDateSelected = { newDate ->
                        viewModel.updateUserInfo(info.copy(dateOfBirth = newDate))
                        showDateDialog = false
                    },
                    onDismiss = { showDateDialog = false }
                )
            }

            // Ввод роста
            if (showHeightDialog) {
                NumberInputDialog(
                    title = "Редактирование роста (см):",
                    initialValue = info.height.toString(),
                    onConfirm = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            viewModel.updateUserInfo(info.copy(height = it))
                        }
                        showHeightDialog = false
                    },
                    onDismiss = { showHeightDialog = false }
                )
            }

            // Ввод веса
            if (showMassDialog) {
                NumberInputDialog(
                    title = "Редактирование веса (кг):",
                    initialValue = info.mass.toString(),
                    onConfirm = { newValue ->
                        newValue.toFloatOrNull()?.let {
                            viewModel.updateUserInfo(info.copy(mass = it))
                        }
                        showMassDialog = false
                    },
                    onDismiss = { showMassDialog = false }
                )
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
            .clickable { onClick() }
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
                text = value,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDatePickerDialog(
    currentDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }
    val initialDate = try {
        LocalDate.parse(currentDate)
    } catch (e: Exception) {
        LocalDate.fromEpochDays(0)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val instant = Instant.fromEpochMilliseconds(it)
                    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                    onDateSelected(date.toString())
                }
            }) { Text("Oк") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun NumberInputDialog(
    title: String,
    initialValue: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputValue by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                maxLines = 1,
                value = inputValue,
                onValueChange = { inputValue = it.filter { c -> c.isDigit() } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(inputValue) },
                enabled = inputValue.isNotBlank()
            ) { Text("Ок") }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) { Text("Отмена") }
        }
    )
}

private fun calculateAge(birthDateString: String): Int {
    return try {
        val birthDate = LocalDate.parse(birthDateString)
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

        birthDate.yearsUntil(today)
    } catch (e: Exception) {
        -1
    }
}