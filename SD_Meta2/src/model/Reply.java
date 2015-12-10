package model;

import java.io.Serializable;

/**
 * Created by miguel and maria
 */
public class Reply implements Serializable
{
	private static final long serialVersionUID = 1L;
    private int replyID;
    private String message;
    private User user;


    public Reply()
    {

    }

    public Reply(String message, User user)
    {
        this.message = message;
        this.user = user;
    }

    public Reply(int replyID, String message, User user)
    {
        this.replyID = replyID;
        this.message = message;
        this.user = user;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return  user.getUsername() + ": " + message;
    }
}
