package me.jimmyshaw.retrofitwithokhttp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
    This interface has one method and it's to request the repositories for a given user.
    We're replacing the path parameter placehold {user} with the actual value of the user
    when calling this method.
*/
public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<GitHubRepo>> reposForUser(@Path("user") String username);
}
