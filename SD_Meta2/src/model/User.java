package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by miguel and maria
 */
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
    private int usernameID;
    private String username;
    private String mail;
    private String password;
    private int money;
    private ArrayList<Reward> rewards;

    public User(int usernameID)
    {
        this.usernameID = usernameID;
    }

    public User(String mail, String password)
    {
        this.mail = mail;
        this.password = password;
    }

    public User(String username, String mail, String password)
    {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public User(int usernameID, String username, String mail, String password, int money, ArrayList<Reward> rewards)
    {
        this.usernameID = usernameID;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.money = money;
        this.rewards = rewards;
    }

    public int getUsernameID() {
        return usernameID;
    }

    public void setUsernameID(int usernameID) {
        this.usernameID = usernameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }
}
