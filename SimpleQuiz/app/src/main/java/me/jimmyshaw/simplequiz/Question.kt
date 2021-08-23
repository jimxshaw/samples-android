package me.jimmyshaw.simplequiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)