package me.jimmyshaw.retrofit;

import retrofit2.Callback;
import retrofit2.http.GET;

public interface MInterface
{
    @GET("/users/jimxshaw")
    void getUser(Callback<Pojo> uscb);
}
