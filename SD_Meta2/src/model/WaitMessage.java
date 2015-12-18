package model;

import java.io.Serializable;

/**
 * Created by mianj on 17/12/2015.
 */
public class WaitMessage implements Serializable
{
    private String message;
    private String username;

    WaitMessage(String message, String username)
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
