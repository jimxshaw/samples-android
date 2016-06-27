package me.jimmyshaw.dropbucketlist.beans;

public class Drop {

    private String goal;
    private long dateAdded;
    private long dateDue;
    private boolean completed;

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
