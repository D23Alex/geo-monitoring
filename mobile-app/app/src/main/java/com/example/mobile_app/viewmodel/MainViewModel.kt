package com.example.mobile_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_app.model.Event
import com.example.mobile_app.repository.EventRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
private val repository = EventRepository(application)

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
