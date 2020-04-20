package com.gzeinnumer.chatapppart2.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//todo 83
public interface  APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0HyWd0E:APA91bHw-4HqY3-EkpN8fvMAA73ca02Tf37BAS3Pztt8xaxsdiMzic9ZUt_vOJfeXb9iB8dp50ybn9mOx7HGnt8mSWh1-MP6p4wLbW0MEBQeE1dNcuWu0S0-W0xFFqWAIm66FjOLqYrv"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
