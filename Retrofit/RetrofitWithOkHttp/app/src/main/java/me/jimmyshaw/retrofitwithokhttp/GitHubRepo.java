package me.jimmyshaw.retrofitwithokhttp;

/*
    This is the data model class. It has the required fields and methods to map the response data.
    If an appropriate response converter is present, Retrofit ensures that the server's JSON response
    gets mapped correctly to Java objects (assuming that the JSON data matches the given Java
    data model class).
*/
public class GitHubRepo {

    private int id;
    private String name;

    public GitHubRepo() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
