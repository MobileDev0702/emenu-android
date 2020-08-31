package com.app.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SigninActivity extends AppCompatActivity {

    public static final String PREF_LANG = "PREFERENCE_LANG";

    private ImageView iv_italy, iv_spain, iv_english, iv_france;
    private EditText et_email, et_password;
    private TextView tv_forgotpwd;
    private ImageView iv_signin;
    private ImageView iv_signup;

    private String language = "";
    private SharedPreferences langPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initComponent();
        setOnClickListener();
        setSharedPreference();
    }

    private void initComponent() {
        iv_italy = findViewById(R.id.iv_italy);
        iv_spain = findViewById(R.id.iv_spain);
        iv_english = findViewById(R.id.iv_uk);
        iv_france = findViewById(R.id.iv_france);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_forgotpwd = findViewById(R.id.tv_forgotpwd);
        iv_signin = findViewById(R.id.iv_signin);
        iv_signup = findViewById(R.id.iv_account_signup);
    }

    private void setOnClickListener() {
        onClickItalyButton();
        onClickSpainButton();
        onClickUKButton();
        onClickFranceButton();

        onClickForgotPwd();
        onClickSignIn();
        onClickSignUp();
    }

    private void setSharedPreference() {
        langPref = getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
    }

    private void onClickItalyButton() {
        iv_italy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "it";
                iv_italy.setBackgroundResource(R.drawable.flagcornerradius);
                iv_spain.setBackgroundResource(0);
                iv_english.setBackgroundResource(0);
                iv_france.setBackgroundResource(0);
                langPref.edit().putString("Language", language).commit();
            }
        });
    }

    private void onClickSpainButton() {
        iv_spain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "es";
                iv_italy.setBackgroundResource(0);
                iv_spain.setBackgroundResource(R.drawable.flagcornerradius);
                iv_english.setBackgroundResource(0);
                iv_france.setBackgroundResource(0);
                langPref.edit().putString("Language", language).commit();
            }
        });
    }

    private void onClickUKButton() {
        iv_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "en";
                iv_italy.setBackgroundResource(0);
                iv_spain.setBackgroundResource(0);
                iv_english.setBackgroundResource(R.drawable.flagcornerradius);
                iv_france.setBackgroundResource(0);
                langPref.edit().putString("Language", language).commit();
            }
        });
    }

    private void onClickFranceButton() {
        iv_france.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language = "fr";
                iv_italy.setBackgroundResource(0);
                iv_spain.setBackgroundResource(0);
                iv_english.setBackgroundResource(0);
                iv_france.setBackgroundResource(R.drawable.flagcornerradius);
                langPref.edit().putString("Language", language).commit();
            }
        });
    }

    private void onClickForgotPwd() {

    }

    private void onClickSignIn() {
        iv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                String pwd = et_password.getText().toString();
                if (language.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Please select language!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Please input email address!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pwd.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Please input password!", Toast.LENGTH_LONG).show();
                    return;
                }
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestLang = RequestBody.create(MediaType.parse("multipart/form-data"), language);
                RequestBody requestEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody requestPwd = RequestBody.create(MediaType.parse("multipart/form-data"), pwd);
                RequestBody requestDevice = RequestBody.create(MediaType.parse("multipart/form-data"), "ANDROID");

                Call<JsonObject> call = apiInterface.login(requestLang, requestEmail, requestPwd, requestDevice);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String response_body = response.body().toString();

                        try {
                            JSONObject jsonObject = new JSONObject(response_body);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String motivo = dataObject.getString("motivo");
                            Toast.makeText(SigninActivity.this, motivo, Toast.LENGTH_LONG).show();
                            JSONObject resultObject = jsonObject.getJSONObject("response");
                            String result = resultObject.getString("result");
                            if (result.equals("OK")) {
                                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
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
            }
        });
    }

    private void onClickSignUp() {
        iv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SigninActivity.this, Signup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}