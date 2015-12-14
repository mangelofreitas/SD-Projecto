package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Message;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 11/12/2015.
 */
public class ListProjects extends ActionSupport implements SessionAware {
    private Map<String,Object> session;
    private static final long serialVersionUID = 1L;
    private String type;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("tipo");
        if(user.getRmiConnection()!=null)
        {
            ArrayList <Project> projects = null;
            session.put("projectstype",type);
            if(type.compareTo("myprojects")==0)
            {
                projects = user.getMyProjects();
                for(int i=0;i<projects.size();i++)
                {
                    ArrayList<Message> messages = user.getMessagesProject(projects.get(i).getProjectID());
                    projects.get(i).setMessages(messages);
                }
            }
            else if(type.compareTo("actualprojects")==0)
            {
                projects = user.getActualProjects();
                for(int i=0;i<projects.size();i++)
                {
                    if(projects.get(i).getUser().getUsernameID()==user.getUser().getUsernameID())
                    {
                        projects.remove(projects.get(i));
                        i--;
                    }
                }
                for(int i=0;i<projects.size();i++)
                {
                    ArrayList<Message> messages = user.getMessagesProject(projects.get(i).getProjectID());
                    projects.get(i).setMessages(messages);
                }
            }
            else if(type.compareTo("oldprojects")==0)
            {
                projects = user.getOldProjects();
            }
            session.put("projects", projects);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
