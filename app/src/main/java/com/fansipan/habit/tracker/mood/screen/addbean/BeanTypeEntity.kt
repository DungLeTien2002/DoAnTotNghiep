package com.fansipan.habit.tracker.mood.screen.addbean

import com.fansipan.habit.tracker.mood.data.entity.BeanDefaultEmoji

data class BeanTypeEntity(
    var beanDefaultEmoji: BeanDefaultEmoji,
    var isSelected: Boolean = false
)
