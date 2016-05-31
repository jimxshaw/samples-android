package me.jimmyshaw.retrofitwithokhttp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 This ServiceGenerator is the heart of our API client. It only defines one method and that's to
 create a basic REST client for a given class/interface, which returns a service class from
 the interface.

 The reason why all the class members are static is because we want to use the same OkHttpClient
 throughout the app. We just open one socket connection that handles all requests and responses,
 including caching. To do this, we either have to inject OkHttpClient into this class via dependency
 injector or use statics. It's simpler to use statics.
*/
public class ServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    // This is our Retrofit object. It creates a new REST client with the given API base url.
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    // This is our OkHttpClient object.
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    // The serviceClass parameter defines the annotated class/interface for API requests.
    public static <S> S createService(Class<S> serviceClass) {
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

}
