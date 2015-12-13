package action;

import com.opensymphony.xwork2.ActionSupport;
import model.ProductType;
import model.Reward;
import model.SessionModel;

import org.apache.struts2.interceptor.SessionAware;


import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 11/12/2015.
 */
public class CreateProject extends ActionSupport implements SessionAware
{
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private String name, description, productType[],reward[];
    private Date dateLimit;
    private int requestedValue, valueReward[];

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        session.remove("tipo");
        if(user.getRmiConnection()!=null && user.createProject(name,description,dateLimit,requestedValue,productType,reward,valueReward))
        {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(String dateLimit) {
        String split[] = dateLimit.split("-");
        this.dateLimit = new Date(Integer.parseInt(split[0])-1900,Integer.parseInt(split[1])-1,Integer.parseInt(split[2]));
    }

    public int getRequestedValue() {
        return requestedValue;
    }

    public void setRequestedValue(int requestedValue) {
        this.requestedValue = requestedValue;
    }

    public String[] getProductType() {
        return productType;
    }

    public void setProductType(String productType[]) {
        this.productType = productType;
    }

    public String[] getReward() {
        return reward;
    }

    public void setReward(String[] reward) {
        this.reward = reward;
    }

    public int[] getValueReward() {
        return valueReward;
    }

    public void setValueReward(int[] valueReward) {
        this.valueReward = valueReward;
    }
}
