package com.gzeinnumer.chatapppart2.notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//todo 79
public class Client {
    private static Retrofit retrofit = null;

    public static Retrofit getCLient(String url) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
