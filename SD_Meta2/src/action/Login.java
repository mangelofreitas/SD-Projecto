package action;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;

import model.User;
import org.apache.struts2.interceptor.SessionAware;
import model.SessionModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Login extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 1L;
	private Map<String,Object> session;
	private String mail = null, password = null, oauth_verifier = null, loginType=null;

	private static final String API_APP_KEY = "lgwSHsxW7wW6lDW0Ne5FIHjUzgkum2nmsosIqkuPjjXIXy7UM1";
	private static final String API_APP_SECRET = "uqC2XhRUfTjgaNDdr4jmJwe3xIK6UKUtG24J7w7iigHVR4oUSd";

	
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
				System.out.println(oauth_verifier);
				OAuthService service = (OAuthService) session.get("service");
				Token requestToken = (Token) session.get("requestToken");
				Verifier verifier = new Verifier(oauth_verifier);
				Token accessToken = service.getAccessToken(requestToken, verifier);
				session.put("accessTokenKey",accessToken.getToken());
				session.put("accessTokenSecret",accessToken.getSecret());
				String url = "https://api.tumblr.com/v2/user/info";
				OAuthRequest request = new OAuthRequest(Verb.GET, url, service);
				request.addHeader("Accept", "application/json");
				service.signRequest(accessToken, request);

				Response response = request.send();

				JSONObject obj = (JSONObject) JSONValue.parse(response.getBody());
				JSONObject resp = (JSONObject)obj.get("response");
				JSONObject userget = (JSONObject)resp.get("user");
				JSONArray arr = (JSONArray)userget.get("blogs");
				String username = (String)userget.get("name"), blog=null;
				if (arr.size() > 0){
					JSONObject bloginfo = (JSONObject)arr.get(0);
					blog = (String)bloginfo.get("name")+".tumblr.com";
				}
				if(user.login(blog,username)==false)
				{
					System.out.println("Não existe na bd -> a fazer registo...");
					if(user.regist(username,blog,username)!=false)
					{
						session.put("user", user);
						session.put("tipo","regist");
						return "success";
					}
				}
				return "success";

			}
			else if(loginType != null && loginType.matches("tumblr"))
			{
				OAuthService service = new ServiceBuilder()
						.provider(TumblrApi.class)
						.apiKey(API_APP_KEY)
						.apiSecret(API_APP_SECRET)
						.callback("https://student.dei.uc.pt/~mfreitas")
						.build();
				Token requestToken=null;
				try
				{
					requestToken = service.getRequestToken();
				}
				catch(OAuthException e)
				{
					System.err.println("OAuthException:" + e);
				}
				session.put("authorization", service.getAuthorizationUrl(requestToken));
				session.put("service",service);
				session.put("requestToken",requestToken);
				System.out.println("authorization");
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
