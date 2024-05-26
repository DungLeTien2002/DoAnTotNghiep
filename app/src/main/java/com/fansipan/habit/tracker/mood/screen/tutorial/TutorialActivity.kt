package com.fansipan.habit.tracker.mood.screen.tutorial

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseActivity
import com.fansipan.habit.tracker.mood.databinding.ActivityTutorialBinding
import com.fansipan.habit.tracker.mood.screen.home.MainActivity
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.requestNotifyPermission
import com.fansipan.habit.tracker.mood.utils.showToast

class TutorialActivity : BaseActivity<ActivityTutorialBinding>() {
    lateinit var viewPagerAdapter: ViewPagerTutorialAdapter
    private val fragmentList = mutableListOf<TutorialFragment>()
    private val tutorialStep1 by lazy {
        TutorialFragment().apply {
            arguments = bundleOf(Constant.TUTORIAL_STEP to TutorialStep.One.step)
        }
    }

    private val tutorialStep2 by lazy {
        TutorialFragment().apply {
            arguments = bundleOf(Constant.TUTORIAL_STEP to TutorialStep.Two.step)
        }
    }

    private val tutorialStep3 by lazy {
        TutorialFragment().apply {
            arguments = bundleOf(Constant.TUTORIAL_STEP to TutorialStep.Three.step)
        }
    }

    override fun initView() {
        if (SharePrefUtils.isFirstRequestNotification()) {
            requestNotifyPermission()
            SharePrefUtils.setFirstRequestNotification(false)
        }
    }

    override fun initData() {
        fragmentList.add(tutorialStep1)
        fragmentList.add(tutorialStep2)
        fragmentList.add(tutorialStep3)
        viewPagerAdapter = ViewPagerTutorialAdapter(supportFragmentManager, fragmentList)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.setCurrentItem(0, true)
    }

    override fun initListener() {
        tutorialStep1.apply {
            onNextToStep2 = {
                binding.viewPager.setCurrentItem(1, true)
            }
            onStartApp = {
                startApp()
            }
        }
        tutorialStep2.apply {
            onNextToStep3 = {
                binding.viewPager.setCurrentItem(2, true)
            }
            onStartApp = {
                startApp()
            }
        }
        tutorialStep3.onStartApp = {
            //because default key is false, then save true to check (reverse logic)
            startApp()
        }
    }

    private fun startApp() {
        SharePrefUtils.saveKey(Constant.IS_FIRST_OPEN, true)
        openActivity(MainActivity::class.java, isFinish = true)
//        if (SharePrefUtils.isBought()) {
//        } else {
//            openActivity(
//                PremiumActivity::class.java, isFinish = true, bundleOf(
//                    Constant.IS_FROM_START_APP to true
//                )
//            )
//        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityTutorialBinding {
        return ActivityTutorialBinding.inflate(inflater)
    }

    private var isClickBack = false

    private fun onBack() {
        if (isClickBack) {
            finish()
        } else {
            showToast(getString(R.string.click_back))
            isClickBack = true
            Handler(Looper.getMainLooper()).postDelayed({
                isClickBack = false
            }, 1000L)
        }
    }

    override fun onBackPressed() {
        onBack()
    }
}