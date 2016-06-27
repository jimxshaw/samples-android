package me.jimmyshaw.dropbucketlist.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Drop extends RealmObject {

    @PrimaryKey
    private long dateAdded;
    private long dateDue;
    private String goal;
    private boolean completed;

    public Drop() {

    }

    public Drop(long dateAdded, long dateDue, String goal, boolean completed) {
        this.dateAdded = dateAdded;
        this.dateDue = dateDue;
        this.goal = goal;
        this.completed = completed;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateDue() {
        return dateDue;
    }

    public void setDateDue(long dateDue) {
        this.dateDue = dateDue;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
