package com.example.mobile_app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.model.Event
import com.example.mobile_app.model.SystemState
import com.example.mobile_app.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EventRepository(application)

    private val _systemState = MutableStateFlow<SystemState?>(null)
    val systemState: StateFlow<SystemState?> = _systemState

    fun loadSystemState() {
        viewModelScope.launch {
            repository.getCurrentState().onSuccess { state ->
                _systemState.value = state
            }.onFailure { error ->
                // Логируем ошибку и устанавливаем dummy state для отладки
                Log.e("MainViewModel", ": ${error.message}")
                _systemState.value = com.example.mobile_app.debug.DebugData.dummySystemState
            }
        }
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            repository.sendEvent(event).onSuccess { response ->
                // Обработка успешного ответа (например, обновление UI или логирование)
            }.onFailure { error ->
                // Обработка ошибки
            }
        }
    }

    // Функция для периодической попытки отправки неотправленных событий,
    // её можно вызвать, например, при запуске приложения или через WorkManager.
    fun retrySendingEvents() {
        viewModelScope.launch {
            repository.resendUnsentEvents()
        }
    }
}
