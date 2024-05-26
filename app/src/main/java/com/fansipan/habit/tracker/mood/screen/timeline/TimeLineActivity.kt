package com.fansipan.habit.tracker.mood.screen.timeline

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseActivity
import com.fansipan.habit.tracker.mood.base.NeedPermissionDialog
import com.fansipan.habit.tracker.mood.data.database.BeanViewModel
import com.fansipan.habit.tracker.mood.data.entity.BeanDailyEntity
import com.fansipan.habit.tracker.mood.data.entity.BeanIconEntity
import com.fansipan.habit.tracker.mood.data.entity.BeanImageAttachEntity
import com.fansipan.habit.tracker.mood.data.entity.IconEntity
import com.fansipan.habit.tracker.mood.databinding.ActivityTimeLineBinding
import com.fansipan.habit.tracker.mood.screen.addbean.AddBeanActivity
import com.fansipan.habit.tracker.mood.screen.addbean.DialogPickTimeSleep
import com.fansipan.habit.tracker.mood.screen.home.share.ShareImageActivity
import com.fansipan.habit.tracker.mood.screen.setting.content.dialog.DialogConfirmRemoveIcon
import com.fansipan.habit.tracker.mood.utils.CalendarUtil
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.DataUtils
import com.fansipan.habit.tracker.mood.utils.RemoteConfigUtil
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.checkReadImagePermission
import com.fansipan.habit.tracker.mood.utils.getActivityResultLauncher
import com.fansipan.habit.tracker.mood.utils.loadBitmapFromView
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.requestPermissionReadStorage
import com.fansipan.habit.tracker.mood.utils.setFullScreenMode
import com.fansipan.habit.tracker.mood.utils.setGridManager
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.showOrGone
import com.fansipan.habit.tracker.mood.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

class TimeLineActivity : BaseActivity<ActivityTimeLineBinding>() {
    companion object {
        const val TAG = Constant.TAG

        enum class SortType {
            Asc,
            Des
        }
    }

    private val adapter by lazy {
        TimeLineAdapterV2()
    }

    private val viewModel: BeanViewModel by viewModels {
        Constant.getViewModelFactory(application)
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false

    //    private val timeLineAdapter by lazy {
//        TimeLineAdapter()
//    }
    private val dialogPickTime by lazy {
        DialogPickTimeSleep(this)
    }
    private val dialogConfirmRemoveRecord by lazy {
        DialogConfirmRemoveIcon(this)
    }

    /** list data */
    private var listIcon: MutableList<IconEntity> = mutableListOf()
    private var listBean: MutableList<BeanDailyEntity> = mutableListOf()
    private var listBeanIcon: MutableList<BeanIconEntity> = mutableListOf()
    private var listBeanImageAttach: MutableList<BeanImageAttachEntity> = mutableListOf()

    private var sortType = SortType.Des
    private var currentMonth = CalendarUtil.getMonthInt() + 1
    private var currentYear = CalendarUtil.getYearInt()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setFullScreenMode(SharePrefUtils.isFullScreenMode())
//        Constant.mapNativeAd.clear()
        if (SharePrefUtils.isCustomBackgroundImage()) {
            binding.root.setBackgroundResource(SharePrefUtils.getBackgroundImageApp())
        }
        binding.rcvBeanDaily.setGridManager(this, 1, adapter = adapter)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        binding.tvTitleTime.text =
            CalendarUtil.getMonthString(this, month) + " " + year
    }

    override fun initData() {
        isReadPermissionGranted = checkReadImagePermission()
        permissionLauncher = getActivityResultLauncher { permissions ->
            isReadPermissionGranted = checkReadImagePermission()
            if (isReadPermissionGranted) {
                showToast("Reload to update data")
            }
        }
        //observe live data
        viewModel.allIcons.observe(this) {
            listIcon.clear()
            listIcon.addAll(it)
            setDataBean(listBean)
        }
        viewModel.allBeanIcon.observe(this) {
            listBeanIcon.clear()
            listBeanIcon.addAll(it)
            setDataBean(listBean)
        }
        viewModel.allBeanImageAttach.observe(this) {
            listBeanImageAttach.clear()
            listBeanImageAttach.addAll(it)
            setDataBean(listBean)
        }
        viewModel.allBeans.observe(this) {
            listBean.clear()
            listBean.addAll(it.sortedByDescending { item -> item.day })
            setDataBean(listBean)
        }
    }

    private fun setDataBean(beans: MutableList<BeanDailyEntity>) {
        synchronized(this) {
            Log.d(
                TAG,
                "setDataBean: beans: ${beans.size} - icons: ${listIcon.size} - listBeanIcon: ${listBeanIcon.size} - " +
                        "image: ${listBeanImageAttach.size}"
            )
            val dataList = DataUtils.orderBeanDailyDetail(
                beans.filter { it.month == currentMonth && it.year == currentYear }.toMutableList(), listIcon, listBeanIcon,
                listBeanImageAttach
            )
            val isHaveImage = dataList.any {
                it.listImageAttach.isNotEmpty()
            }
            if (isHaveImage) {
                //need permission read storage to view image
                if (!isReadPermissionGranted) {
                    if (Constant.isShowDialogNeedPermission) {
                        showDialogNeedPermission()
                    }
                }
            }
            executeAndSetDataListView(dataList)
            binding.tvNoRecord.showOrGone(dataList.isEmpty())
        }
    }

