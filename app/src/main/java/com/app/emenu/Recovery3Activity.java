package com.app.emenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class Recovery3Activity extends AppCompatActivity {

    public static final String PREF_LANG = "PREFERENCE_LANG";

    private EditText et_temporary_pwd, et_new_pwd, et_repeat_pwd;
    private TextView tv_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery3);

        et_temporary_pwd = findViewById(R.id.et_temp_pwd);
        et_new_pwd = findViewById(R.id.et_new_pwd);
        et_repeat_pwd = findViewById(R.id.et_repeat_pwd);

        SharedPreferences langPref = getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
        final String lang = langPref.getString("Language", "en");

        tv_enter = findViewById(R.id.tv_enter);
        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Recovery3Activity.this, DoneActivity.class);
//                startActivity(intent);
//                finish();

                String pwd1 = et_temporary_pwd.getText().toString();
                String pwd2 =  et_new_pwd.getText().toString();
                String pwd3 = et_repeat_pwd.getText().toString();

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestLang = RequestBody.create(MediaType.parse("multipart/form-data"), lang);
                RequestBody requestPwd1 = RequestBody.create(MediaType.parse("multipart/form-data"), pwd1);
                RequestBody requestPwd2 = RequestBody.create(MediaType.parse("multipart/form-data"), pwd2);
                RequestBody requestPwd3 = RequestBody.create(MediaType.parse("multipart/form-data"), pwd3);

                Call<JsonObject> call = apiInterface.reset_pwd4(requestLang, requestPwd1, requestPwd2, requestPwd3);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String response_body = response.body().toString();

                        try {
                            JSONObject jsonObject = new JSONObject(response_body);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String motivo = dataObject.getString("motivo");
                            Toast.makeText(Recovery3Activity.this, motivo, Toast.LENGTH_LONG).show();
                            JSONObject resultObject = jsonObject.getJSONObject("response");
                            String result = resultObject.getString("result");
                            if (result.equals("OK")) {
                                Intent intent = new Intent(Recovery3Activity.this, DoneActivity.class);
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
}