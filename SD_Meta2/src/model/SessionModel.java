package model;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;

public class SessionModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	private RMI rmiConnection;
	private boolean conclude = false;
	private User user;
	private String ip = "localhost";
	private DataInputStream in;
	private DataOutputStream out;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	private Socket s;
	private ArrayList<WaitNotification> waitNotifications;
	private ArrayList<WaitMessage> waitMessages;

	public SessionModel()
	{
		createSocket();
	}

	public void createSocket()
	{
		boolean notIO = false;
		while(!notIO)
		{
			try {
				s = new Socket("localhost", 6000);

				in = new DataInputStream(s.getInputStream());
				out = new DataOutputStream(s.getOutputStream());
				objOut = new ObjectOutputStream(out);
				objIn = new ObjectInputStream(in);
				try
				{
					rmiConnection = (RMI)objIn.readObject();
					waitNotifications = (ArrayList<WaitNotification>)objIn.readObject();
					waitMessages = (ArrayList<WaitMessage>)objIn.readObject();
					System.out.println(waitNotifications);
					System.out.println(waitMessages);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				notIO = true;
			}
			catch (UnknownHostException e)
			{
				System.out.println("Sock:" + e.getMessage());
			}
			catch (EOFException e)
			{
				System.out.println("EOF:" + e.getMessage());
			}
			catch (IOException e)
			{
				System.out.println("IO:" + e.getMessage());
			}
		}

	}

	public void tryConnectionAgain()
	{
		try
		{
			objOut.writeObject(true);
			createSocket();
		}
		catch (IOException e)
		{
			System.err.println("Connection:" + e);
		}
	}

	public Project getProjectByID(int projectid)
	{
		while(conclude==false)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectid);
				return rmiConnection.projectDetail(project);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get My Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}


	public ArrayList<Message> getMessagesProject(int projectid)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return null;
	}

	public String getUsernameByProject(int projectid)
	{
		while(conclude==false)
		{
			try
			{
				Project project = new Project();
				project.setProjectID(projectid);
				project = rmiConnection.projectDetail(project);
				User user = new User(project.getUser().getUsernameID());
				user = rmiConnection.getUserByID(user);
				return user.getUsername();
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get My Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public boolean addReward(String description, int valueOfReward, int projectID)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean removeReward(int rewardID, int projectID)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean cancelProject(int projectID)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean sendMessage(String message, int projectID)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean sendReply(String message, int projectID, int messageID)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean login(String mail, String password)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean regist(String username, String mail, String password)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public ArrayList<Project> getMyProjects()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.getMyProjects(user);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get My Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public ArrayList<Project> getActualProjects()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.actualProjects();
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Actual Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public ArrayList<Reward> getUserRewards()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.getUserRewardsFuture(user);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public ArrayList<Project> getOldProjects()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.oldProjects();
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public int renewMoneyOfUser()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.renewMoney(this.user);
			}
			catch (RemoteException e)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+e);
				tryConnectionAgain();
			}
		}
		return -1;
	}

	public boolean donate(int productTypeID, int projectID, int value)
	{
		while(conclude==false)
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public boolean createProject(String name, String description, Date dateLimit, int requestedValue, String productType[], String reward[], int valueReward[])
	{
		while(conclude==false)
		{
			try
			{
				ArrayList<ProductType> productTypes = new ArrayList<>();
				for(int i=0;i<productType.length;i++)
				{
					ProductType ptype = new ProductType();
					ptype.setType(productType[i]);
					productTypes.add(ptype);
				}
				ArrayList<Reward> rewards = new ArrayList<>();
				for(int i=0;i<reward.length;i++)
				{
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
				tryConnectionAgain();
			}
		}
		return false;
	}

	public void addWaitMessage(String username, String message)
	{
		try
		{
			objOut.writeObject(false);
			objOut.writeObject("addMessage");
			objOut.writeObject(message);
			objOut.writeObject(username);
		}
		catch (IOException e)
		{
			System.err.println("Connection:" + e);
		}
	}

	public void addWaitNotification(String username, String message)
	{
		try
		{
			objOut.writeObject(false);
			objOut.writeObject("addNotification");
			objOut.writeObject(message);
			objOut.writeObject(username);
		}
		catch (IOException e)
		{
			System.err.println("Connection:" + e);
		}
	}

	public void removeWaitMessage(int id)
	{
		try
		{
			objOut.writeObject(false);
			objOut.writeObject("removeMessage");
			objOut.writeObject(id);
		}
		catch (IOException e)
		{
			System.err.println("Connection:" + e);
		}
	}

	public void removeWaitNotification(int id)
	{
		try
		{
			objOut.writeObject(false);
			objOut.writeObject("removeNotification");
			objOut.writeObject(id);
		}
		catch (IOException e)
		{
			System.err.println("Connection:" + e);
		}
	}

	public int getLastMessageID()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.getLastMessageID();
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return -1;
	}

	public String getUsernameByMessage(int messageID)
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.getUsernameByMessage(messageID);
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return null;
	}

	public int getLastReplyID()
	{
		while(conclude==false)
		{
			try
			{
				return rmiConnection.getLastReplyID();
			}
			catch (RemoteException ex)
			{
				System.err.println("Error Get Old Projects, Remote Exception: "+ex);
				tryConnectionAgain();
			}
		}
		return -1;
	}

	public RMI getRmiConnection()
	{
		return rmiConnection;
	}

	public User getUser()
	{
		return user;
	}

	public ArrayList<WaitNotification> getWaitNotifications() {
		return waitNotifications;
	}

	public void setWaitNotifications(ArrayList<WaitNotification> waitNotifications) {
		this.waitNotifications = waitNotifications;
	}

	public ArrayList<WaitMessage> getWaitMessages() {
		return waitMessages;
	}

	public void setWaitMessages(ArrayList<WaitMessage> waitMessages) {
		this.waitMessages = waitMessages;
	}
}
