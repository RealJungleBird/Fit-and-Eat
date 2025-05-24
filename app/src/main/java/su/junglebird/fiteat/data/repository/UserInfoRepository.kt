package su.junglebird.fiteat.data.repository

import kotlinx.coroutines.flow.Flow
import su.junglebird.fiteat.data.database.dao.UserInfoDAO
import su.junglebird.fiteat.data.database.entities.UserInfo
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
    private val dao: UserInfoDAO
) {
    val userInfo: Flow<UserInfo?> = dao.getUserInfo()

    suspend fun updateUserInfo(userInfo: UserInfo) {
        dao.insert(userInfo)
    }
}