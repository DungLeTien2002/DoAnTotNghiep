package com.fansipan.habit.tracker.mood.screen.addbean

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseAdapterRecyclerView
import com.fansipan.habit.tracker.mood.databinding.ItemLayoutChooseBeanBinding

class BeanTypeAdapter : BaseAdapterRecyclerView<BeanTypeEntity, ItemLayoutChooseBeanBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemLayoutChooseBeanBinding {
        return ItemLayoutChooseBeanBinding.inflate(inflater, parent, false)
    }

    override fun bindData(
        binding: ItemLayoutChooseBeanBinding,
        item: BeanTypeEntity,
        position: Int
    ) {
        binding.imgTypeBean.setImageResource(
            if (item.isSelected) {
                item.beanDefaultEmoji.icon

            } else {
                item.beanDefaultEmoji.iconOff
            }
        )

        binding.imgTypeBean.setBackgroundResource(
            if (item.isSelected) {
                R.drawable.bh_item_layout_choose_bean_selected
            } else {
                R.drawable.bh_item_layout_choose_bean
            }
        )


    }

    fun setSelectedItem(position: Int) {
        val indexSelected = dataList.indexOfFirst { it.isSelected }
        if (indexSelected != -1) {
            dataList[indexSelected].isSelected = false
            notifyItemChanged(indexSelected)
        }
        if (position == -1) {
            return
        }
        dataList[position].isSelected = true
        notifyItemChanged(position)
    }
}