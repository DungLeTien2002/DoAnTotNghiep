package com.fansipan.habit.tracker.mood.screen.setting.app

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseAdapterRecyclerView
import com.fansipan.habit.tracker.mood.databinding.ItemBackgroundImageAppBinding
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.loadImage
import com.fansipan.habit.tracker.mood.utils.showOrGone

class ImageBackgroundAdapter : BaseAdapterRecyclerView<Int, ItemBackgroundImageAppBinding>() {
    override fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): ItemBackgroundImageAppBinding {
        return ItemBackgroundImageAppBinding.inflate(inflater, parent, false)
    }

    override fun bindData(binding: ItemBackgroundImageAppBinding, item: Int, position: Int) {
        binding.root.context.loadImage(binding.imgItemBackground, item, R.drawable.background_app_1)
        binding.icPremium.showOrGone(position > 1 && !SharePrefUtils.isBought())
    }
}