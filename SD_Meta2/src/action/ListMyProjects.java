package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 11/12/2015.
 */
public class ListMyProjects extends ActionSupport implements SessionAware {
    private Map<String,Object> session;
    private static final long serialVersionUID = 1L;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        if(user.getRmiConnection()!=null)
        {
            try
            {
                ArrayList <Project> projects = user.getRmiConnection().getMyProjects(user.getUser());
                session.put("myProjects", projects);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            return "success";
        }
        return "error";
    }

    public SessionModel getModel()
    {
        if(!session.containsKey("model"))
        {
            this.setSessionModel(new SessionModel());
        }
        return (SessionModel) session.get("model");

    }

    public void setSessionModel(SessionModel model)
    {
        this.session.put("model", model);
    }
}
