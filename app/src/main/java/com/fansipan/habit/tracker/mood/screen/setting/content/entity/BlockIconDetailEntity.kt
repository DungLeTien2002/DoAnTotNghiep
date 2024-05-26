package com.fansipan.habit.tracker.mood.screen.setting.content.entity

import com.fansipan.habit.tracker.mood.data.entity.BlockEmojiEntity
import com.fansipan.habit.tracker.mood.data.entity.IconEntity

data class BlockIconDetailEntity(
    val blockEmojiEntity: BlockEmojiEntity,
    val listIcon: List<IconEntity>,
    var isVisibility: Boolean = false
)