package com.muzi.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private SimpleBar simpleBar;
    private MutipleBar mutipleBar;
    private int[] colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleBar = (SimpleBar) findViewById(R.id.simpleBar);
        mutipleBar = (MutipleBar) findViewById(R.id.mutipleBar);

        colors = new int[]{R.color.themeAmber,
                R.color.themeBlue,
                R.color.themeBrown,
                R.color.themeGreen,
                R.color.themeOrange,
                R.color.themePink,
                R.color.themeRed};

        mutipleBar.setColors(colors);
        mutipleBar.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleBar.onDestroy();
        mutipleBar.onDestroy();
    }
}
