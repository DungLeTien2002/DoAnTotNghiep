package com.fansipan.habit.tracker.mood

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.fansipan.habit.tracker.mood.data.database.BeanDatabase
import com.fansipan.habit.tracker.mood.data.database.BeanRepository
import com.fansipan.habit.tracker.mood.service.AlarmUtil
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.RemoteConfigUtil
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BeanDailyApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {
    //    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    companion object {
        public var CHECK_INTER_SHOW = false
        const val TAG = Constant.TAG
    }

    //    var allIcons: List<IconEntity> = mutableListOf()
//    var allBeans: List<BeanDailyEntity> = mutableListOf()
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { BeanDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BeanRepository(database.beanDao()) }

    override fun onCreate() {
        super.onCreate()
//        AdsSdk().initialize(this)
        registerActivityLifecycleCallbacks(this)
        SharePrefUtils.init(this)
        RemoteConfigUtil.init()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        appOpenAdManager = AppOpenAdManager()
        AlarmUtil.setIntervalAlarm(this, Constant.actions)
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onMoveToForeground() {
//        if (RemoteConfigUtil.isShowOpenAd && !SharePrefUtils.isBought()) {
//            if (!CHECK_INTER_SHOW) {
//                currentActivity?.let {
//                    if (it !is SplashActivity) {
//                        appOpenAdManager.showAdIfAvailable(it)
//                    } else {
//                        if (!Constant.isFirstOpenSplash) {
//                            Constant.isFirstOpenSplash.invert()
//                            appOpenAdManager.showAdIfAvailable(it)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
//        if (!CHECK_INTER_SHOW) {
//            appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
//        }
//    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
//        if (!appOpenAdManager.isShowingAd) {
//        }
            currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}