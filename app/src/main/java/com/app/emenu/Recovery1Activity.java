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

public class Recovery1Activity extends AppCompatActivity {

    public static final String PREF_LANG = "PREFERENCE_LANG";

    private EditText et_email;
    private TextView tv_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery1);

        SharedPreferences langPref = getSharedPreferences(PREF_LANG, Context.MODE_PRIVATE);
        final String lang = langPref.getString("Language", "en");

        et_email = findViewById(R.id.et_email);
        tv_enter = findViewById(R.id.tv_enter);

        tv_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                RequestBody requestLang = RequestBody.create(MediaType.parse("multipart/form-data"), lang);
                RequestBody requestEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);

                Call<JsonObject> call = apiInterface.reset_pwd2(requestLang, requestEmail);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        String response_body = response.body().toString();

                        try {
                            JSONObject jsonObject = new JSONObject(response_body);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String motivo = dataObject.getString("motivo");
                            Toast.makeText(Recovery1Activity.this, motivo, Toast.LENGTH_LONG).show();
                            JSONObject resultObject = jsonObject.getJSONObject("response");
                            String result = resultObject.getString("result");
                            if (result.equals("OK")) {
                                Intent intent = new Intent(Recovery1Activity.this, Recovery2Activity.class);
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