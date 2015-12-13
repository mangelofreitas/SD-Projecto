package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Message;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 11/12/2015.
 */
public class SendMessage extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private String message;
    private int projectID;

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("tipo");
        System.out.println(message);
        if(user.sendMessage(message,projectID)==false)
        {
            return "index";
        }
        else
        {
            ArrayList<Project> projects = user.getActualProjects();
            for(int i=0;i<projects.size();i++)
            {
                ArrayList<Message> messages = user.getMessagesProject(projects.get(i).getProjectID());
                projects.get(i).setMessages(messages);
            }
            session.put("projects",projects);
            return "success";
        }
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
