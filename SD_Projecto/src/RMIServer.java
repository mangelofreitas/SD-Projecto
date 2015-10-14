import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

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
        System.out.println("Project Detail!");
        return null;
    }

    public User makeRegist(User user) throws RemoteException {
        System.out.println("Make Regist of "+user.getMail());
        try
        {
            query = "INSERT INTO users (username, mail, password, money) VALUES (?,?,?,?)";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,user.getUsername());
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
            preparedStatement.setString(1,user.getMail());
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

    public ArrayList<Reward> consultRewards(User user) throws RemoteException {
        System.out.println("Consult Reward of "+user.getUsername());
        try
        {
            query = "SELECT name, description, valueOfReward FROM rewards WHERE mail=? AND password=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,user.getMail());
            preparedStatement.setString(2,user.getPassword());
            rs = preparedStatement.executeQuery();
            if(rs.next())
            {

            }
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

    public boolean createProject(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards) throws RemoteException {
        System.out.println("Create Project!");
        return false;
    }

    public boolean addReward(User user, Project project, String reward) throws RemoteException {
        System.out.println("Add Reward!");
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

    private void connectDB()
    {
        String dataBase = "jdbc:mysql://localhost/fund_starter";
        String userdb = "root";
        String passdb = "";
        try
        {
            conn = DriverManager.getConnection(dataBase,userdb,passdb);
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
