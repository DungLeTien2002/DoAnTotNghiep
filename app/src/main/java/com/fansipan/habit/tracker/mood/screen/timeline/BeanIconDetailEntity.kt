package com.fansipan.habit.tracker.mood.screen.timeline

import com.fansipan.habit.tracker.mood.data.entity.BeanDailyEntity
import com.fansipan.habit.tracker.mood.data.entity.BeanImageAttachEntity
import com.fansipan.habit.tracker.mood.data.entity.IconEntity

data class BeanIconDetailEntity(
    val beanDailyEntity: BeanDailyEntity,
    val listIcon: List<IconEntity>,
    val listImageAttach: List<BeanImageAttachEntity>,
    var isAds: Boolean = false
)