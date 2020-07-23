package com.twilio;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RetrofitDemo {

    public interface APODClient {
        @GET("/planetary/apod")
        @Headers("accept: application/json")
        CompletableFuture<APOD> getApod(@Query("api_key") String apiKey);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

        APODClient apodClient = retrofit.create(APODClient.class);

        CompletableFuture<APOD> response = apodClient.getApod("DEMO_KEY");

        // do other stuff here while the request is in-flight

        APOD apod = response.get();

        System.out.println(apod.title);


    }

}
