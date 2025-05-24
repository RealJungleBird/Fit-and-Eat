package su.junglebird.fiteat.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import su.junglebird.fiteat.data.database.entities.UserInfo
import su.junglebird.fiteat.data.repository.UserInfoRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserInfoRepository
): ViewModel() {
    val userInfo = repository.userInfo

    fun updateUserInfo(updatedUserInfo: UserInfo) {
        viewModelScope.launch {
            repository.updateUserInfo(updatedUserInfo)
        }
    }
}