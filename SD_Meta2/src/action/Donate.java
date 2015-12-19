package action;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import model.Message;
import model.Project;
import model.SessionModel;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by mianj on 11/12/2015.
 */
public class Donate extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> session;
    private int valuedonate=-1,producttypechoose=-1,projectID=-1;
    private static final String API_APP_KEY = "JzPwkGfrxOa0nbxx1G4JxqL5qkEBFLGUmlfFlcUmdSsIFfpmxc";

    public void setSession(Map<String, Object> session)
    {
        this.session = session;
    }

    public String execute()
    {
        SessionModel user = getModel();
        if(user.getUser()!=null)
        {
            if(user.getRmiConnection()!=null)
            {
                if(valuedonate!=-1 && producttypechoose !=-1 && projectID!=-1)
                {
                    if(user.donate(producttypechoose,projectID,valuedonate)==false)
                    {
                        return "index";
                    }
                    else
                    {
                        if(user.getUser().getMail().contains("tumblr.com"))
                        {
                            int get_inside = 0;
                            String reblogKey = null;
                            String project_post_id = ""+user.getProjectByID(projectID).getPostID();
                            if(project_post_id.compareTo("0")!=0)
                            {
                                System.out.println("project_post_id:"+project_post_id);
                                String blogName = user.getUsernameByProject(projectID);
                                OAuthService service = (OAuthService) session.get("service");
                                String API_USER_TOKEN = (String) session.get("accessTokenKey");
                                String API_USER_SECRET = (String) session.get("accessTokenSecret");
                                Token accessToken = new Token( API_USER_TOKEN, API_USER_SECRET);

                                try
                                {
                                    String getReblogKey = "https://api.tumblr.com/v2/blog/"+blogName+".tumblr.com/posts/text?api_key="+API_APP_KEY;
                                    OAuthRequest reblogKeyRequest = new OAuthRequest(Verb.GET,getReblogKey,service);
                                    Response reblogKeyResponse = reblogKeyRequest.send();
                                    service.signRequest(accessToken, reblogKeyRequest);
                                    JSONObject inf = (JSONObject) JSONValue.parse(reblogKeyResponse.getBody());
                                    JSONArray arr = (JSONArray) ((JSONObject) inf.get("response")).get("posts");
                                    for(int i=0; i< arr.size(); i++)
                                    {
                                        JSONObject item = (JSONObject) arr.get(i);
                                        if ((item.get("id").toString()).compareTo(project_post_id) == 0)
                                        {
                                            System.out.println("ENCONTREI O PAR PERFEITO: " + item.get("reblog_key"));
                                            reblogKey = (String)item.get("reblog_key");
                                            get_inside = 1;
                                            break;
                                        }
                                    }
                                    if(get_inside==1)
                                    {
                                        OAuthRequest requestLike = new OAuthRequest(Verb.POST, "https://api.tumblr.com/v2/user/like", service);
                                        requestLike.addBodyParameter("id", project_post_id.toString());
                                        requestLike.addBodyParameter("reblog_key",reblogKey);
                                        requestLike.addHeader("Accept", "application/json");
                                        service.signRequest(accessToken, requestLike);
                                        Response responseLike = requestLike.send();
                                        System.out.println("Got it! Lets see what we found...");
                                        System.out.println("HTTP RESPONSE: =============");
                                        System.out.println(responseLike.getCode());
                                        System.out.println(responseLike.getBody());
                                        System.out.println("END RESPONSE ===============");
                                    }
                                }
                                catch(OAuthException e)
                                {
                                    System.err.println("OAuthException: "+e);
                                }
                            }
                        }
                        ArrayList<Project> projects = user.getActualProjects();
                        for(int i=0;i<projects.size();i++)
                        {
                            for (int j = 0; j < projects.size(); j++)
                            {
                                if (projects.get(j).getUser().getUsernameID() == user.getUser().getUsernameID())
                                {
                                    projects.remove(projects.get(j));
                                    j--;
                                }
                            }
                            ArrayList<Message> messages = user.getMessagesProject(projects.get(i).getProjectID());
                            projects.get(i).setMessages(messages);
                        }
                        valuedonate=-1;
                        producttypechoose=-1;
                        projectID=-1;
                        session.put("user",user);
                        session.put("projects",projects);
                        return "success";
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

    public int getValuedonate() {
        return valuedonate;
    }

    public void setValuedonate(String valuedonate) {
        this.valuedonate = Integer.parseInt(valuedonate);
    }

    public int getProducttypechoose() {
        return producttypechoose;
    }

    public void setProducttypechoose(String producttypechoose) {
        this.producttypechoose = Integer.parseInt(producttypechoose);
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = Integer.parseInt(projectID);
    }
}
