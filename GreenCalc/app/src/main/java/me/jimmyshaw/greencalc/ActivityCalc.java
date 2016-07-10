package me.jimmyshaw.greencalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityCalc extends Activity {

    // Use Bufferknife to bind all our views.
    @BindView(R.id.text_view_results)
    TextView mTextViewResults;

    @BindView(R.id.image_button_equal)
    ImageButton mImageButtonEqual;

    @BindView(R.id.image_button_divide)
    ImageButton mImageButtonDivide;
    @BindView(R.id.image_button_multiply)
    ImageButton mImageButtonMultiply;
    @BindView(R.id.image_button_subtract)
    ImageButton mImageButtonSubtract;
    @BindView(R.id.image_button_add)
    ImageButton mImageButtonAdd;

    @BindView(R.id.button_nine)
    Button mButtonNine;
    @BindView(R.id.button_eight)
    Button mButtonEight;
    @BindView(R.id.button_seven)
    Button mButtonSeven;
    @BindView(R.id.button_six)
    Button mButtonSix;
    @BindView(R.id.button_five)
    Button mButtonFive;
    @BindView(R.id.button_four)
    Button mButtonFour;
    @BindView(R.id.button_three)
    Button mButtonThree;
    @BindView(R.id.button_two)
    Button mButtonTwo;
    @BindView(R.id.button_one)
    Button mButtonOne;
    @BindView(R.id.button_zero)
    Button mButtonZero;

    @BindView(R.id.button_decimal)
    Button mButtonDecimal;

    // Here's the running string concatenation of the user's number presses.
    private String mRunningNumberString = "";
    private String mLeftSideNumberString = "0.0";
    private String mRightSideNumberString = "0.0";

    private double mResult = 0.0;

    private Operation mCurrentOperation;

    @OnClick({R.id.button_nine, R.id.button_eight, R.id.button_seven,
            R.id.button_six, R.id.button_five, R.id.button_four,
            R.id.button_three, R.id.button_two, R.id.button_one, R.id.button_zero})
    public void processNumberClick(Button numberButton) {
        int number = 0;

        switch (numberButton.getText().toString()) {
            case "9":
                number = 9;
                break;
            case "8":
                number = 8;
                break;
            case "7":
                number = 7;
                break;
            case "6":
                number = 6;
                break;
            case "5":
                number = 5;
                break;
            case "4":
                number = 4;
                break;
            case "3":
                number = 3;
                break;
            case "2":
                number = 2;
                break;
            case "1":
                number = 1;
                break;
            default:
                break;
        }
        onNumberPress(number);
    }

    @OnClick({R.id.image_button_divide, R.id.image_button_multiply, R.id.image_button_subtract, R.id.image_button_add})
    public void processOperationClick(ImageButton operationButton) {
        Operation operation = Operation.ADD;

        switch (operationButton.getId()) {
            case R.id.image_button_divide:
                operation = Operation.DIVIDE;
                break;
            case R.id.image_button_multiply:
                operation = Operation.MULTIPLY;
                break;
            case R.id.image_button_subtract:
                operation = Operation.SUBTRACT;
                break;
            case R.id.image_button_add:
                operation = Operation.ADD;
                break;
            default:
                break;
        }

        executeOperation(operation);
    }

    @OnClick(R.id.button_decimal)
    public void processDecimalClick() {
        if (mRunningNumberString.contains(".")) {
            Toast.makeText(ActivityCalc.this, "Cannot have multiple decimals", Toast.LENGTH_SHORT).show();
        }
        else {
            mRunningNumberString += ".";
            mTextViewResults.setText(mRunningNumberString);
        }
    }

    @OnClick(R.id.button_clear)
    public void processClearClick() {
        onClearPress();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        // We must bind our activity to Butterknife in order for all data bindings to work.
        ButterKnife.bind(this);

        // The default text shown every time our app launches is 0.
        mTextViewResults.setText("0");

    }

    private void onNumberPress(int number) {
        mRunningNumberString += String.valueOf(number);
        mTextViewResults.setText(mRunningNumberString);
    }

    private void onClearPress() {
        mRunningNumberString = "";
        mTextViewResults.setText(mRunningNumberString);
    }

    private void executeOperation(Operation incomingOperation) {

        if (mCurrentOperation != null) {
            if (mRunningNumberString != "") {
                mRightSideNumberString = mRunningNumberString;
                mRunningNumberString = "";

                Double leftSideNumberDouble = Double.parseDouble(mLeftSideNumberString);
                Double rightSideNumberDouble = Double.parseDouble(mRightSideNumberString);

                switch (mCurrentOperation) {
                    case DIVIDE:
                        if (rightSideNumberDouble != 0.0) {
                            mResult = leftSideNumberDouble / rightSideNumberDouble;
                        }
                        else {
                            onClearPress();
                            Toast.makeText(ActivityCalc.this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MULTIPLY:
                        mResult = leftSideNumberDouble * rightSideNumberDouble;
                        break;
                    case SUBTRACT:
                        mResult = leftSideNumberDouble - rightSideNumberDouble;
                        break;
                    case ADD:
                        mResult = leftSideNumberDouble + rightSideNumberDouble;
                        break;
                }

                mLeftSideNumberString = String.valueOf(mResult);
                mTextViewResults.setText(mLeftSideNumberString);
            }
        }
        else {
            mLeftSideNumberString = mRunningNumberString;
            mRunningNumberString = "";
        }

        mCurrentOperation = incomingOperation;
    }
}
