package com.example.mobile_app.model

class DataModel {
}

data class SiteObjectWithTasks(
    val id: Long,
    val name: String,
    val description: String,
    val tasks: List<TaskItem>
)

data class ActiveInterval(
    val from: String,
    val to: String
)

data class CompletionCriterion(
    val id: Int,
    val name: String,
    val description: String,
    val isCommentRequired: Boolean,
    val isPhotoProofRequired: Boolean,
    val isCompleted: Boolean,
    val comment: String?,
    val completedBy: Int?
)

data class TaskDetails(
    val id: Long,
    val description: String,
    val assignedWorkers: List<Int>,
    val status: String,
    val completionCriteria: Map<Int, CompletionCriterion>,
    val createdAt: String,
    val closedAt: String?,
    val closingReason: String?,
    val activeInterval: ActiveInterval,
    val locationId: Long
)

data class TaskItem(
    val id: Long,
    val name: String,
    val status: String
)