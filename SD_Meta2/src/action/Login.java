package action;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import model.SessionModel;

public class Login extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private String mail = null, password = null;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public String execute()
	{
		SessionModel user = getModel();
		session.remove("user");
		if(user.getRmiConnection()!=null && user.login(mail, password)!=false)
		{
			session.put("user", user);
			session.put("tipo","login");
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
	
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getMail()
	{
		return mail;
	}
	
	public String getPassword()
	{
		return password;
	}
}
