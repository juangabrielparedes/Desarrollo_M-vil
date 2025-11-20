package com.example.serviciocomputadoras.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciocomputadoras.data.repository.AuthRepository
import com.example.serviciocomputadoras.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)

            when (val result = repository.login(email, password)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState(isSuccess = true)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthState(error = result.message)
                }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)

            when (val result = repository.register(email, password)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState(isSuccess = true)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthState(error = result.message)
                }
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)

            when (val result = repository.resetPassword(email)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState(isSuccess = true)
                }
                is AuthResult.Error -> {
                    _authState.value = AuthState(error = result.message)
                }
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    fun resetState() {
        _authState.value = AuthState()
    }

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }
}