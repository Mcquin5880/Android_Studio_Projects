package model;

import com.google.firebase.Timestamp;

public class Journal {

    private String title;
    private String thought;
    private String imageURL;
    private String username;
    private String userID;
    private Timestamp timeAdded;

    public Journal() { // need empty constructor for Firestore to work properly

    }

    public Journal(String title, String thought, String imageURL, String username, String userID, Timestamp timeAdded) {
        this.title = title;
        this.thought = thought;
        this.imageURL = imageURL;
        this.username = username;
        this.userID = userID;
        this.timeAdded = timeAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }
}
