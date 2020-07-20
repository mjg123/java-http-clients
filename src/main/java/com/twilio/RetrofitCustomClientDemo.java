package com.twilio;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RetrofitCustomClientDemo {

    // note that requests in this class need an API Key, which is provided
    // for all requests by an interceptor in the `clientWithApiKey` method.
    public interface APODClient {
        @GET("/planetary/apod")
        @Headers("accept: application/json")
        CompletableFuture<APOD> getApod();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        OkHttpClient clientWithApiKey = clientWithApiKey("DEMO_KEY");

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(clientWithApiKey)
            .build();

        APODClient apodClient = retrofit.create(APODClient.class);

        APOD apod = apodClient.getApod().get();

        System.out.println(apod.title);
    }

    private static OkHttpClient clientWithApiKey(String apiKey) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    HttpUrl newUrl = originalRequest.url().newBuilder()
                        .addQueryParameter("api_key", apiKey).build();
                    Request request = originalRequest.newBuilder().url(newUrl).build();
                    return chain.proceed(request);
                }).build();
    }

}
