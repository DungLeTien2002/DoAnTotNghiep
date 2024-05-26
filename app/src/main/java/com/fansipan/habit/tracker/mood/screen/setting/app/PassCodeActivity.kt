package com.fansipan.habit.tracker.mood.screen.setting.app

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.base.BaseActivity
import com.fansipan.habit.tracker.mood.databinding.LayoutLockBinding
import com.fansipan.habit.tracker.mood.screen.setting.app.adapter.QuestionAdapter
import com.fansipan.habit.tracker.mood.utils.Constant
import com.fansipan.habit.tracker.mood.utils.SharePrefUtils
import com.fansipan.habit.tracker.mood.utils.gone
import com.fansipan.habit.tracker.mood.utils.hide
import com.fansipan.habit.tracker.mood.utils.setOnSafeClick
import com.fansipan.habit.tracker.mood.utils.show
import com.fansipan.habit.tracker.mood.utils.showToast
import com.google.gson.Gson

class PassCodeActivity : BaseActivity<LayoutLockBinding>() {
    private var passCode = ""
    private var passCodeConfirm = ""


    private var randomQuestion = Constant.questions.shuffled().take(3)

    override fun initView() {
        val gson = Gson()
        val json = gson.toJson(randomQuestion)
        SharePrefUtils.saveKey(Constant.QUESTION,json)
    }

    override fun initData() {

    }

    private fun onBack() {
        SharePrefUtils.setIsShowPasscode(false)
        finish()
    }

    override fun initListener() {
        binding.btnExit.setOnClickListener {
            if (isConfirm) {
                passCode = ""
                passCodeConfirm = ""
                showPass(passCodeConfirm)
                binding.tvTitle.text = getString(R.string.enter_passcode)
                binding.tvNotCorrect.hide()
                isConfirm = false
                binding.btnExit.text = getString(R.string.txt_exit)
            } else {
                onBack()
            }
        }
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
            finish()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): LayoutLockBinding {
        return LayoutLockBinding.inflate(inflater)
    }

    private var isConfirm = false
    private fun clickNumber(number: Int) {
        if (!isConfirm) {
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
                isConfirm = true
                passCodeConfirm = ""
                showPass(passCodeConfirm)
                binding.tvTitle.text = getString(R.string.confirm_passcode)
                binding.btnExit.text = getString(R.string.txt_back_screen)
            }
        } else {
            if (number != -1) {
                if (passCodeConfirm.length == 4) {
                    return
                }
                passCodeConfirm += number
            } else {
                if (passCodeConfirm.isNotEmpty()) {
                    passCodeConfirm = passCodeConfirm.substring(0, passCodeConfirm.length - 1)
                }
            }
            showPass(passCodeConfirm)
            if (passCodeConfirm.length == 4) {
                if (passCode == passCodeConfirm) {

                    //finish()
                    showQuestions()
                } else {
                    binding.tvNotCorrect.show()
                    SharePrefUtils.setIsShowPasscode(false)
                    passCodeConfirm = ""
                    showPass(passCodeConfirm)
                    showToast(getString(R.string.passcode_not_match))
                }
            } else {
                binding.tvNotCorrect.hide()
            }
        }
    }

    private fun showQuestions() {
        binding.layoutPassword.gone()
        binding.layoutQuestions.show()
        val recyclerView: RecyclerView = findViewById(R.id.listQuestion)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = QuestionAdapter(randomQuestion)
        recyclerView.adapter = adapter
        binding.btnSubmit.setOnSafeClick {
            if (adapter.getSelectedOptions().size == 3) {
                val gson1 = Gson()
                val json1 = gson1.toJson(adapter.getSelectedOptions())
                SharePrefUtils.saveKey(Constant.ANSWER,json1)
                SharePrefUtils.setIsShowPasscode(true)
                SharePrefUtils.setPassCode(passCode)
                showToast(getString(R.string.set_passcode_success))
                finish()
            } else {
                showToast("Please select all options")
            }

        }
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

    override fun onBackPressed() {
    }
}