package action;

import com.opensymphony.xwork2.ActionSupport;
import model.Reward;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by filip on 15/12/2015.
 */
public class ConsultRewards extends ActionSupport implements SessionAware{
    private Map<String,Object> session;
    private static final long serialVersionUID = 1L;

    @Override
    public void setSession(Map<String, Object> session) { this.session = session;}

    public String execute()
    {
        SessionModel user = getModel();
        if (user.getRmiConnection()!=null){
            ArrayList <Reward> rewards = user.getUserRewards();
            session.put("rewards",rewards);
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
            this.setSessionModel(new SessionModel());
        }
        return (SessionModel) session.get("model");
    }

    public void setSessionModel(SessionModel model)
    {
        this.session.put("model", model);
    }
}
