package model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SessionModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	private RMI rmiConnection;
	private boolean connection = false;
	private User user;
	
	public SessionModel()
	{
		String ip = "localhost";
		try
		{
			System.getProperties().put("java.security.policy", "politics.policy");
            int rmiport = 7697;
            String name = "rmi://"+ip+":"+rmiport+"/DB";
            System.setProperty("java.rmi.server.hostname", ip);
            rmiConnection = (RMI) Naming.lookup(name);
			System.out.println(rmiConnection.printTest());
			connection = true;
		}
        catch (NotBoundException e)
        {
            System.err.println("RMI Not Bound Exception:" + e);
			connection = false;
        }
        catch (RemoteException e)
        {
            System.err.println("RMI is drinking beers, wait for him");
			connection = false;
        }
        catch (MalformedURLException e)
        {
            System.err.println("RMI Malformed URL Exception");
			connection = false;
        }
	}
	
	public boolean login(String mail, String password)
	{
		if(connection)
		{
			try
			{
				user = new User(mail,password);
				if((user=rmiConnection.makeLogin(user))==null)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (RemoteException ex)
			{
				System.err.println("Error on login, Remote Exeption: "+ ex);
				connection = false;
				return false;
			}
		}
		return false;
	}

	public boolean regist(String username, String mail, String password)
	{
		if(connection)
		{
			try
			{
				user = new User(username,mail,password);
				user=rmiConnection.makeRegist(user);
				if(user.getUsernameID()==-1)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (RemoteException ex)
			{
				System.err.println("Error regist, Remote Exception: "+ex);
				connection = false;
				return false;
			}
		}
		return false;
	}
	
	public RMI getRmiConnection()
	{
		return rmiConnection;
	}
	
	public User getUser()
	{
		return user;
	}
}
