package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 13/12/2015.
 */
public class EditReward extends ActionSupport implements SessionAware
{
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private String type,reward;
    private int id,valueReward,projectid;

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("tipo");
        if(type.compareTo("remove")==0)
        {
            if((user.removeReward(id,projectid))==false)
            {
                type = null;
                return "index";
            }
            else
            {
                ArrayList<Project> projects = user.getMyProjects();
                session.put("projects",projects);
                return "success";
            }
        }
        else if (type.compareTo("add")==0)
        {
            if((user.addReward(reward,valueReward,projectid))==false)
            {
                type = null;
                return "index";
            }
            else
            {
                ArrayList<Project> projects = user.getMyProjects();
                session.put("projects",projects);
                return "success";
            }
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValueReward() {
        return valueReward;
    }

    public void setValueReward(int valueReward) {
        this.valueReward = valueReward;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
