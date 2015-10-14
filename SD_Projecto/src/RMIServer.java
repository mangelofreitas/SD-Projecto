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
    private ResultSet rs = null;
    private String query;

    public RMIServer() throws RemoteException
    {
        super();
        connectDB();
    }

    public ArrayList<Project> actualProjects() throws RemoteException {
        System.out.println("Actual Projects!");
        return null;
    }

    public ArrayList<Project> oldProjects() throws RemoteException {
        System.out.println("Old Projects!");
        return null;
    }

    public Project projectDetail(Project project) throws RemoteException {
        System.out.println("Project Detail of "+project.getProjectID());
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            rs = preparedStatement.executeQuery();
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
            query = "INSERT INTO users (username, mail, password, money) VALUES (?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2,user.getMail());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setInt(4,100);
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

    public User makeLogin(User user) throws RemoteException {
        System.out.println("Make Login of "+user.getMail());
        try
        {
            query = "SELECT usernameID, username, money FROM users WHERE mail=? AND password=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, user.getMail());
            preparedStatement.setString(2,user.getPassword());
            rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                User newUser = new User(rs.getInt("usernameID"),rs.getString("username"),user.getMail(),user.getPassword(),rs.getInt("money"));
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
            query = "SELECT votes, type FROM types_products WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            rs = preparedStatement.executeQuery();
            ArrayList<ProductType> productTypes = new ArrayList<ProductType>();
            while(rs.next())
            {
                productTypes.add(new ProductType(rs.getString("type"), rs.getInt("votes")));
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
            query = "SELECT name, description, valueOfReward FROM rewards WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            rs = preparedStatement.executeQuery();
            ArrayList<Reward> rewards = new ArrayList<Reward>();
            while(rs.next())
            {
                rewards.add(new Reward(rs.getString("name"),rs.getString("description"),rs.getInt("valueOfReward")));
            }
            return rewards;
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    public boolean donateMoney(User user, Project project, int alternativeChoose) throws RemoteException {
        System.out.println("Donate Money!");
        return false;
    }

    public boolean sendMensage(User user, Project project, String mensage) throws RemoteException {
        System.out.println("Send Mensage!");
        return false;
    }

    public boolean createProject(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes) throws RemoteException {
        System.out.println("Create Project!");
        try
        {
            query = "INSERT INTO projects (usernameID, projectName, description, dateLimit, requestedValue, currentAmount, alive) VALUES(?,?,?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            preparedStatement.setString(2, projectName);
            preparedStatement.setString(3, description);
            preparedStatement.setDate(4, dateLimit);
            preparedStatement.setInt(5, requestedValue);
            preparedStatement.setInt(6, 0);
            preparedStatement.setBoolean(7, true);
            int result =preparedStatement.executeUpdate();
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
            rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "INSERT INTO rewards (projectID, name, description, valueOfReward) VALUES (?,?,?,?)";
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, project.getProjectID());
                preparedStatement.setString(2,reward.getName());
                preparedStatement.setString(3,reward.getDescription());
                preparedStatement.setInt(4,reward.getValueOfReward());
                int result = preparedStatement.executeUpdate();
                if(result==1)
                {
                    return true;
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    public boolean removeReward(User user, Project project, int rewardID) throws RemoteException {
        System.out.println("Remove Reward!");
        return false;
    }

    public boolean cancelProject(User user, Project project) throws RemoteException {
        System.out.println("Cancel Project!");
        return false;
    }

    public boolean replyMensage(User user, int mensageID, String mensage) throws RemoteException {
        System.out.println("Reply Mensage!");
        return false;
    }

    public boolean endProject(User user, Project project) throws RemoteException {
        System.out.println("End Project!");
        return false;
    }

    public Project getProjectID(String projectName, User user) throws RemoteException {
        System.out.println("Get Project ID of "+projectName);
        try
        {
            query = "SELECT projectID FROM projects WHERE projectName=? AND usernameID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,projectName);
            preparedStatement.setInt(2,user.getUsernameID());
            rs = preparedStatement.executeQuery();
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
