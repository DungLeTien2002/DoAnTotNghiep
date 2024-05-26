package com.fansipan.habit.tracker.mood.screen.setting.content.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.habit.tracker.mood.base.BaseAdapterRecyclerView
import com.fansipan.habit.tracker.mood.databinding.LayoutSourceIconBinding
import com.fansipan.habit.tracker.mood.screen.setting.content.entity.IconBase

class ListIconAdapter : BaseAdapterRecyclerView<IconBase, LayoutSourceIconBinding>() {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): LayoutSourceIconBinding {
        return LayoutSourceIconBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: LayoutSourceIconBinding, item: IconBase, position: Int) {
        binding.imgIconBean.setImageResource(item.sourceId)
    }
}