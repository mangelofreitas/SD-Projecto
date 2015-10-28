import javax.xml.transform.Result;
import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by miguel and maria
 */


public class RMIServer implements RMI
{
    private Connection conn = null;
    private PreparedStatement preparedStatement = null;
    private String query;

    public RMIServer() throws RemoteException
    {
        super();
        connectDB();
        new refreshDate().start();
    }

    public class refreshDate extends Thread
    {
        public void run()
        {
            while(true)
            {
                try
                {
                    ArrayList<Project> projects= actualProjects();
                    for(int i=0; i<projects.size(); i++){
                        endProject(projects.get(i));
                    }
                    sleep(50000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public User getUserByID(User user) throws RemoteException
    {
        try
        {
            query = "SELECT username, mail FROM users WHERE usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                user.setUsername(rs.getString("username"));
                user.setMail(rs.getString("mail"));
                return user;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public String printTest() throws RemoteException
    {
        System.out.println("Connected to TCP Server");
        return "Connected to RMI";
    }

    public ArrayList<Project> actualProjects() throws RemoteException {
        System.out.println("Actual Projects!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE alive=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                User user = new User(rs.getInt("usernameID"));
                user = getUserByID(user);
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(projectTypes(newProject));
                projects.add(newProject);
            }
            return projects;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public ArrayList<Project> oldProjects() throws RemoteException {
        System.out.println("Old Projects!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE alive=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                User user = new User(rs.getInt("usernameID"));
                user = getUserByID(user);
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(projectTypes(newProject));
                projects.add(newProject);
            }
            return projects;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public Project projectDetail(Project project) throws RemoteException {
        System.out.println("Project Detail of "+project.getProjectID());
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                Project newProject = new Project(new User(rs.getInt("usernameID")), rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"), projectRewards(project), projectTypes(project));
                return newProject;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public User makeRegist(User user) throws RemoteException {
        System.out.println("Make Regist of "+user.getMail());
        try
        {
            query = "SELECT * FROM users WHERE username = ? OR mail = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2,user.getMail());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {

                return new User(-1);
            }
            query = "INSERT INTO users (username, mail, password, money) VALUES (?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getMail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, 100);
            preparedStatement.executeUpdate();
            user = makeLogin(user);
            return user;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }
        return null;
    }

    public ArrayList<Reward> getUserRewards(User user) throws RemoteException
    {
        System.out.println("Get User Rewards");
        try
        {
            query = "SELECT rewards.rewardID, rewards.name, rewards.description, rewards.valueOfReward FROM users_contributes,rewards, projects, users WHERE users_contributes.rewardID = rewards.rewardID AND users.usernameID = users_contributes.usernameID AND projects.projectID = users_contributes.projectID AND projects.alive = ? AND projects.success = ? AND users_contributes.usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setBoolean(2, true);
            preparedStatement.setInt(3, user.getUsernameID());
            ArrayList<Reward> rewards = new ArrayList<Reward>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                rewards.add(new Reward(rs.getString("name"),rs.getString("description"),rs.getInt("valueOfReward"),rs.getInt("rewardID")));
            }
            return rewards;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }


    public User makeLogin(User user) throws RemoteException {
        System.out.println("Make Login of "+user.getMail());
        try
        {
            query = "SELECT usernameID, username, money FROM users WHERE mail=? AND password=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getMail());
            preparedStatement.setString(2, user.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                User newUser = new User(rs.getInt("usernameID"),rs.getString("username"),user.getMail(),user.getPassword(),rs.getInt("money"),getUserRewards(new User(rs.getInt("usernameID"))));
                return newUser;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public ArrayList<ProductType> projectTypes(Project project) throws RemoteException
    {
        System.out.println("Project Types of "+project.getProjectID());
        try
        {
            query = "SELECT votes, type, typeProductID FROM types_products WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
            while(rs.next())
            {
                productTypes.add(new ProductType(rs.getString("type"), rs.getInt("votes"),rs.getInt("typeProductID")));
            }
            return productTypes;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }
    public ArrayList<Reward> projectRewards(Project project) throws RemoteException {
        System.out.println("Project Rewards of "+project.getProjectID());
        try
        {
            query = "SELECT rewardID, name, description, valueOfReward FROM rewards WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            ArrayList<Reward> rewards = new ArrayList<Reward>();
            while(rs.next())
            {
                rewards.add(new Reward(rs.getString("name"),rs.getString("description"),rs.getInt("valueOfReward"),rs.getInt("rewardID")));
            }
            return rewards;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public boolean donateMoney(User user, Project project, ProductType productType, int moneyGiven) throws RemoteException {
        System.out.println("Donate Money!");
        if(user.getMoney()-moneyGiven<0)
        {
            return false;
        }
        try
        {
            query = "INSERT INTO users_contributes (projectID, usernameID, moneyGiven, typeProductID, rewardID) VALUES (?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            preparedStatement.setInt(2,user.getUsernameID());
            preparedStatement.setInt(3,moneyGiven);
            preparedStatement.setInt(4,productType.getProductTypeID());
            for(int i=0;i<project.getRewards().size();i++)
            {
                if(moneyGiven==project.getRewards().get(i).getValueOfReward())
                {
                    preparedStatement.setInt(5,project.getRewards().get(i).getRewardID());
                    break;
                }
            }
            int result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            query = "UPDATE users SET money = ? WHERE usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            user.setMoney(user.getMoney() - moneyGiven);
            preparedStatement.setInt(1, user.getMoney());
            preparedStatement.setInt(2, user.getUsernameID());
            result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            query = "UPDATE types_products SET votes = ? WHERE typeProductID = ?";
            preparedStatement = conn.prepareStatement(query);
            productType.setVote(productType.getVote() + 1);
            preparedStatement.setInt(1, productType.getVote());
            preparedStatement.setInt(2, productType.getProductTypeID());
            result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            query = "UPDATE projects SET currentAmount = ? WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            project.setCurrentAmount(project.getCurrentAmount()+moneyGiven);
            preparedStatement.setInt(1, project.getCurrentAmount());
            preparedStatement.setInt(2, project.getProjectID());
            result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            return true;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean sendMessage(Message message) throws RemoteException {
        System.out.println("Send Message!");
        try
        {
            query = "INSERT INTO messages_send (message, projectID, usernameID) VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setInt(2,message.getProject().getProjectID());
            preparedStatement.setInt(3,message.getUser().getUsernameID());
            int result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            return true;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean createProject(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes) throws RemoteException {
        System.out.println("Create Project!");
        try
        {
            query = "INSERT INTO projects (usernameID, projectName, description, dateLimit, requestedValue, currentAmount, alive, success) VALUES (?,?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            preparedStatement.setString(2, projectName);
            preparedStatement.setString(3, description);
            preparedStatement.setDate(4, dateLimit);
            preparedStatement.setInt(5, requestedValue);
            preparedStatement.setInt(6, 0);
            preparedStatement.setBoolean(7, true);
            preparedStatement.setBoolean(8, false);
            int result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            Project thisProject = getProjectID(projectName,user);
            query = "INSERT INTO rewards (projectID, name, description, valueOfReward) VALUES (?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,thisProject.getProjectID());
            for(int i=0;i<rewards.size();i++)
            {
                preparedStatement.setString(2,rewards.get(i).getName());
                preparedStatement.setString(3,rewards.get(i).getDescription());
                preparedStatement.setInt(4, rewards.get(i).getValueOfReward());
                result = preparedStatement.executeUpdate();
                if(result!=1)
                {
                    return false;
                }
            }
            query = "INSERT INTO types_products (projectID, votes, type) VALUES (?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,thisProject.getProjectID());
            preparedStatement.setInt(2, 0);
            for(int i=0;i<productTypes.size();i++)
            {
                preparedStatement.setString(3, productTypes.get(i).getType());
                result = preparedStatement.executeUpdate();
                if(result!=1)
                {
                    return false;
                }
            }
            return true;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean addReward(User user, Project project, Reward reward) throws RemoteException {
        System.out.println("Add Reward!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=? AND usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            preparedStatement.setInt(2,user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "INSERT INTO rewards (projectID, name, description, valueOfReward) VALUES (?,?,?,?)";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, project.getProjectID());
                preparedStatement.setString(2,reward.getName());
                preparedStatement.setString(3,reward.getDescription());
                preparedStatement.setInt(4,reward.getValueOfReward());
                int result = preparedStatement.executeUpdate();
                if(result!=1)
                {
                    return false;
                }
            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean removeReward(User user, Project project, int rewardID) throws RemoteException {
        System.out.println("Remove Reward!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=? AND usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            preparedStatement.setInt(2,user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "DELETE FROM rewards WHERE rewardID = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, rewardID);
                int result = preparedStatement.executeUpdate();
                if(result!=1)
                {
                    return false;
                }
            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean retrieveMoney(Project project) throws RemoteException
    {
        System.out.println("Retrieve Money to Users of "+project.getProjectName()+"!");
        try
        {
            query = "SELECT contributeID, usernameID, moneyGiven, typeProductID, rewardID FROM users_contributes WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                query = "SELECT money FROM users WHERE usernameID = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1,rs.getInt("usernameID"));
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                {
                    query = "UPDATE users SET money = ? WHERE usernameID = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, resultSet.getInt("money") + rs.getInt("moneyGiven"));
                    preparedStatement.setInt(2, rs.getInt("usernameID"));
                    int result = preparedStatement.executeUpdate();
                    if(result!=1)
                    {
                        return false;
                    }
                    query = "DELETE FROM users_contributes WHERE contributeID = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,rs.getInt("contributeID"));
                    result = preparedStatement.executeUpdate();
                    if(result!=1)
                    {
                        return false;
                    }
                }

            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean deleteMessages(Project project) throws RemoteException
    {
        try
        {
            query = "SELECT messageSendID FROM messages_send WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                if(deleteReplies(new Message(rs.getInt("messageSendID")))==false)
                {
                    return false;
                }
            }
            System.out.println("Delete Messages of "+project.getProjectName()+"!");
            query = "DELETE FROM messages_send WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean deleteReplies(Message message) throws RemoteException
    {
        System.out.println("Delete Replies of "+message.getMessageID()+"!");
        try
        {
            query = "DELETE FROM messages_reply WHERE messageSendID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, message.getMessageID());
            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean deleteRewards(Project project) throws RemoteException
    {
        System.out.println("Delete Rewards of "+project.getProjectName()+"!");
        try
        {
            query = "DELETE FROM rewards WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean deleteProductTypes(Project project) throws RemoteException
    {
        System.out.println("Delete Product Types of "+project.getProjectName()+"!");
        try
        {
            query = "DELETE FROM types_products WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean cancelProject(User user, Project project) throws RemoteException {
        System.out.println("Cancel Project "+project.getProjectName()+"!");
        try
        {
            query = "SELECT usernameID, projectID FROM projects WHERE projectID = ? AND usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            preparedStatement.setInt(2, user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                retrieveMoney(project);
                deleteProductTypes(project);
                deleteRewards(project);
                deleteMessages(project);
                query = "UPDATE projects SET alive = ? WHERE projectID = ?";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setBoolean(1,false);
                preparedStatement.setInt(2,project.getProjectID());
                int result = preparedStatement.executeUpdate();
                if (result!=1)
                {
                    return false;
                }
            }
            return true;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public ArrayList<Project> getMyProjects(User user) throws RemoteException
    {
        System.out.println("Get Projects of "+user.getUsername()+"!");
        try
        {
            query = "SELECT projectID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(projectTypes(newProject));
                projects.add(newProject);
            }
            return projects;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public boolean replyMessage(Message message, Reply reply) throws RemoteException {
        System.out.println("Reply Message!");
        try
        {
            query = "SELECT usernameID, projectID FROM projects WHERE projectID = ? AND usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,message.getProject().getProjectID());
            preparedStatement.setInt(2, reply.getUser().getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "INSERT INTO messages_reply (message, messageSendID, usernameID) VALUES (?,?,?)";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, reply.getMessage());
                preparedStatement.setInt(2,message.getMessageID());
                preparedStatement.setInt(3,reply.getUser().getUsernameID());
                message.getReplies().add(reply);
                int result = preparedStatement.executeUpdate();
                if(result!=1)
                {
                    return false;
                }
            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public ArrayList<Reply> getReplyMessage(Message message) throws RemoteException
    {
        System.out.println("Get Reply Messages of Message "+message.getMessageID()+"!");
        try
        {
            query = "SELECT messageReplyID, message, usernameID FROM messages_reply WHERE messageSendID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,message.getMessageID());
            ArrayList<Reply> replies = new ArrayList<Reply>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                replies.add(new Reply(rs.getInt("messageReplyID"),rs.getString("message"),getUserByID(new User(rs.getInt("usernameID")))));
            }
            return replies;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public ArrayList<Message> getProjectMessages(Project project) throws  RemoteException
    {
        System.out.println("Get Messages of Project "+project.getProjectName()+"!");
        try
        {
            query = "SELECT messageSendID, projectID, message, projectID, usernameID FROM messages_send WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            ArrayList<Message> messages = new ArrayList<Message>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                messages.add(new Message(rs.getInt("messageSendID"),rs.getString("message"),project,getUserByID(new User(rs.getInt("usernameID"))),getReplyMessage(new Message(rs.getInt("messageSendID")))));
            }
            return messages;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public boolean endProject(Project project) throws RemoteException {
        System.out.println("End Project "+project.getProjectName()+"!");
        try
        {
            Date dateNow = new Date(new java.util.Date().getTime());
            Date date = project.getDateLimit();
            if(dateNow.getYear()>=date.getYear() && dateNow.getMonth()>=date.getMonth() && dateNow.getDay()>date.getDay())
            {
                if(project.getCurrentAmount()>=project.getRequestedValue())
                {
                    query = "SELECT money FROM users WHERE usernameID = ?";
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,project.getUser().getUsernameID());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next())
                    {
                        query = "UPDATE users SET money = ? WHERE usernameID = ?";
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setInt(1,resultSet.getInt("money")+project.getCurrentAmount());
                        preparedStatement.setInt(2,project.getUser().getUsernameID());
                        int result = preparedStatement.executeUpdate();
                        if(result != 1)
                        {
                            return false;
                        }
                        int maxVotes = 0,maxVotesIndex = 0;
                        for(int i=0;i<project.getProductTypes().size();i++)
                        {
                            if(maxVotes<project.getProductTypes().get(i).getVote())
                            {
                                maxVotes = project.getProductTypes().get(i).getVote();
                                maxVotesIndex = i;
                            }
                        }
                        if(project.getCurrentAmount()>=project.getRequestedValue()*2 && project.getProductTypes().size()>1)
                        {
                            String type= project.getProductTypes().get(maxVotesIndex).getType()+", ";
                            project.getProductTypes().remove(maxVotesIndex);
                            for(int i=0;i<project.getProductTypes().size();i++)
                            {
                                if(maxVotes<project.getProductTypes().get(i).getVote())
                                {
                                    maxVotes = project.getProductTypes().get(i).getVote();
                                    maxVotesIndex = i;
                                }
                            }
                            type = type + project.getProductTypes().get(maxVotesIndex);
                            query = "UPDATE projects SET alive = ?, success = ?, finalProduct = ? WHERE projectID = ?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setBoolean(1,false);
                            preparedStatement.setBoolean(2,true);
                            preparedStatement.setString(3,type);
                            preparedStatement.executeUpdate();
                        }
                        else if(project.getCurrentAmount()<project.getRequestedValue()*2)
                        {
                            query = "UPDATE projects SET alive = ?, success = ?, finalProduct = ? WHERE projectID = ?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setBoolean(1,false);
                            preparedStatement.setBoolean(2,true);
                            preparedStatement.setString(3,project.getProductTypes().get(maxVotesIndex).getType());
                            preparedStatement.executeUpdate();
                        }
                    }
                }
                else
                {
                    cancelProject(project.getUser(),project);
                }
            }

            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public Project getProjectID(String projectName, User user) throws RemoteException {
        System.out.println("Get Project ID of "+projectName);
        try
        {
            query = "SELECT projectID FROM projects WHERE projectName=? AND usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            preparedStatement.setInt(2, user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                Project newProject = new Project(user,rs.getInt("projectID"));
                return newProject;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    private void connectDB()
    {
        String dataBase = "jdbc:mysql://localhost/fund_starter";
        String userdb = "root";
        String passdb = "";
        try
        {
            conn = DriverManager.getConnection(dataBase, userdb, passdb);
        }
        catch (SQLException e)
        {
            System.err.println("SQL Exception:"+e);
        }
    }

    public static void main(String args[])
    {
        System.getProperties().put("java.security.policy", "politics.policy");
        System.setSecurityManager(new SecurityManager());
        int port = 7697;
        String name = "DB";
        try
        {
            RMI rmi = new RMIServer();
            RMI stub = (RMI) UnicastRemoteObject.exportObject(rmi,0);
            Registry regis = LocateRegistry.createRegistry(port);
            regis.rebind(name, stub);
            System.out.println("RMI Server Up!");
        }
        catch(Exception e)
        {
            System.err.println("RMI exception:"+e);
        }


    }
}
