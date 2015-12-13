package model;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

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

	public boolean addReward(String description, int valueOfReward, int projectID)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				Reward reward = new Reward();
				reward.setName(description);
				reward.setDescription(description);
				reward.setValueOfReward(valueOfReward);
				if (rmiConnection.addReward(user, project, reward) == false)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (RemoteException ex) {
				System.err.println("Error on Login, Remote Exeption: " + ex);
				connection = false;
				return false;
			}
		}
		return false;
	}

	public boolean removeReward(int rewardID, int projectID)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				if (rmiConnection.removeReward(user, project, rewardID) == false)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (RemoteException ex) {
				System.err.println("Error on Login, Remote Exeption: " + ex);
				connection = false;
				return false;
			}
		}
		return false;
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
				System.err.println("Error on Login, Remote Exeption: "+ ex);
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
				System.err.println("Error Regist, Remote Exception: "+ex);
				connection = false;
				return false;
			}
		}
		return false;
	}

	public ArrayList<Project> getMyProjects()
	{
		try
		{
			return rmiConnection.getMyProjects(user);
		}
		catch (RemoteException ex)
		{
			System.err.println("Error Get My Projects, Remote Exception: "+ex);
			connection = false;
			return null;
		}
	}

	public ArrayList<Project> getActualProjects()
	{
		try
		{
			return rmiConnection.actualProjects();
		}
		catch (RemoteException ex)
		{
			System.err.println("Error Get Actual Projects, Remote Exception: "+ex);
			connection = false;
			return null;
		}
	}

	public ArrayList<Project> getOldProjects()
	{
		try
		{
			return rmiConnection.oldProjects();
		}
		catch (RemoteException ex)
		{
			System.err.println("Error Get Old Projects, Remote Exception: "+ex);
			connection = false;
			return null;
		}
	}

	public boolean createProject(String name, String description, Date dateLimit, int requestedValue, String productType[], String reward[], int valueReward[])
	{
		if(connection)
		{
			try
			{
				ArrayList<ProductType> productTypes = new ArrayList<>();
				for(int i=0;i<productType.length;i++)
				{
					System.out.println(productType[i]);
					ProductType ptype = new ProductType();
					ptype.setType(productType[i]);
					productTypes.add(ptype);
				}
				ArrayList<Reward> rewards = new ArrayList<>();
				for(int i=0;i<reward.length;i++)
				{
					System.out.println(reward[i]+":"+valueReward[i]);
					Reward rwrd = new Reward();
					rwrd.setName(reward[i]);
					rwrd.setDescription(reward[i]);
					rwrd.setValueOfReward(valueReward[i]);
				}
				if(rmiConnection.createProject(user,name,description,dateLimit,requestedValue,rewards,productTypes)==true)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Create Project, Remote Exception: "+ex);
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
