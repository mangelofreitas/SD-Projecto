import java.io.Serializable;

/**
 * Created by miguel and maria
 */
public class User implements Serializable
{
    private int usernameID;
    private String username;
    private String mail;
    private String password;
    private int money;

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

    public User(int usernameID, String username, String mail, String password, int money)
    {
        this.usernameID = usernameID;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.money = money;
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
}