    private fun showDialogNeedPermission() {
        NeedPermissionDialog(this).also { dialog ->
            dialog.show(onClickDone = {
                requestPermissionReadStorage(permissionLauncher)
            }, onCLickCLose = {
                Constant.isShowDialogNeedPermission = false
            })
        }
    }

    override fun initListener() {
        binding.layoutBtnBack.setOnSafeClick { onBack() }
        binding.btnBack.setOnSafeClick { onBack() }
        binding.layoutShort.setOnSafeClick {
            reverseTypeSort()
            setUpViewShort()
            sortDataList()
        }
        binding.layoutChooseTime.setOnSafeClick {
            showDialogChooseTime()
        }
        adapter.onClickEditBean = {
            goToEditBean(it.beanDailyEntity)
        }
        adapter.onClickRemoveBean = {
            showDialogRemoveBean(it.beanDailyEntity)
        }
        adapter.onClickShareBean = { itemView, data ->
            gotoShareScreen(itemView)
        }
    }

    private fun gotoShareScreen(itemView: View) {
        itemView.loadBitmapFromView { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            openActivity(
                ShareImageActivity::class.java,
                bundle = Bundle().apply {
                    putByteArray(Constant.IMAGE_SHARE, byteArray)
                }
            )
        }
    }

    private fun showDialogRemoveBean(beanDailyEntity: BeanDailyEntity) {
        if (!dialogConfirmRemoveRecord.isShowing()) {
            dialogConfirmRemoveRecord.show(this.getString(R.string.do_you_want_remove_this_record)) {
                Log.d(com.fansipan.habit.tracker.mood.screen.home.calendar.TAG, "initListener: remove $beanDailyEntity")
                CoroutineScope(Dispatchers.IO).launch {
                    listBean.remove(beanDailyEntity)
                    viewModel.deleteBeanById(beanDailyEntity.beanId)
                    beanDailyEntity.beanIconId?.let { it -> viewModel.deleteBeanIconByBeanIconId(it) }
                }
            }
        }
    }

    private fun goToEditBean(beanDailyEntity: BeanDailyEntity) {
        openActivity(
            AddBeanActivity::class.java, bundle = bundleOf(
                Constant.TYPE to beanDailyEntity.beanTypeId,
                Constant.IS_ADD to false,
                Constant.BEAN_EDIT to beanDailyEntity,
                Constant.BEAN_DAY to beanDailyEntity.day,
                Constant.BEAN_MONTH to beanDailyEntity.month,
                Constant.BEAN_YEAR to beanDailyEntity.year
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogChooseTime() {
        if (!dialogPickTime.isShowing()) {
            dialogPickTime.showPickMonthYear(
                getString(R.string.pick_to_show_timeline),
                true, currentMonth, currentYear
            ) { month, year ->
                currentMonth = month
                currentYear = year
                binding.tvTitleTime.text = CalendarUtil.getMonthString(this, month - 1) + " " + currentYear
                setDataBean(listBean)
            }
        }
    }

    private fun reverseTypeSort() {
        sortType = if (sortType == SortType.Asc) {
            SortType.Des
        } else {
            SortType.Asc
        }
    }

    private fun setUpViewShort() {
        val backgroundShort: Int
        val imgShort: Int
        if (sortType == SortType.Asc) {
            backgroundShort = R.drawable.bg_radius_salmon_stroke_grey_shadow
            imgShort = R.drawable.ic_short
        } else {
            backgroundShort = R.drawable.bg_radius_grey_shadow
            imgShort = R.drawable.ic_un_short
        }
        binding.layoutShort.setBackgroundResource(backgroundShort)
        binding.imgShort.setImageResource(imgShort)
    }

    private fun sortDataList() {
        when (sortType) {
            SortType.Asc -> listBean.sortBy { it.day }
            SortType.Des -> listBean.sortByDescending { it.day }
        }
        setDataBean(listBean)
        binding.rcvBeanDaily.smoothScrollToPosition(0)
    }

    private fun executeAndSetDataListView(list: List<BeanIconDetailEntity>) {
        if (!RemoteConfigUtil.isShowNative || SharePrefUtils.isBought()) {
            adapter.setDataList(list)
            return
        }
        val newList = mutableListOf<BeanIconDetailEntity>()
        var indexAds = 5
        list.forEachIndexed { index, message ->
            if (index == indexAds) {
                indexAds += 5
                newList.add(BeanIconDetailEntity(BeanDailyEntity(), mutableListOf(), mutableListOf(), true))
                newList.add(message)
            } else {
                newList.add(message)
            }
        }
        adapter.setDataList(newList)
    }

    private fun onBack() {
        finish()
    }

    override fun onBackPressed() {
        onBack()
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityTimeLineBinding {
        return ActivityTimeLineBinding.inflate(inflater)
    }
}