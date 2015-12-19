package action;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import model.ProductType;
import model.Reward;
import model.SessionModel;

import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


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
    private String name =null, description=null, productType[],reward[];
    private Date dateLimit;
    private int requestedValue=-1, valueReward[];

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        if(session.get("tipo")!=null)
        {
            session.remove("tipo");
        }
        if(user!=null)
        {
            if(user.getRmiConnection()!=null)
            {
                if(name!=null && description!=null && productType.length!=0 && reward.length!=0 && valueReward.length!=0 && requestedValue!=-1)
                {
                    if(user.createProject(name,description,dateLimit,requestedValue,productType,reward,valueReward))
                    {
                        if(user.getUser().getMail().contains("tumblr.com"))
                        {
                            OAuthService service = (OAuthService) session.get("service");
                            String blogName = user.getUser().getUsername();
                            String API_USER_TOKEN = (String) session.get("accessTokenKey");
                            String API_USER_SECRET = (String) session.get("accessTokenSecret");
                            Token accessToken = new Token( API_USER_TOKEN, API_USER_SECRET);
                            try{
                                OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/blog/"+blogName+".tumblr.com/post",service); //request que vai postar qualquer coisa
                                request.addHeader("Accept", "application/json");
                                request.addBodyParameter("body", "I have created the project: "+name+", that consist on "+description+"\nDonate me to complete this project!");
                                service.signRequest(accessToken, request);
                                Response response = request.send();
                                System.out.println("Got it! Lets see what we found...");
                                System.out.println("HTTP RESPONSE: =============");
                                System.out.println(response.getCode());
                                System.out.println(response.getBody());
                                System.out.println("END RESPONSE ===============");
                                JSONObject inf = (JSONObject) JSONValue.parse(response.getBody());
                                JSONObject response1 = (JSONObject) inf.get("response");
                                long postID = (long)response1.get("id");
                                user.updateProjectPostID(postID);
                            }
                            catch(OAuthException e)
                            {
                                System.err.println("OAuthException: "+e);
                            }
                        }
                        name=null;
                        description=null;
                        requestedValue=-1;
                        return "success";
                    }
                    else
                    {
                        return "index";
                    }
                }
                else
                {
                    return "stay";
                }
            }
            else
            {
                return "noservice";
            }
        }
        else
        {
            return "login";
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
