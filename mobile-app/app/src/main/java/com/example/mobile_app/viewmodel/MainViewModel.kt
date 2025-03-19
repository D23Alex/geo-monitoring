package com.example.mobile_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.model.Event
import com.example.mobile_app.model.SystemState
import com.example.mobile_app.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = EventRepository()

    private val _systemState = MutableStateFlow<SystemState?>(null)
    val systemState: StateFlow<SystemState?> = _systemState

    fun loadSystemState() {
        viewModelScope.launch {
            val result = repository.getCurrentState()
            result.onSuccess { state ->
                _systemState.value = state
            }.onFailure {
                // Обработка ошибки (например, через уведомление или логирование)
            }
        }
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            val result = repository.sendEvent(event)
            result.onSuccess { response ->
                // Обработка успешного ответа (например, обновление UI или логирование)
            }.onFailure { error ->
                // Обработка ошибки
            }
        }
    }
}
