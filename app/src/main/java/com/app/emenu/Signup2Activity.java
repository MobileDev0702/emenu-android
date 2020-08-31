package com.app.emenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Signup2Activity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private EditText et_phonenumber;
    private TextView tv_enter;

    private Integer code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        et_phonenumber = findViewById(R.id.et_phone);
        tv_enter = findViewById(R.id.tv_enter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rng = new Random();
                code = rng.nextInt(900000) + 100000;

                if (et_phonenumber.getText().toString().isEmpty()) {
                    Toast.makeText(Signup2Activity.this, "Input phone number!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    sendSMSMessage();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    protected void sendSMSMessage() {
        Intent intent = new Intent(Signup2Activity.this, Signup3Activity.class);
        intent.putExtra("DigitCode", code.toString());
        intent.putExtra("PhoneNumber", et_phonenumber.getText().toString());
        startActivity(intent);
        finish();
    }
}