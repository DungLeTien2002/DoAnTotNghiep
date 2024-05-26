package com.fansipan.habit.tracker.mood.screen.setting.app

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseFragment
import com.fansipan.habit.tracker.mood.databinding.FragmentSettingBinding
import com.fansipan.habit.tracker.mood.screen.addbean.DialogPickTimeSleep
import com.fansipan.habit.tracker.mood.screen.premium.PremiumActivity
import com.fansipan.habit.tracker.mood.screen.sync.SyncDataActivity
import com.fansipan.habit.tracker.mood.utils.CalendarUtil
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.Define
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.isSdkR
import com.fansipan.habit.tracker.mood.utils.navigateToMarket
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.sendEmail
import com.fansipan.habit.tracker.mood.utils.setDarkMode
import com.fansipan.habit.tracker.mood.utils.setFullScreenMode
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.shareApp
import com.fansipan.habit.tracker.mood.utils.showToast
import com.fansipan.habit.tracker.mood.utils.trackingEvent

class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val dialogPickTimeSleep by lazy {
        DialogPickTimeSleep(requireContext())
    }

    private var currentHourSleep = 0
    private var currentMinutesSleep = 0

    override fun initView() {
        binding.btnPremiumPass.tvButtonDone.text = getString(R.string.premium_pass)
//        if (SharePrefUtils.isBought()) {
//            binding.tvInfoPremium.text = getString(R.string.txt_premium_version_congratulation)
//        } else {
//            binding.tvInfoPremium.text = getString(R.string.txt_should_subscribe_premium)
//        }
    }

    override fun onResume() {
        super.onResume()
        binding.layoutSyncLock.switchPinLock.isChecked = SharePrefUtils.isShowPasscode()
        binding.layoutSyncLock.switchFingerLock.isChecked = SharePrefUtils.isShowFingerprint()
        binding.layoutThemeScreen.switchFullScreen.isChecked = SharePrefUtils.isFullScreenMode()
        binding.imgBackground.setImageResource(SharePrefUtils.getBackgroundImageApp())
        binding.layoutThemeScreen.switchDarkMode.isChecked = SharePrefUtils.isDarkMode()
    }

    override fun initData() {

    }

    override fun initListener() {
//        binding.cardInfoPremium.setOnSafeClick {
//            openActivity(PremiumActivity::class.java)
//        }
        binding.btnPremiumPass.layoutButtonDone.setOnSafeClick {
            openActivity(PremiumActivity::class.java)
        }
        binding.switchRemainder.setOnCheckedChangeListener { compoundButton, isChecked ->
            setUpLayoutRemainder(isChecked)
        }
        binding.layoutTimeRemainder.setOnSafeClick {
            showDialogPickTimeRemainder()
        }
//        binding.layoutSyncLock.layoutSyncData.setOnSafeClick {
//            Define.CLICK_SYNC_DATA_SETTING.trackingEvent()
//            if (SharePrefUtils.isBought()) {
//                openActivity(SyncDataActivity::class.java)
//            } else {
//                showToast("Premium feature")
//            }
//        }
        binding.layoutSyncLock.switchPinLock.setOnSafeClick {
            val isChecked = binding.layoutSyncLock.switchPinLock.isChecked
            if (!isChecked) {
                SharePrefUtils.setIsShowPasscode(false)
            }
            if (isChecked) {
                openActivity(PassCodeActivity::class.java)
            }
        }
        binding.layoutSyncLock.switchFingerLock.setOnSafeClick {
            val isChecked = binding.layoutSyncLock.switchFingerLock.isChecked
            if (isChecked) {
                if (checkDeviceHasFingerprint()) {
                    SharePrefUtils.setIsShowFingerprint(isChecked)
                } else {
                    SharePrefUtils.setIsShowFingerprint(false)
                }
            } else {
                SharePrefUtils.setIsShowFingerprint(false)
            }
        }
        binding.layoutThemeScreen.layoutScreenType.setOnSafeClick {
            Define.CLICK_THEME_SETTING.trackingEvent()
            openActivity(ChooseBackgroundImageActivity::class.java)
        }
        binding.layoutThemeScreen.switchDarkMode.setOnSafeClick {
            val isChecked = binding.layoutThemeScreen.switchDarkMode.isChecked
            SharePrefUtils.setIsDarkMode(isChecked)
            requireActivity().setDarkMode(isChecked)
        }
        binding.layoutThemeScreen.switchFullScreen.setOnSafeClick {
            val isChecked = binding.layoutThemeScreen.switchFullScreen.isChecked
            SharePrefUtils.setIsFullScreenMode(isChecked)
            requireActivity().setFullScreenMode(isChecked)
        }
//        binding.layoutShareReport.layoutShareApp.setOnSafeClick {
//            requireContext().shareApp()
//        }
//        binding.layoutShareReport.layoutSendReport.setOnSafeClick {
//            requireContext().sendEmail(getString(R.string.email_contact))
//        }
//        binding.layoutShareReport.layoutRateApp.setOnSafeClick {
//            //showDialogRating()
//            navigateToMarket()
//        }
    }

    private fun checkDeviceHasFingerprint(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(Constant.TAG, "App can authenticate using biometrics.")
                return true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d(Constant.TAG, "No biometric features available on this device.")
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d(Constant.TAG, "Biometric features are currently unavailable.")
                return false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                showToast(getString(R.string.txt_fingerprint_not_set))
                Log.d(Constant.TAG, "Device has fingerprint but not set.")
                // Prompts the user to create credentials that your app accepts.
                if (isSdkR()) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    startActivityForResult(enrollIntent, 1234)
                }
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {

            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {

            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {

            }
        }
        return false
    }

    private fun setUpLayoutRemainder(checked: Boolean) {
        SharePrefUtils.setIsShowNotification(checked)
//        binding.layoutTimeRemainder.showOrGone(checked)
        if (checked) {
            //TODO check api 33, request permission notification
        }
    }

    private fun showDialogPickTimeRemainder() {
        if (!dialogPickTimeSleep.isShowing()) {
            dialogPickTimeSleep.show(getString(R.string.pick_time_sleep_start)) { hour, minutes ->
                currentHourSleep = hour
                currentMinutesSleep = minutes
                binding.tvTimeRemainder.text = CalendarUtil.formatTime(hour, minutes)
            }
        }
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater, container, false)
    }
}