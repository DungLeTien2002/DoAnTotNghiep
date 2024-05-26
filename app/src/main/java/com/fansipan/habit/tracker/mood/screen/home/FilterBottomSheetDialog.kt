package com.fansipan.habit.tracker.mood.screen.home

import android.view.LayoutInflater
import com.fansipan.habit.tracker.mood.base.BaseBottomSheetDialog
import com.fansipan.habit.tracker.mood.data.entity.IconEntity
import com.fansipan.habit.tracker.mood.databinding.FilterBottomSheetDialogBinding
import com.fansipan.habit.tracker.mood.screen.setting.content.entity.IconBase
import com.fansipan.habit.tracker.mood.utils.DataUtils
import com.fansipan.habit.tracker.mood.utils.getDrawableIdByName
import com.fansipan.habit.tracker.mood.utils.setGridManager
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick

class FilterBottomSheetDialog(
    val listIcon: List<IconEntity>,
    private val isFilter: Boolean = false,
    private val iconSelectId: Int
) : BaseBottomSheetDialog<FilterBottomSheetDialogBinding>() {
    companion object {
        const val ICON_IN_ROW = 8
    }

    private val listIconAdapter by lazy {
        ListIconAdapterV2()
    }

    var onClickIcon: ((iconBase: IconBase, position: Int) -> Unit)? = null
    var onClickReset: (() -> Unit)? = null

    override

    fun initView() {
        binding.rcvListIcon.setGridManager(requireContext(), ICON_IN_ROW, listIconAdapter)
    }

    val dataList = mutableListOf<IconBase>()
    override fun initData() {
        dataList.clear()
        dataList.addAll(DataUtils.getBaseBeanType(requireContext()))
        dataList.addAll(listIcon.map {
            val url = requireContext().getDrawableIdByName(it.iconUrl ?: "")
            val urlOff = requireContext().getDrawableIdByName((it.iconUrl ?: "") + "_off")
            IconBase(it.iconName ?: "", url, urlOff, it.iconId)
        })
        if (isFilter) {
            val icon = dataList.find { it.iconId == iconSelectId }
            icon?.let {
                it.isSelected = true
                binding.tvNameFilter.text = it.name
            }
        }

        listIconAdapter.setDataList(dataList)
    }

    override fun initListener() {
        listIconAdapter.setOnClickItem { item, position ->
            item?.let {
                binding.tvNameFilter.text = item.name
                listIconAdapter.setSelectedItem(position)
                onClickIcon?.invoke(item, position)
            }
        }
        binding.btnReset.setOnSafeClick {
            onClickReset?.invoke()
        }
        binding.btnDone.setOnSafeClick { dismiss() }
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): FilterBottomSheetDialogBinding {
        return FilterBottomSheetDialogBinding.inflate(layoutInflater)
    }
}