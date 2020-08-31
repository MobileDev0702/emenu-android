package com.app.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
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

public class Signup3Activity extends AppCompatActivity {

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

    private EditText et_verification_code;
    private TextView tv_send;
    private String code;
    private String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        et_verification_code = findViewById(R.id.et_verification_code);
        tv_send = findViewById(R.id.tv_send);

        code = getIntent().getStringExtra("DigitCode");
        phoneno = getIntent().getStringExtra("PhoneNumber");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneno, null, "Your EMenu verification code is " + code.toString(), null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();

        SharedPreferences langPref = getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
        final String lang = langPref.getString("Language", "en");
        SharedPreferences fnamePref = getSharedPreferences(PREF_FNAME, Context.MODE_PRIVATE);
        final String fname = fnamePref.getString("FName", "");
        SharedPreferences lnamePref = getSharedPreferences(PREF_LNAME, Context.MODE_PRIVATE);
        final String lname = lnamePref.getString("LName", "");
        SharedPreferences emailPref = getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
        final String email = emailPref.getString("Email", "");
        SharedPreferences pwdPref = getSharedPreferences(PREF_PWD, Context.MODE_PRIVATE);
        final String pwd = pwdPref.getString("Pwd", "");
        SharedPreferences confirmpwdPref = getSharedPreferences(PREF_CONFIRMPWD, Context.MODE_PRIVATE);
        final String confirmpwd = confirmpwdPref.getString("ConfirmPwd", "");
        SharedPreferences addressPref = getSharedPreferences(PREF_ADDRESS, Context.MODE_PRIVATE);
        final String address = addressPref.getString("Address", "");
        SharedPreferences zipcodePref = getSharedPreferences(PREF_ZIPCODE, Context.MODE_PRIVATE);
        final String zipcode = zipcodePref.getString("Zipcode", "");
        SharedPreferences cityPref = getSharedPreferences(PREF_CITY, Context.MODE_PRIVATE);
        final String city = cityPref.getString("City", "");
        SharedPreferences countryPref = getSharedPreferences(PREF_COUNTRY, Context.MODE_PRIVATE);
        final String country = countryPref.getString("Country", "");
        final String phonenumber = getIntent().getStringExtra("PhoneNumber");
        SharedPreferences contractPref = getSharedPreferences(PREF_CONTRACT, Context.MODE_PRIVATE);
        final String contract = contractPref.getString("Contract", "");
        SharedPreferences disclaimerPref = getSharedPreferences(PREF_DISCLAIMER, Context.MODE_PRIVATE);
        final String disclaimer = disclaimerPref.getString("Disclaimer", "");
        SharedPreferences privacyPref = getSharedPreferences(PREF_PRIVACY, Context.MODE_PRIVATE);
        final String privacy = privacyPref.getString("Privacy", "");

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_verification_code.getText().toString().equals(code)) {
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    RequestBody requestLang = RequestBody.create(MediaType.parse("multipart/form-data"), lang);
                    RequestBody requestLname = RequestBody.create(MediaType.parse("multipart/form-data"), lname);
                    RequestBody requestFname = RequestBody.create(MediaType.parse("multipart/form-data"), fname);
                    RequestBody requestEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                    RequestBody requestPwd = RequestBody.create(MediaType.parse("multipart/form-data"), pwd);
                    RequestBody requestConfirmPwd = RequestBody.create(MediaType.parse("multipart/form-data"), confirmpwd);
                    RequestBody requestAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
                    RequestBody requestZipcode = RequestBody.create(MediaType.parse("multipart/form-data"), zipcode);
                    RequestBody requestCity = RequestBody.create(MediaType.parse("multipart/form-data"), city);
                    RequestBody requestCountry = RequestBody.create(MediaType.parse("multipart/form-data"), country);
                    RequestBody requestPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phonenumber);
                    RequestBody requestContract = RequestBody.create(MediaType.parse("multipart/form-data"), contract);
                    RequestBody requestDisclaimer = RequestBody.create(MediaType.parse("multipart/form-data"), disclaimer);
                    RequestBody requestPrivacy = RequestBody.create(MediaType.parse("multipart/form-data"), privacy);

                    Call<JsonObject> call = apiInterface.registrazione1(requestLang, requestLname, requestFname, requestEmail, requestPwd, requestConfirmPwd, requestAddress, requestZipcode,
                            requestCity, requestCountry, requestPhone, requestContract, requestDisclaimer, requestPrivacy);

                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            String response_body = response.body().toString();

                            try {
                                JSONObject jsonObject = new JSONObject(response_body);
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                String motivo = dataObject.getString("motivo");
                                Toast.makeText(Signup3Activity.this, motivo, Toast.LENGTH_LONG).show();
                                JSONObject resultObject = jsonObject.getJSONObject("response");
                                String result = resultObject.getString("result");
                                if (result.equals("OK")) {
                                    Intent intent = new Intent(Signup3Activity.this, ValidateActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(Signup3Activity.this, "Verification Code is wrong!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}