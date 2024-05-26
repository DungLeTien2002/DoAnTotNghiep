package com.fansipan.habit.tracker.mood.screen.tutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fansipan.habit.tracker.mood.base.BaseFragment
import com.fansipan.habit.tracker.mood.databinding.FragmentTutorialBinding
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.gone
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.show

class TutorialFragment : BaseFragment<FragmentTutorialBinding>() {
    private val step by lazy {
        arguments?.getInt(Constant.TUTORIAL_STEP, TutorialStep.One.step)
    }
    var onNextToStep2: (() -> Unit)? = null
    var onNextToStep3: (() -> Unit)? = null
    var onStartApp: (() -> Unit)? = null
    override fun initView() {
        when (step) {
            TutorialStep.One.step -> {
                setUpTitleStepOne()
            }

            TutorialStep.Two.step -> {
                setUpTitleStepTwo()
            }

            TutorialStep.Three.step -> {
                setUpTitleStepThree()
            }
        }
    }

    private fun setUpTitleStepThree() {
        binding.step1.root.gone()
        binding.step2.root.gone()
        binding.step3.root.show()
    }

    private fun setUpTitleStepTwo() {
        binding.step1.root.gone()
        binding.step2.root.show()
        binding.step3.root.gone()
    }

    private fun setUpTitleStepOne() {
        binding.step1.root.show()
        binding.step2.root.gone()
        binding.step3.root.gone()
    }

    override fun initData() {

    }

    override fun initListener() {
        binding.step1.apply {
            btnNextStep.setOnSafeClick {
                onNextToStep2?.invoke()
            }
            btnSkip.setOnSafeClick {
                onStartApp?.invoke()
            }
        }
        binding.step2.apply {
            btnNextStep.setOnSafeClick {
                onNextToStep3?.invoke()
            }
            btnSkip.setOnSafeClick {
                onStartApp?.invoke()
            }
        }
        binding.step3.btnNextStep.setOnSafeClick {
            onStartApp?.invoke()
        }
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentTutorialBinding {
        return FragmentTutorialBinding.inflate(inflater, container, false)
    }
}