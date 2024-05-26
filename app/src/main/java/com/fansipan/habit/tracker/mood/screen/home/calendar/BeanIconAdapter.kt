package com.fansipan.habit.tracker.mood.screen.home.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.habit.tracker.mood.base.BaseAdapterRecyclerView
import com.fansipan.habit.tracker.mood.data.entity.IconEntity
import com.fansipan.habit.tracker.mood.databinding.LayoutItemIconBeanBinding
import com.fansipan.habit.tracker.mood.utils.getDrawableIdByName

class BeanIconAdapter : BaseAdapterRecyclerView<IconEntity, LayoutItemIconBeanBinding>() {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): LayoutItemIconBeanBinding {
        return LayoutItemIconBeanBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: LayoutItemIconBeanBinding, item: IconEntity, position: Int) {
        binding.imgIconBean.setImageResource(binding.root.context.getDrawableIdByName(item.iconUrl ?: ""))
    }
}