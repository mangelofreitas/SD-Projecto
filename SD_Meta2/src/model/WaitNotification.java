package model;

import java.io.Serializable;

/**
 * Created by mianj on 16/12/2015.
 */
public class WaitNotification implements Serializable
{
    private String message;
    private String username;

    WaitNotification(String message, String username)
    {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "message = " + message +
                ", username = " + username+"\n";
    }
}
