package com.app.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.emenu.api.ApiClient;
import com.app.emenu.api.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup1Activity extends AppCompatActivity {

    public static final String PREF_LANG = "PREFERENCE_LANG";
    public static final String PREF_FNAME = "PREFERENCE_FNAME";
    public static final String PREF_LNAME = "PREFERENCE_LNAME";
    public static final String PREF_EMAIL = "PREFERENCE_EMAIL";
    public static final String PREF_PWD = "PREFERENCE_PWD";
    public static final String PREF_CONFIRMPWD = "PREFERENCE_CONFIRMPWD";
    public static final String PREF_ADDRESS = "PREFERENCE_ADDRESS";
    public static final String PREF_ZIPCODE = "PREFERENCE_ZIPCODE";
    public static final String PREF_CITY = "PREFERENCE_CITY";
    public static final String PREF_COUNTRY = "PREFERENCE_COUNTRY";
    public static final String PREF_CONTRACT = "PREFERENCE_CONTRACT";
    public static final String PREF_DISCLAIMER = "PREFERENCE_DISCLAIMER";
    public static final String PREF_PRIVACY = "PREFERENCE_PRIVACY";

    private EditText et_fname, et_lname, et_email, et_pwd, et_confirmpwd, et_address, et_zipcode, et_city, et_country;
    private TextView tv_contract_watch, tv_disclaimer_watch, tv_privacy_watch;
    private CheckBox cb_contract, cb_disclaimer, cb_privacy;
    private TextView tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        initComponent();
        setOnClickListener();
    }

    private void initComponent() {
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        et_confirmpwd = findViewById(R.id.et_confirmpwd);
        et_address = findViewById(R.id.et_address);
        et_zipcode = findViewById(R.id.et_zipcode);
        et_city = findViewById(R.id.et_city);
        et_country = findViewById(R.id.et_country);

        tv_contract_watch = findViewById(R.id.tv_contract_watch);
        tv_disclaimer_watch = findViewById(R.id.tv_disclaimer_watch);
        tv_privacy_watch = findViewById(R.id.tv_privacy_watch);

        cb_contract = findViewById(R.id.cb_contract);
        cb_disclaimer = findViewById(R.id.cb_disclaimer);
        cb_privacy = findViewById(R.id.cb_privacy);

        tv_next = findViewById(R.id.tv_next);
    }

    private void setOnClickListener() {
        onClickContractWatch();
        onClickDisclaimerWatch();
        onClickPrivacyWatch();

        onClickNext();
    }

    private void onClickContractWatch() {

    }

    private void onClickDisclaimerWatch() {

    }

    private void onClickPrivacyWatch() {

    }

    private void onClickNext() {
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences fnamePref = getSharedPreferences(PREF_FNAME, Context.MODE_PRIVATE);
                String fname = et_fname.getText().toString();
                if (fname.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input first name!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    fnamePref.edit().putString("FName", fname).commit();
                }

                SharedPreferences lnamePref = getSharedPreferences(PREF_LNAME, Context.MODE_PRIVATE);
                String lname = et_lname.getText().toString();
                if (lname.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input last name!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    lnamePref.edit().putString("LName", lname).commit();
                }

                SharedPreferences emailPref = getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
                String email = et_email.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input email!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    emailPref.edit().putString("Email", email).commit();
                }

                SharedPreferences pwdPref = getSharedPreferences(PREF_PWD, Context.MODE_PRIVATE);
                String pwd = et_pwd.getText().toString();
                if (pwd.isEmpty() || pwd.length() < 8) {
                    Toast.makeText(Signup1Activity.this, "Password must contain at least 8 characters!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    pwdPref.edit().putString("Pwd", pwd).commit();
                }

                SharedPreferences confirmPwdPref = getSharedPreferences(PREF_CONFIRMPWD, Context.MODE_PRIVATE);
                String confirmpwd = et_confirmpwd.getText().toString();
                if (!confirmpwd.equals(pwd)) {
                    Toast.makeText(Signup1Activity.this, "Password differs from confirmation!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    confirmPwdPref.edit().putString("ConfirmPwd", confirmpwd).commit();
                }

                SharedPreferences addressPref = getSharedPreferences(PREF_ADDRESS, Context.MODE_PRIVATE);
                String address = et_address.getText().toString();
                if (address.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input address!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    addressPref.edit().putString("Address", address).commit();
                }

                SharedPreferences zipcodePref = getSharedPreferences(PREF_ZIPCODE, Context.MODE_PRIVATE);
                String zipcode = et_zipcode.getText().toString();
                if (zipcode.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input zipcode!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    zipcodePref.edit().putString("Zipcode", zipcode).commit();
                }

                SharedPreferences cityPref = getSharedPreferences(PREF_CITY, Context.MODE_PRIVATE);
                String city = et_city.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input city!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    cityPref.edit().putString("City", city).commit();
                }

                SharedPreferences countryPref = getSharedPreferences(PREF_COUNTRY, Context.MODE_PRIVATE);
                String country = et_country.getText().toString();
                if (country.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Input country!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    countryPref.edit().putString("Country", country).commit();
                }

                SharedPreferences contractPref = getSharedPreferences(PREF_CONTRACT, Context.MODE_PRIVATE);
                String contract = cb_contract.isChecked() ? "ok" : "";
                if (contract.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Terms of contract must be checked for acknowledgement!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    contractPref.edit().putString("Contract", contract).commit();
                }

                SharedPreferences disclaimerPref = getSharedPreferences(PREF_DISCLAIMER, Context.MODE_PRIVATE);
                String disclaimer = cb_disclaimer.isChecked() ? "ok" : "";
                if (disclaimer.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Disclaimer must be checked for acknowledgement!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    disclaimerPref.edit().putString("Disclaimer", disclaimer).commit();
                }

                SharedPreferences privacyPref = getSharedPreferences(PREF_PRIVACY, Context.MODE_PRIVATE);
                String privacy = cb_privacy.isChecked() ? "ok" : "";
                if (privacy.isEmpty()) {
                    Toast.makeText(Signup1Activity.this, "Privacy must be checked for acknowledgement!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    privacyPref.edit().putString("Privacy", privacy).commit();
                }

                Intent intent = new Intent(Signup1Activity.this, Signup2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}