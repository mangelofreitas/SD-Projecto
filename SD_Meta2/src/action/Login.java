package action;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import java.util.Scanner;

import org.apache.struts2.interceptor.SessionAware;
import model.SessionModel;

public class Login extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private String mail = null, password = null, oauth_verifier = null, loginType=null;

	private final String API_APP_KEY = "lgwSHsxW7wW6lDW0Ne5FIHjUzgkum2nmsosIqkuPjjXIXy7UM1";
	private final String API_APP_SECRET = "uqC2XhRUfTjgaNDdr4jmJwe3xIK6UKUtG24J7w7iigHVR4oUSd";

	private final String API_USER_TOKEN = "";
	private final String API_USER_SECRET = "";

	private OAuthService service;
	private Token requestToken;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public String execute() {
		SessionModel user = getModel();
		session.remove("user");
		if (user.getRmiConnection() != null)
		{
			if(mail != null && password != null)
			{
				if (user.login(mail, password) != false)
				{
					session.put("user", user);
					session.put("tipo", "login");
					mail=null;
					password=null;
					return "success";
				}
				else
				{
					return "login";
				}
			}
			if(oauth_verifier != null)
			{
				service = (OAuthService) session.get("service");
				requestToken = (Token) session.get("requestToken");
				System.out.println(oauth_verifier+"\n"+service+"\n"+requestToken);
				Verifier verifier = new Verifier(oauth_verifier);
				Token accessToken = service.getAccessToken(requestToken, verifier);
				System.out.println("Define API_USER_TOKEN: " + accessToken.getToken());
				System.out.println("Define API_USER_SECRET: " + accessToken.getSecret());
				Token newAccessToken = new Token( API_USER_TOKEN, API_USER_SECRET);
				return "stay";
			}
			else if(loginType != null && loginType.matches("tumblr"))
			{
				System.out.println(loginType);
				Long id = 135209607512L;

				String reblogKey = "OKnFQdiM";

				service = new ServiceBuilder()
						.provider(TumblrApi.class)
						.apiKey(API_APP_KEY)
						.apiSecret(API_APP_SECRET)
						.callback("https://student.dei.uc.pt/~mfreitas")
						.build();
				requestToken = service.getRequestToken();
				session.put("authorization",service.getAuthorizationUrl(requestToken));
				session.put("service",service);
				session.put("requestToken",requestToken);
				return "authorization";
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

	public String getOauth_verifier() {
		return oauth_verifier;
	}

	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
}
