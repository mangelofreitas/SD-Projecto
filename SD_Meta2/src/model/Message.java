package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguel and maria
 */
public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
    private int messageID;
    private String message;
    private Project project;
    private User user;
    private ArrayList<Reply> replies;

    public Message()
    {

    }

    public Message(int messageID)
    {
        this.messageID = messageID;
    }

    public Message(String message, Project project, User user)
    {
        this.message = message;
        this.project = project;
        this.user = user;
        this.replies = new ArrayList<Reply>();
    }

    public Message(int messageID,String message, Project project, User user, ArrayList<Reply> replies)
    {
        this.messageID = messageID;
        this.message = message;
        this.project = project;
        this.user = user;
        this.replies = replies;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public String toString() {
        String text = messageID + " -> " + user.getUsername() +": " + message+"\n";
        for(int i=0;i<replies.size();i++)
        {
            text = text + "\n       " + replies.get(i);
        }
        return text;
    }
}
