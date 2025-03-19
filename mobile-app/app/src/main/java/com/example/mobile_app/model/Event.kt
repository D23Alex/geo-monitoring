package com.example.mobile_app.model

import com.google.gson.annotations.SerializedName


// Базовый тип для всех событий
sealed class Event {
    abstract val timestamp: String
    abstract val eventType: String
}

// Событие обновления позиции работника
data class WorkerPositionUpdateEvent(
    @SerializedName("workerId") val workerId: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "WorkerPositionUpdateEvent"
}

// Событие назначения задачи (создание задачи)
data class TaskAssignedEvent(
    @SerializedName("description") val description: String,
    @SerializedName("assignedWorkerIds") val assignedWorkerIds: Set<Long>,
    @SerializedName("completionCriteria") val completionCriteria: String,
    @SerializedName("locationId") val locationId: Long,
    @SerializedName("activeFrom") val activeFrom: String,
    @SerializedName("activeTo") val activeTo: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "TaskAssignedEvent"
}

// Событие завершения задачи
data class TaskCompletedEvent(
    @SerializedName("taskId") val taskId: Long,
    @SerializedName("closedAt") val closedAt: String,
    @SerializedName("closingReason") val closingReason: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "TaskCompletedEvent"
}

// Событие отмены задачи
data class TaskCancelledEvent(
    @SerializedName("taskId") val taskId: Long,
    @SerializedName("closedAt") val closedAt: String,
    @SerializedName("closingReason") val closingReason: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "TaskCancelledEvent"
}

// Событие регистрации внештатной ситуации
data class AbnormalSituationEvent(
    @SerializedName("workerId") val workerId: Long,
    @SerializedName("description") val description: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "AbnormalSituationEvent"
}

// Событие выполнения критерия задачи
data class CriteriaCompletedEvent(
    @SerializedName("taskId") val taskId: Long,
    @SerializedName("criteriaNumber") val criteriaNumber: Int,
    @SerializedName("completedAt") val completedAt: String,
    @SerializedName("completionComment") val completionComment: String,
    @SerializedName("completedBy") val completedBy: Long,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "CriteriaCompletedEvent"
}

// Событие запроса невыхода (Work Absence Request)
data class WorkAbsenceRequestEvent(
    @SerializedName("absenceFrom") val absenceFrom: String,
    @SerializedName("absenceTo") val absenceTo: String,
    @SerializedName("absenceReason") val absenceReason: String, // Например, "ILLNESS", "VACATION", etc.
    @SerializedName("reasonComment") val reasonComment: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "WorkAbsenceRequestEvent"
}

// Событие создания работника
data class WorkerCreationEvent(
    @SerializedName("workerName") val workerName: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "WorkerCreationEvent"
}

// Событие создания группы работников
data class WorkerGroupCreationEvent(
    @SerializedName("foremanId") val foremanId: Long,
    @SerializedName("workerIds") val workerIds: Set<Long>,
    @SerializedName("groupActiveFrom") val groupActiveFrom: String,
    @SerializedName("groupActiveTo") val groupActiveTo: String,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "WorkerGroupCreationEvent"
}

// Событие создания локации (объекта)
data class LocationCreationEvent(
    @SerializedName("name") val name: String,
    @SerializedName("points") val points: Set<Point>,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "LocationCreationEvent"
}

// Событие обновления локации
data class LocationUpdateEvent(
    @SerializedName("locationId") val locationId: Long,
    @SerializedName("points") val points: Set<Point>,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "LocationUpdateEvent"
}

// Событие удаления локации
data class LocationDeleteEvent(
    @SerializedName("locationId") val locationId: Long,
    @SerializedName("timestamp") override val timestamp: String
) : Event() {
    override val eventType: String = "LocationDeleteEvent"
}

// Простая модель для географической точки
data class Point(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)