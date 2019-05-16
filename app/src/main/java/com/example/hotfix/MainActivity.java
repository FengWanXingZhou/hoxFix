package com.example.hotfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HotFix";


    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.calculate);
        Calculate calculate = new Calculate();
        button.setOnClickListener(v->{

            calculate.calculate(10,0);

        });
    }




}
