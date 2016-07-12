package me.jimmyshaw.jsfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityMain extends AppCompatActivity {

    public static final String EXERCISE_ANAEROBIC = "Anaerobic";
    public static final String EXERCISE_AEROBIC = "Aerobic";
    public static final String EXERCISE_OTHER = "Other";

    @OnClick({R.id.relative_layout_anaerobic, R.id.relative_layout_aerobic, R.id.relative_layout_other})
    public void processOnClick(ViewGroup layout) {
        loadDetailActivity(layout.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void loadDetailActivity(int layoutId) {

        String stringExtra;

        switch (layoutId) {
            case R.id.relative_layout_anaerobic:
                stringExtra = EXERCISE_ANAEROBIC;
                break;
            case R.id.relative_layout_aerobic:
                stringExtra = EXERCISE_AEROBIC;
                break;
            case R.id.relative_layout_other:
                stringExtra = EXERCISE_OTHER;
                break;
            default:
                stringExtra = EXERCISE_OTHER;
                break;
        }

        ActivityDetails.newIntent(this, stringExtra);
    }
}
