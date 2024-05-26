package com.fansipan.habit.tracker.mood.screen.setting.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fansipan.habit.tracker.mood.R
import com.fansipan.habit.tracker.mood.screen.setting.app.Question

class QuestionAdapter(private val questions: List<Question>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    private val selectedOptions = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question_layout, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textQuestion: TextView = itemView.findViewById(R.id.textQuestion)
        private val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)

        fun bind(question: Question) {
            textQuestion.text = question.text
            setupRadioButtons(question.options)
        }

        private fun setupRadioButtons(options: List<String>) {
            radioGroup.removeAllViews()

            for ((index, option) in options.withIndex()) {
                val radioButton = RadioButton(itemView.context)
                radioButton.text = option
                radioButton.id = index
                radioGroup.addView(radioButton)
            }
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val radioButton = group.findViewById<RadioButton>(checkedId)
                radioButton?.let {
                    val selectedOption = it.text.toString()
                    // Lưu giá trị mà người dùng đã chọn
                    selectedOptions[adapterPosition] = selectedOption
                }
            }
        }
    }

    // Phương thức để lấy các giá trị đã chọn
    fun getSelectedOptions(): Map<Int, String> {
        return selectedOptions
    }
}

