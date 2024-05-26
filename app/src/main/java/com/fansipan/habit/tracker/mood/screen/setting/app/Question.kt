package com.fansipan.habit.tracker.mood.screen.setting.app

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    var selectedAnswer: String? = null
)