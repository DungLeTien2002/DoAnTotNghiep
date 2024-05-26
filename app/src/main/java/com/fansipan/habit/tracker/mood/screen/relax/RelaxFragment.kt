package com.fansipan.habit.tracker.mood.screen.relax

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseFragment
import com.fansipan.habit.tracker.mood.data.database.BeanViewModel
import com.fansipan.habit.tracker.mood.databinding.FragmentRelaxBinding
import com.fansipan.habit.tracker.mood.databinding.LayoutPopupSelectTimeMusicBinding
import com.fansipan.habit.tracker.mood.screen.addbean.WatchAdsDialog
import com.fansipan.habit.tracker.mood.screen.home.MainActivity
import com.fansipan.habit.tracker.mood.screen.premium.PremiumActivity
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.DataUtils
import com.fansipan.habit.tracker.mood.utils.Define
import com.fansipan.habit.tracker.mood.utils.RemoteConfigUtil
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.setLinearLayoutManager
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.trackingEvent


class RelaxFragment : BaseFragment<FragmentRelaxBinding>() {
    companion object {
        const val TAG = Constant.TAG
        const val DEFAULT_MINUTES = 5
    }

    private var timeSet = DEFAULT_MINUTES
    private var isSelectTwoList = false

    private val viewModel: BeanViewModel by viewModels {
        Constant.getViewModelFactory(requireActivity().application)
    }

    private val adapterTriggerSound by lazy {
        RelaxSoundAdapter()
    }
    private val adapterAmbientSound by lazy {
        RelaxSoundAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.tvTimeSet.text = "$timeSet " + requireContext().getString(R.string.min)
        binding.rcvTriggerSound.setLinearLayoutManager(requireContext(), adapterTriggerSound, RecyclerView.HORIZONTAL)
        binding.rcvAmbientSound.setLinearLayoutManager(requireContext(), adapterAmbientSound, RecyclerView.HORIZONTAL)
        if (SharePrefUtils.isCustomBackgroundImage()) {
            binding.imgBackground.setImageResource(SharePrefUtils.getBackgroundImageApp())
        }
        checkSelectSound()
    }

    override fun initData() {
        adapterTriggerSound.setDataList(DataUtils.getTriggerSound(requireContext()))
        adapterAmbientSound.setDataList(DataUtils.getAmbientSound(requireContext()))
    }

    override fun initListener() {
        adapterTriggerSound.setOnClickItem { item, position ->
            if (position > 2 && position != TriggerSound.values().size - 1 && !SharePrefUtils.isBought()) {
                openActivity(PremiumActivity::class.java)
            } else {
                adapterTriggerSound.setSelectedItem(position)
                checkSelectSound()
            }
        }
        adapterAmbientSound.setOnClickItem { item, position ->
            if (position > 2 && position != AmbientSound.values().size - 1 && !SharePrefUtils.isBought()) {
                openActivity(PremiumActivity::class.java)
            } else {
                adapterAmbientSound.setSelectedItem(position)
                checkSelectSound()
            }
        }
        binding.layoutSetTime.setOnSafeClick {
            showDialog()
        }
        binding.btnStartSound.setOnSafeClick {
            Define.CLICK_START_RELAX.trackingEvent()
            if (adapterTriggerSound.getIndexSelect() != -1 && adapterAmbientSound.getIndexSelect() != -1) {
                gotoPlayMusic()
            }
        }
    }

    private fun gotoPlayMusic() {
        val goToPlayMusic = {
            openActivity(
                MusicRelaxActivity::class.java, bundle = bundleOf(
                    Constant.TRIGGER_SOUND_SELECT to adapterTriggerSound.getIndexSelect(),
                    Constant.AMBIENT_SOUND_SELECT to adapterAmbientSound.getIndexSelect(),
                    Constant.TIME_COUNTDOWN to timeSet
                )
            )
        }
        val actionWatchAds = {
//            if (RemoteConfigUtil.isShowInter && !SharePrefUtils.isBought()) {
//                if (MaxUtilsV2.countStartMusicRelax >= 2) {
//                    MaxUtilsV2.countStartMusicRelax = 1
//                    MaxUtilsV2.getInstance(requireActivity()).setMaxUtilsListener {
//                        goToPlayMusic()
//                    }
//                    MaxUtilsV2.getInstance(requireActivity()).showInterstitialAd()
//                } else {
//                    MaxUtilsV2.countStartMusicRelax++
//                    goToPlayMusic()
//                }
//            } else {
//                goToPlayMusic()
//            }
            goToPlayMusic()
        }
        if (Constant.isShowDialogWatchAdsRelax && !SharePrefUtils.isBought() && RemoteConfigUtil.isShowInter) {
            WatchAdsDialog(requireContext()).also {
                it.show(
                    onClickWatchAds = {
                        Constant.isShowDialogWatchAdsRelax = false
                        actionWatchAds()
                    },
                    onClickBuyPremium = {
                        openActivity(PremiumActivity::class.java)
                    }
                )
            }
        } else {
            actionWatchAds()
        }
    }

    private fun checkSelectSound() {
        isSelectTwoList = adapterTriggerSound.isSelectSound() && adapterAmbientSound.isSelectSound()
        binding.btnStartSound.isEnabled = isSelectTwoList
        if (isSelectTwoList) {
            binding.btnStartSound.isEnabled = true
            binding.btnStartSound.setBackgroundResource(R.drawable.bg_button_relax)
            binding.tvStart.setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
        } else {
            binding.btnStartSound.isEnabled = false
            binding.btnStartSound.setBackgroundResource(R.drawable.bg_button_done_disable)
            binding.tvStart.setTextColor(ResourcesCompat.getColor(resources, R.color.grey_default_text_start, null))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog() {
        val dialogBinding = LayoutPopupSelectTimeMusicBinding.inflate(LayoutInflater.from(requireContext()))
        dialogBinding.minutesPicker.apply {
            minValue = 1
            maxValue = 30
            setFormatter {
                String.format("%02d", it)
            }
            value = timeSet
        }
        dialogBinding.minutesPicker.setOnValueChangedListener { numberPicker, old, new ->
            timeSet = new
            binding.tvTimeSet.text = "$timeSet " + requireContext().getString(R.string.min)
        }
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(dialogBinding.root, width, height, false)
        popupWindow.apply {
            isOutsideTouchable = true
            isFocusable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        popupWindow.showAsDropDown(binding.layoutSetTime, -250, -300)
        (requireActivity() as? MainActivity)?.setupLayer(true)
        popupWindow.setOnDismissListener {
            (requireActivity() as? MainActivity)?.setupLayer(false)
        }
        dialogBinding.minutesPicker.setOnSafeClick {
            popupWindow.dismiss()
        }
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentRelaxBinding {
        return FragmentRelaxBinding.inflate(inflater, container, false)
    }
}