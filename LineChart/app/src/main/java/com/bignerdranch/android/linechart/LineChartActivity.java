package com.bignerdranch.android.linechart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class LineChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        // Here we create the list of entries.
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2f, 0));
        entries.add(new Entry(1f, 1));
        entries.add(new Entry(4f, 2));
        entries.add(new Entry(5f, 3));
        entries.add(new Entry(1f, 4));
        entries.add(new Entry(7f, 5));
        entries.add(new Entry(11f, 6));
        entries.add(new Entry(4f, 7));
        entries.add(new Entry(4f, 8));
        entries.add(new Entry(9f, 9));
        entries.add(new Entry(8f, 10));
        entries.add(new Entry(6f, 11));

        // What does each entry in the dataset represent?
        LineDataSet dataset = new LineDataSet(entries, "Gross sales");

        // The chart columns need labels.
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");

        // The data of a line chart is comprised of the column names and a collection of entries.
        LineData data = new LineData(labels, dataset);

        // The line connecting two points will be colored in.
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        // The line connecting two points will be curved instead of straight.
        dataset.setDrawCubic(true);
        // The area under the curve will be filled in with color.
        dataset.setDrawFilled(true);

        // Place the data within the line chart.
        lineChart.setData(data);

        lineChart.setDescription("Description");

        // The chart can have various animations.
        lineChart.animateY(5000);


    }

}
