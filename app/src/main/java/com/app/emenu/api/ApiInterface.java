package com.app.emenu.api;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST(EndApi.pubblicita)
    Call<JsonObject> pubblicita(@Part("latitudine") RequestBody lat,
                                @Part("longitudine") RequestBody lon,
                                @Part("distanza") RequestBody distance);

    @Multipart
    @POST(EndApi.registrazione1)
    Call<JsonObject> registrazione1(@Part("lingua") RequestBody lang,
                                    @Part("cognome") RequestBody lname,
                                    @Part("nome") RequestBody fname,
                                    @Part("email") RequestBody email,
                                    @Part("password1") RequestBody userpwd,
                                    @Part("password2") RequestBody userconfirmpwd,
                                    @Part("indirizzo") RequestBody address,
                                    @Part("cap") RequestBody zipcode,
                                    @Part("citta") RequestBody city,
                                    @Part("provincia") RequestBody country,
                                    @Part("telefono") RequestBody phonenumber,
                                    @Part("esonero") RequestBody contract,
                                    @Part("clausole") RequestBody disclaimer,
                                    @Part("privacy") RequestBody privacy);

    @Multipart
    @POST(EndApi.login)
    Call<JsonObject> login(@Part("lingua") RequestBody lang,
                           @Part("email") RequestBody email,
                           @Part("password") RequestBody pwd,
                           @Part("device") RequestBody device);

    @Multipart
    @POST(EndApi.reset_pwd2)
    Call<JsonObject> reset_pwd2(@Part("lingua") RequestBody lang,
                                @Part("email") RequestBody email);

    @Multipart
    @POST(EndApi.reset_pwd4)
    Call<JsonObject> reset_pwd4(@Part("lingua") RequestBody lang,
                                @Part("pwd1") RequestBody pwd1,
                                @Part("pwd2") RequestBody pwd2,
                                @Part("pwd3") RequestBody pwd3);
}
