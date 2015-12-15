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

	public ArrayList<Message> getMessagesProject(int projectid)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectid);
				return rmiConnection.getProjectMessages(project);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get My Projects, Remote Exception: "+ex);
				connection = false;
				return null;
			}
		}
		return null;
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

	public boolean cancelProject(int projectID)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				if((rmiConnection.cancelProject(user,project))==false)
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

	public boolean sendMessage(String message, int projectID)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				if(rmiConnection.sendMessage(new Message(message,project,user))==false)
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

	public boolean sendReply(String message, int projectID, int messageID)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				Message message1 = new Message();
				message1.setProject(project);
				message1.setMessageID(messageID);
				Reply reply = new Reply(message,user);
				if(rmiConnection.replyMessage(message1,reply)==false)
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
		if(connection)
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
		return null;
	}

	public ArrayList<Project> getActualProjects()
	{
		if(connection)
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
		return null;
	}

	public ArrayList<Reward> getUserRewards()
	{
		if(connection)
		{
			try
			{
				return rmiConnection.getUserRewardsFuture(user);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				connection = false;
				return null;
			}
		}
		return null;
	}

	public ArrayList<Project> getOldProjects()
	{
		if(connection)
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
		return null;
	}

	public boolean donate(int productTypeID, int projectID, int value)
	{
		if(connection)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectID);
				project = rmiConnection.projectDetail(project);
				ProductType productType = null;
				for(int i=0;i<project.getProductTypes().size();i++)
				{
					if(project.getProductTypes().get(i).getProductTypeID() == productTypeID)
					{
						productType = project.getProductTypes().get(i);
					}
				}
				if(rmiConnection.donateMoney(user,project,productType,value) && productType!=null)
				{
					int money;
					if((money=rmiConnection.renewMoney(user))!=-1)
					{
						user.setMoney(money);
						return true;
					}
				}
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				connection = false;
				return false;
			}
		}
		return false;
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
					rewards.add(rwrd);
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
