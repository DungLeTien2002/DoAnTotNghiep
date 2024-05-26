package com.fansipan.habit.tracker.mood.screen.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseFragment
import com.fansipan.habit.tracker.mood.data.database.BeanViewModel
import com.fansipan.habit.tracker.mood.data.entity.BeanDailyEntity
import com.fansipan.habit.tracker.mood.data.entity.IconEntity
import com.fansipan.habit.tracker.mood.databinding.FragmentHomeBinding
import com.fansipan.habit.tracker.mood.screen.home.calendar.CalendarBean
import com.fansipan.habit.tracker.mood.screen.home.calendar.CalendarFragmentV2
import com.fansipan.habit.tracker.mood.screen.home.calendar.CalendarViewPager
import com.fansipan.habit.tracker.mood.screen.home.share.ShareImageActivity
import com.fansipan.habit.tracker.mood.screen.setting.content.entity.IconBase
import com.fansipan.habit.tracker.mood.utils.CalendarUtil
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.ShareImageUtil
import com.fansipan.habit.tracker.mood.utils.loadBitmapFromView
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import java.io.ByteArrayOutputStream

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        const val TAG = Constant.TAG
    }

    private var iconSelectId: Int = -10
    private var listBean: MutableList<BeanDailyEntity> = mutableListOf()
    private var listIcon: MutableList<IconEntity> = mutableListOf()

    private var focusPage = CalendarUtil.getMonthInt()
    private val viewModel: BeanViewModel by viewModels {
        Constant.getViewModelFactory(requireActivity().application)
    }
    private val dialogChooseTime by lazy {
        DialogChooseTime(requireContext())
    }

    override fun initView() {
    }

    private lateinit var viewPagerAdapter: CalendarViewPager
    var listCalendarBean = mutableListOf<CalendarBean>()

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val currentYear = CalendarUtil.getYearInt()
        getPageWithYear(currentYear)
        focusPage = listCalendarBean.indexOfFirst { it.year == currentYear && it.month == CalendarUtil.getMonthInt() + 1 }
        viewModel.allIcons.observe(this) {
            listIcon.clear()
            listIcon.addAll(it)
        }
        viewModel.allBeans.observe(this) {
            listBean.clear()
            listBean.addAll(it)
        }

        binding.tvTitleTime.text = CalendarUtil.getMonthString(requireContext(), CalendarUtil.getMonthInt()) + " " + CalendarUtil
            .getYearInt()
        with(binding) {
            val list = listOf(tv1, tv2, tv3, tv4, tv5, tv6, tv7)
            val title = CalendarUtil.DayOfWeek.getWeekTitleShort()
            list.forEachIndexed { index, textView ->
                textView.text = getString(title[index])
            }
        }

        viewPagerAdapter = CalendarViewPager(
            listCalendarBean, childFragmentManager, lifecycle
        )
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.setCurrentItem(focusPage, false)
        if (Constant.isFilter) {
            binding.btnFilter.setImageResource(R.drawable.ic_filter)
            binding.toolbarIconBean.setImageResource(Constant.sourceId)
        }
    }

    private fun getPageWithYear(currentYear: Int) {
        val list = mutableListOf<CalendarBean>()
        for (year in (currentYear - 20)..(currentYear + 20)) {
            for (i in 1..12) {
                list.add(CalendarBean(i, year))
            }
        }
        listCalendarBean.addAll(0, list)
    }

    override fun initListener() {
        binding.layoutChooseTime.setOnSafeClick {
            showDialogChooseTime()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val actionNavigateMonth = {
                    setupTitle(position)
                    focusPage = position
                }
                actionNavigateMonth()
            }

            @SuppressLint("SetTextI18n")
            private fun setupTitle(position: Int) {
                val month = listCalendarBean[position].month
                val year = listCalendarBean[position].year
                binding.tvTitleTime.text = CalendarUtil.getMonthString(requireContext(), month - 1) + " " + year
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.layoutShare.setOnSafeClick {
            gotoShareScreen()
        }
        binding.btnShare.setOnSafeClick {
            gotoShareScreen()
        }
        binding.layoutEmojiFilter.setOnSafeClick {
            showDialogFilter()
        }
        binding.toolbarIconBean.setOnSafeClick {
            showDialogFilter()
        }
        binding.btnFilter.setOnSafeClick {
            showDialogFilter()
        }
    }

    private fun showDialogFilter() {
        FilterBottomSheetDialog(listIcon, Constant.isFilter, iconSelectId).also { dialog ->
            dialog.show(childFragmentManager, "")
            dialog.onClickIcon = { iconBase: IconBase, _: Int ->
                binding.btnFilter.setImageResource(R.drawable.ic_filter)
                binding.toolbarIconBean.setImageResource(iconBase.sourceId)
                filterByIconId(iconBase.iconId, iconBase.sourceId)
                setPagerCalendar()
            }
            dialog.onClickReset = {
                Constant.isFilter = false
                binding.btnFilter.setImageResource(R.drawable.ic_filter_blur)
                binding.toolbarIconBean.setImageResource(R.drawable.ic_logo_diary)
                dialog.dismiss()
                setPagerCalendar()
            }
        }
    }

    private fun setPagerCalendar() {
        childFragmentManager.fragments.forEach {
            if (it is CalendarFragmentV2) {
                it.updateData()
            }
        }
//        viewPagerAdapter = CalendarViewPager(listCalendarBean, childFragmentManager, lifecycle)
//        binding.viewPager.adapter = viewPagerAdapter
//        binding.viewPager.setCurrentItem(focusPage, true)
    }

    private fun filterByIconId(iconId: Int, sourceId: Int) {
        iconSelectId = iconId
        Constant.iconFilter = iconId
        Constant.sourceId = sourceId
        Constant.isFilter = true
    }

    private fun gotoShareScreen() {
        binding.layoutWeekTitle.loadBitmapFromView { toolbarBitmap ->
            val calendarFragment = childFragmentManager.fragments.last() as CalendarFragmentV2
            calendarFragment.exportCalendar { calendarBitmap ->
                val bitmapShare = ShareImageUtil.mergeBitmapsVertical(toolbarBitmap, calendarBitmap)
                val stream = ByteArrayOutputStream()
                bitmapShare.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()
                openActivity(
                    ShareImageActivity::class.java,
                    bundle = Bundle().apply {
                        putByteArray(Constant.IMAGE_SHARE, byteArray)
                    }
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogChooseTime() {
        if (!dialogChooseTime.isShowing()) {
            val timeSelectNow = binding.tvTitleTime.text.toString()
            dialogChooseTime.show(
                currentYear = timeSelectNow.substring(timeSelectNow.length - 4),
                timeSelectNow.substring(0, timeSelectNow.length - 5),
                true, onClickSubmit = {
                    //TODO: check monthInt to set title and current page
                    val month = it.monthInt
                    val year = it.year
                    binding.tvTitleTime.text = "${it.month} ${it.year}"
                    val index = listCalendarBean.indexOfFirst { item -> item.month == month && item.year == year }
                    binding.viewPager.currentItem = index
                }
            )
        }
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }
}