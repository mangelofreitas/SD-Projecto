package action;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import model.SessionModel;

public class Regist extends ActionSupport implements SessionAware{
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private String mail = null, password = null, username = null;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("user");
        if(user.getRmiConnection()!=null && user.regist(username,mail, password)!=false)
        {
            session.put("user", user);
            session.put("tipo","regist");
            return "success";
        }
        else
        {
            return "index";
        }
    }

    public SessionModel getModel()
    {
        if(!session.containsKey("model"))
        {
            this.setLoginModel(new SessionModel());
        }
        return (SessionModel) session.get("model");
    }

    public void setLoginModel(SessionModel model)
    {
        this.session.put("model", model);
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getMail()
    {
        return mail;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
