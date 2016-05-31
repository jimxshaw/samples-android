package me.jimmyshaw.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MInterface {
    @GET("/users/jimxshaw")
    Call<Pojo> getUser();
}
