package com.fansipan.habit.tracker.mood.screen.splash

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseActivity
import com.fansipan.habit.tracker.mood.data.database.BeanViewModel
import com.fansipan.habit.tracker.mood.databinding.ActivitySplashBinding
import com.fansipan.habit.tracker.mood.screen.home.MainActivity
import com.fansipan.habit.tracker.mood.screen.setting.app.Question
import com.fansipan.habit.tracker.mood.screen.setting.app.adapter.QuestionAdapter
import com.fansipan.habit.tracker.mood.screen.tutorial.TutorialActivity
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.gone
import com.fansipan.habit.tracker.mood.utils.openActivity
import com.fansipan.habit.tracker.mood.utils.setFullScreenMode
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.show
import com.fansipan.habit.tracker.mood.utils.showToast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    companion object {
        const val TIME_COUNT = 5L
        const val TAG = Constant.TAG
    }

    private var timeCountDown: Long = 0L
    private val viewModel: BeanViewModel by viewModels {
        Constant.getViewModelFactory(application)
    }
    private val isFirstOpen by lazy {
        SharePrefUtils.getBoolean(Constant.IS_FIRST_OPEN)
    }

    override fun initView() {
        setFullScreenMode(SharePrefUtils.isFullScreenMode())
    }

    override fun initData() {
        createTimer()
    }

    override fun initListener() {
        binding.number1.setOnClickListener { clickNumber(1) }
        binding.number2.setOnClickListener { clickNumber(2) }
        binding.number3.setOnClickListener { clickNumber(3) }
        binding.number4.setOnClickListener { clickNumber(4) }
        binding.number5.setOnClickListener { clickNumber(5) }
        binding.number6.setOnClickListener { clickNumber(6) }
        binding.number7.setOnClickListener { clickNumber(7) }
        binding.number8.setOnClickListener { clickNumber(8) }
        binding.number9.setOnClickListener { clickNumber(9) }
        binding.number0.setOnClickListener { clickNumber(0) }
        binding.imgDelete.setOnClickListener { clickNumber(-1) }
        binding.close.setOnSafeClick {
            binding.llPasscode.show()
            binding.layoutQuestions.gone()
        }
        binding.tvForgot.setOnSafeClick {
            binding.llPasscode.gone()
            binding.layoutQuestions.show()
            val recyclerView: RecyclerView = findViewById(R.id.listQuestion)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val listQuestion: List<Question> = questions()
            val listAnswer: Map<Int, String> = map()
            recyclerView.layoutManager = layoutManager
            val adapter = QuestionAdapter(listQuestion)
            recyclerView.adapter = adapter
            var listOption = adapter.getSelectedOptions()
            binding.btnSubmit.setOnSafeClick {
                clickSubmit(adapter, listOption, listAnswer)
            }
        }
    }

    private fun questions(): List<Question> {
        val gson = Gson()
        val json = SharePrefUtils.getString(Constant.QUESTION)
        val type = object : TypeToken<List<Question>>() {}.type
        val listQuestion: List<Question> = gson.fromJson(json, type) ?: emptyList()
        return listQuestion
    }

    private fun map(): Map<Int, String> {
        val gson1 = Gson()
        val json1 = SharePrefUtils.getString(Constant.ANSWER)
        val type1 = object : TypeToken<Map<Int, String>>() {}.type
        val listAnswer: Map<Int, String> = gson1.fromJson(json1, type1) ?: emptyMap()
        return listAnswer
    }

    private fun clickSubmit(
        adapter: QuestionAdapter,
        listOption: Map<Int, String>,
        listAnswer: Map<Int, String>
    ) {
        if (adapter.getSelectedOptions().size == 3) {
            if (listOption == listAnswer) {
                Toast.makeText(
                    this,
                    "Pass Code:" + SharePrefUtils.getPassCode(),
                    Toast.LENGTH_LONG
                ).show()
                startMain()
            } else {
                Toast.makeText(this, "The answer is not correct", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select all options", Toast.LENGTH_SHORT).show()
        }
    }

    inline fun <reified T> SharedPreferences.getObject(key: String, defaultValue: T? = null): T? {
        val json = this.getString(key, null)
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type) ?: defaultValue
    }

    private fun createTimer() {
        val countDownTimer: CountDownTimer = object : CountDownTimer(TIME_COUNT * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeCountDown = millisUntilFinished / 1000 + 1
            }

            override fun onFinish() {
                timeCountDown = 0
                startApp()
            }
        }
        countDownTimer.start()
    }

    private fun startApp() {
        if (!isFirstOpen) {
            openActivity(TutorialActivity::class.java, isFinish = true)
        } else {
            if (SharePrefUtils.isShowPasscode()) {
                showLayoutPasscode()
            } else {
                binding.llPasscode.gone()
                startMain()
            }
        }
    }

    private fun showLayoutPasscode() {
        binding.llPasscode.show()
    }


    private var passCode = ""
    private fun clickNumber(number: Int) {
        if (number != -1) {
            if (passCode.length == 4) {
                return
            }
            passCode += number
        } else {
            if (passCode.isNotEmpty()) {
                passCode = passCode.substring(0, passCode.length - 1)
            }
        }
        showPass(passCode)
        if (passCode.length == 4) {
            if (passCode == SharePrefUtils.getPassCode()) {
                startMain()
            } else {
                showToast(getString(R.string.passcode_incorrect))
                passCode = ""
                showPass(passCode)
            }
        }
    }

    private fun startMain() {
        openActivity(MainActivity::class.java, isFinish = true)
    }

    private fun showPass(passCode: String) {
        val passCodeLength = passCode.length
        val passcodeViews = arrayOf(
            binding.passcode1,
            binding.passcode2,
            binding.passcode3,
            binding.passcode4
        )

        for (i in passcodeViews.indices) {
            passcodeViews[i].setImageResource(
                if (i < passCodeLength) R.drawable.ic_round_radio_button_checked
                else R.drawable.ic_round_radio_button_unchecked
            )
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}