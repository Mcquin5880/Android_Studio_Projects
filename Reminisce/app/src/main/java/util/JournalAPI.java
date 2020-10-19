package util;

import android.app.Application;

public class JournalAPI extends Application {

    private String username;
    private String userID;
    private static JournalAPI journalAPI_Instance;

    public static JournalAPI getInstance() {
        if (journalAPI_Instance == null)
                journalAPI_Instance = new JournalAPI();
        return journalAPI_Instance;
    }

    public JournalAPI() {

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
}
