package action;

import com.opensymphony.xwork2.ActionSupport;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class LogOut extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        session.clear();
        return "success";
    }
}
