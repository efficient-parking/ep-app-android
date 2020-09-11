package com.efficientparking.efficientparking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TargaActivity extends AppCompatActivity {
    TextView targa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targa);
        targa = findViewById(R.id.regTarga);
    }
}
