package me.jimmyshaw.simplequiz

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_android, true),
        Question(R.string.question_animal, false),
        Question(R.string.question_earth, false),
        Question(R.string.question_tallest_building, true),
        Question(R.string.question_us, true)
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.btn_true)
        falseButton = findViewById(R.id.btn_false)
        nextButton = findViewById(R.id.btn_next)
        questionTextView = findViewById(R.id.tv_question)


        trueButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT
            )
                .show()
        }

        falseButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.wrong_toast,
                Toast.LENGTH_SHORT
            )
                .show()
        }

        val questionTextResId = questionBank[currentIndex].textResId

        questionTextView.setText(questionTextResId)
    }
}