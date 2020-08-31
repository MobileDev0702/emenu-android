package com.app.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Recovery2Activity extends AppCompatActivity {

    private TextView tv_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery2);

        tv_continue = findViewById(R.id.tv_continue);
        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Recovery2Activity.this, Recovery3Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}