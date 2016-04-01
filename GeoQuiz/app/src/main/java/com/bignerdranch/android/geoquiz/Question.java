package com.bignerdranch.android.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswerShown;

    public Question(int textRedId, boolean answerTrue, boolean answerShown) {
        mTextResId = textRedId;
        mAnswerTrue = answerTrue;
        mAnswerShown = answerShown;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean wasAnswerShown() {
        return mAnswerShown;
    }

    public void setAnswerShown(boolean answerShown) {
        mAnswerShown = answerShown;
    }
}
