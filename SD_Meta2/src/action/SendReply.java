package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Message;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

public class SendReply extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private String message;
    private int projectID,messageID;

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        if(user.sendReply(message,projectID,messageID)==false && message!=null)
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
            message = null;
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

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
}
