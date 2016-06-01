package me.jimmyshaw.retrofitwithokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a simple REST adapter that points to GitHub's api.
        GitHubService service = ServiceGenerator.createService(GitHubService.class);

        // Retrieve and print a list of repositories for a particular user.
        Call<List<GitHubRepo>> call = service.reposForUser("jimxshaw");
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                if (response.isSuccessful()) {
                    for (GitHubRepo repo : response.body()) {
                        Log.d("Repo: ", repo.getName() + " (ID: " + repo.getId() + ")");
                    }
                }
                else {
                    Log.e("Request failure: ", "Could not retrieve GitHub repositories");
                }
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Log.e("Error retrieving repos", t.getMessage());
            }
        });
    }
}
