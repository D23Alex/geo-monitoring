package com.example.mobile_app.debug

import com.example.mobile_app.model.ActiveInterval
import com.example.mobile_app.model.CompletionCriterion
import com.example.mobile_app.model.SiteObjectWithTasks
import com.example.mobile_app.model.SystemState
import com.example.mobile_app.model.TaskDetails
import com.example.mobile_app.model.TaskItem
import com.example.mobile_app.model.WorkerPositionUpdateEvent

object DebugData {
    val dummySystemState: SystemState = SystemState(
        lastEvent = WorkerPositionUpdateEvent(
            workerId = 0,
            latitude = 0.0,
            longitude = 0.0,
            timestamp = "2025-03-20T09:00:00Z"
        )
    )

    val dummySiteObject = SiteObjectWithTasks(
        id = 1001,
        name = "Объект А",
        description = "Главный склад на юге",
        tasks = listOf(
            TaskItem(id = 501, name = "Проверка дверей", status = "CREATED"),
            TaskItem(id = 502, name = "Инвентаризация оборудования", status = "IN_PROGRESS")
        )
    )

    val dummyTaskDetails = TaskDetails(
        id = 501,
        description = "Проверка дверей",
        assignedWorkers = listOf(2, 3),
        status = "IN_PROGRESS",
        completionCriteria = mapOf(
            1 to CompletionCriterion(
                id = 1,
                name = "Визуальный осмотр",
                description = "Проверка на наличие повреждений",
                isCommentRequired = false,
                isPhotoProofRequired = true,
                isCompleted = true,
                comment = "Все в порядке",
                completedBy = 2
            ),
            2 to CompletionCriterion(
                id = 2,
                name = "Проверка замков",
                description = "Тест на работоспособность всех замков",
                isCommentRequired = true,
                isPhotoProofRequired = false,
                isCompleted = false,
                comment = null,
                completedBy = null
            )
        ),
        createdAt = "2025-03-20T09:00:00Z",
        closedAt = null,
        closingReason = null,
        activeInterval = ActiveInterval(
            from = "2025-03-20T08:00:00Z",
            to = "2025-03-20T18:00:00Z"
        ),
        locationId = 1001
    )
}
