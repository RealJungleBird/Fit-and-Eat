package su.junglebird.fiteat.data.repository

import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import su.junglebird.fiteat.data.database.dao.UserInfoDAO
import su.junglebird.fiteat.data.database.entities.UserInfo
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
    private val dao: UserInfoDAO
) {
    val userInfo: Flow<UserInfo?> = dao.getUserInfo()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if(dao.getUserInfo().first() == null) {
                insertDefaultUser()
            }
        }
    }

    private suspend fun insertDefaultUser() {
        val defaultUser = UserInfo(
            gender = false,
            dateOfBirth = "1970-01-01",
            height = 170f,
            mass = 60f
        )
        dao.insert(defaultUser)
    }

    suspend fun updateUserInfo(userInfo: UserInfo) {
        dao.insert(userInfo)
    }
}