package me.jimmyshaw.simplequiz

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.btn_true)
        falseButton = findViewById(R.id.btn_false)

        trueButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.correct_toast,
                Toast.LENGTH_SHORT)
                .show()
        }

        falseButton.setOnClickListener {
            Toast.makeText(
                this,
                R.string.wrong_toast,
                Toast.LENGTH_SHORT)
                .show()
        }
    }
}