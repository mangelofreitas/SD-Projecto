import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.sql.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

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
                    sleep(1000*60*60);
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

    public int renewMoney(User user) throws RemoteException
    {
        System.out.println("Renew Money of "+user.getUsernameID()+"!");
        try
        {
            query = "SELECT money FROM users WHERE usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return rs.getInt("money");
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return -1;
    }

    public int getLastMessageID() throws RemoteException
    {
        System.out.println("Get Last MessageID!");
        try
        {
            query = "SELECT messageSendID FROM messages_send ORDER BY messageSendID DESC LIMIT 1";
            preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return rs.getInt("messageSendID");
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return -1;
    }

    public int getLastReplyID() throws RemoteException
    {
        System.out.println("Get Last ReplyID!");
        try
        {
            query = "SELECT messageReplyID FROM messages_reply ORDER BY messageReplyID DESC LIMIT 1";
            preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return rs.getInt("messageReplyID");
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return -1;
    }

    public String getUsernameByMessage(int messageID) throws RemoteException
    {
        System.out.println("Get Username By MessageID "+messageID+"!");
        try
        {
            query = "SELECT usernameID FROM messages_send WHERE messageSendID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,messageID);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return getUserByID(new User(rs.getInt("usernameID"))).getUsername();
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    /*
    * Esta classe � apenas usada para obter o nome de um utilizador no qual s� temos o ID dele,
    * como por exemplo Send Message, s� temos o ID mas ao imprimir para o cliente aparece o Nome dele.
    * **/
    public User getUserByID(User user) throws RemoteException
    {
        System.out.println("Get User By ID "+user.getUsernameID()+"!");
        try
        {
            /*query = "SELECT username, mail FROM users WHERE usernameID=?";  //criamos a query
            preparedStatement = conn.prepareStatement(query);   //realiza-mos o prepare statement da query para a conec��o anteriormente realizada � BD
            preparedStatement.setInt(1, user.getUsernameID());  //inserimos os campos respectivos aos ? do prepare statement
            ResultSet rs = preparedStatement.executeQuery();    //no caso do select fazemos o result set do executeQuery() porque vamos estar � espera de obter informa��o*/
            System.out.println("Executei grande procedure!");
            query = "CALL getUserByID(?,?,?)";
            CallableStatement cs = conn.prepareCall(query);
            cs.setInt(1,user.getUsernameID());
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.VARCHAR);
            int result = cs.executeUpdate();
            if(result==1)   //este rs.next() vai dando linha � linha vindo da tabela, neste caso s� temos uma linha fez-se if(), se n�o fazia-se while()
            {
                user.setUsername(cs.getString(2));     //rs.getString(coluna pretendida), rs.getInt(), rs.getBoolean(), rs.getDate()...
                user.setMail(cs.getString(3));
                return user;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    /*
    * Esta fun��o serve apenas para uma primeira impress�o de teste para que se possa verificar
    * que o Servidor Prim�rio se conectou ao RMI
    * **/
    public String printTest() throws RemoteException
    {
        System.out.println("Connected to TCP Server");
        return "Connected to RMI";
    }

    /*
    * Fun��o que vai buscar todos os projectos da BD onde o alive=true, ou seja, ainda n�o passaram do prazo
    * **/

    public ArrayList<Project> actualProjects() throws RemoteException
    {
        System.out.println("Actual Projects!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount,success FROM projects WHERE alive=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                User user = new User(rs.getInt("usernameID"));
                user = getUserByID(user);
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"),rs.getBoolean("success"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(productTypes(newProject));
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

    /*
    * Fun��o que vai buscar todos os projectos da BD onde o alive=false, ou seja, j� passaram do prazo
    * **/

    public ArrayList<Project> oldProjects() throws RemoteException
    {
        System.out.println("Old Projects!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount, success, finalProduct FROM projects WHERE alive=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, false);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                User user = new User(rs.getInt("usernameID"));
                user = getUserByID(user);
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"),rs.getBoolean("success"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(productTypes(newProject));
                newProject.setFinalProduct(rs.getString("finalProduct"));
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


    /*
    * Fun��o que vai buscar todos os detalhes de um determinado projecto, podendo receber apenas o projectID
    * **/

    public Project projectDetail(Project project) throws RemoteException
    {
        System.out.println("Project Detail of "+project.getProjectID());
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                Project newProject = new Project(new User(rs.getInt("usernameID")), rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"), projectRewards(project), productTypes(project));
                return newProject;
            }
        }
        catch (SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    /*
    * Fun��o que realiza o registo de um novo utilizador, verificando antes com o SELECT se j� existe uma username ou mail igual, se sim, n�o adiciona, se n�o, faz o INSERT na BD
    * **/

    public User makeRegist(User user) throws RemoteException
    {
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
            preparedStatement.setInt(4, 100);   //sempre que um novo utilizador se regista come�a com 100�
            preparedStatement.executeUpdate();  //quando o query � um INSERT, UPDATE ou DELETE usa-se o executeUpdate() em vez do executeQuery()
            user = makeLogin(user);
            return user;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:"+e);
        }
        return null;
    }

    /*
    * Esta fun��o � para ir buscar todas as rewards que o utilizador tem direito, porque fez uma doa��o a um certo
    * projecto com uma certa quantia e o projecto foi bem sucecidido caso contr�rio n�o teria reward, mas tinha o dinheiro das
    * doa��es de volta
    * **/

    public ArrayList<Reward> getUserRewards(User user)
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

    public ArrayList<Reward> getUserRewardsFuture(User user) throws RemoteException
    {
        System.out.println("Get User Rewards");
        try
        {
            query = "SELECT rewards.rewardID, rewards.name, rewards.description, rewards.valueOfReward FROM users_contributes,rewards, projects, users WHERE users_contributes.rewardID = rewards.rewardID AND users.usernameID = users_contributes.usernameID AND projects.projectID = users_contributes.projectID AND projects.alive = ? AND projects.success = ? AND users_contributes.usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setBoolean(2, false);
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

    /*
    * Fun��o que realiza o login, isto �, faz um SELECT do utilizador pelo mail e password que no TCPClient foi dado
    * se existir retorna o utilizador com o username e usernameID na classe user, caso contr�rio retorna null, informando
    * que mail ou password estavam errados
    * **/

    public User makeLogin(User user) throws RemoteException
    {
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

    /*
    * Fun��o que � apenas chamada quando queremos listar projectos com detalhes, pois esta fun��o
    * vai buscar os productTypes de um certo projecto (� uma fun��o chamada internamente, pois n�o tem o trows
     * RemoteException, nem conta no RMI.java, pois n�o � chamada remotamente nenhuma vez pelo TCPServer)
    * **/

    public ArrayList<ProductType> productTypes(Project project)
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

    /*
    * Esta fun��o � igual h� anterior no que toca a n�o ser chamada remotamente, devolve as rewards de um certo projecto
    * **/

    public ArrayList<Reward> projectRewards(Project project)
    {
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

    /*
    * Fun��o que realiza a doa��o do dinheiro, recebe o utilizador que fez a doa��o, recebe o projecto que o ir� receber,
    * o tipo de producto no qual votou e a quantidade de dinheiro que deu
    * **/

    public boolean donateMoney(User user, Project project, ProductType productType, int moneyGiven) throws RemoteException
    {
        System.out.println("Donate Money!");
        if(user.getMoney()-moneyGiven<0)
        {
            return false;
        }
        try
        {
            query = "INSERT INTO users_contributes (projectID, usernameID, moneyGiven, typeProductID, rewardID) VALUES (?,?,?,?,?)"; //inserimos na tabela de contribui��es para que mais tarde se o projecto se n�o se realizar ser mais f�cil a devolu��o do dinheiro aos respectivos users
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
            query = "UPDATE users SET money = ? WHERE usernameID = ?";  //fazemos update ao dinheiro actual do utilizador que fez a doa��o
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, (user.getMoney() - moneyGiven));
            preparedStatement.setInt(2, user.getUsernameID());
            result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            query = "UPDATE types_products SET votes = ? WHERE typeProductID = ?";  //update no n�mero de votos no qual o user que fez a doa��o votou
            preparedStatement = conn.prepareStatement(query);
            productType.setVote(productType.getVote() + 1);
            preparedStatement.setInt(1, productType.getVote());
            preparedStatement.setInt(2, productType.getProductTypeID());
            result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            query = "UPDATE projects SET currentAmount = ? WHERE projectID = ?";    //e update no amontoado de dinheiro que o projecto tem at� ao momento
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

    /*
    * Fun��o que realiza o envio de mensagens para um certo projecto por um certo user
    * **/

    public boolean sendMessage(Message message) throws RemoteException {
        System.out.println("Send Message!");
        try
        {
            query = "INSERT INTO messages_send (message, projectID, usernameID) VALUES (?,?,?)";    //inserimos na tabela messages_send o conteudo da mensagem, para que projecto se referia e qual o user que a mandou
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

    /*
    * Fun��o que cria o projecto
    * **/

    public boolean createProject(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes) throws RemoteException {
        System.out.println("Create Project!");
        try
        {
            //inserimos o projecto na tabela projects com os campos respectivos
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
            query = "INSERT INTO rewards (projectID, name, description, valueOfReward) VALUES (?,?,?,?)";   //inserimos logo tamb�m as rewards
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
            query = "INSERT INTO types_products (projectID, votes, type) VALUES (?,?,?)";   //e inserimos os tipos de produtos tamb�m
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

    /*
    * Fun��o para adicionar mais uma reward a um projecto
    * **/

    public boolean addReward(User user, Project project, Reward reward) throws RemoteException
    {
        System.out.println("Add Reward!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=? AND usernameID=?";    //vamos buscar o projecto correspondente ao seleccionado e por preven��o colocamos o usernameID para ter a certeza que ele � o owner do projecto
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            preparedStatement.setInt(2,user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "INSERT INTO rewards (projectID, name, description, valueOfReward) VALUES (?,?,?,?)";   //inserimos a reward
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

    /*
    * Fun��o de remo��o de uma reward de um projecto passando o id respectivo a essa reward
    * **/

    public boolean removeReward(User user, Project project, int rewardID) throws RemoteException
    {
        System.out.println("Remove Reward!");
        try
        {
            query = "SELECT projectID, usernameID, projectName, description, dateLimit, requestedValue, currentAmount FROM projects WHERE projectID=? AND usernameID=?";    //o mesmo que a addReward()
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            preparedStatement.setInt(2,user.getUsernameID());
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                query = "DELETE FROM rewards WHERE rewardID = ?";   //removemos a reward
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

    /*
    * Fun��o que � chamada quando um projecto chega ao final e n�o � bem sucedido
    * **/

    public boolean retrieveMoney(Project project)
    {
        System.out.println("Retrieve Money to Users of "+project.getProjectName()+"!");
        try
        {
            query = "DELETE FROM users_contributes WHERE projectID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            int result = preparedStatement.executeUpdate();
            if(result!=1)
            {
                return false;
            }
            /*query = "SELECT contributeID, usernameID, moneyGiven, typeProductID, rewardID FROM users_contributes WHERE projectID = ?";  //vamos buscar todos os contributos respectivos ao projecto que acabou
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())   //entramos no ciclo dos v�rios contributos
            {
                query = "SELECT money FROM users WHERE usernameID = ?";     //vamos buscar user a user (isto porque estamos no while(rs.next()))
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1,rs.getInt("usernameID"));
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next())
                {
                    query = "UPDATE users SET money = ? WHERE usernameID = ?";  //fazemos o update no dinheiro do utilizador correspondente ao que ele deu
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, resultSet.getInt("money") + rs.getInt("moneyGiven"));
                    preparedStatement.setInt(2, rs.getInt("usernameID"));
                    int result = preparedStatement.executeUpdate();
                    if(result!=1)
                    {
                        return false;
                    }
                    query = "DELETE FROM users_contributes WHERE contributeID = ?"; //fazemos delete dos contributos �quele projecto
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,rs.getInt("contributeID"));
                    result = preparedStatement.executeUpdate();
                    if(result!=1)
                    {
                        return false;
                    }
                }

            }*/
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return false;
    }

    /*
    * Fun��o para apagar mensagens de um projecto, esta fun��o � chamada aquando a cancelProject()
    * **/

    public boolean deleteMessages(Project project)
    {
        try
        {
            query = "SELECT messageSendID FROM messages_send WHERE projectID = ?";  //vamos buscar o messageSendID, para poder apagar primeiro as replies, visto que elas t�m uma foreign key e daria erro se fosse ao contr�rio ou se tent�ssemos apagar as send e n�o as replies
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,project.getProjectID());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                if(deleteReplies(new Message(rs.getInt("messageSendID")))==false)   //executamos a fun��o de apagar as replies respectivas as send acima obtidas
                {
                    return false;
                }
            }
            System.out.println("Delete Messages of "+project.getProjectName()+"!");
            query = "DELETE FROM messages_send WHERE projectID = ?";    //apagamos as mensagens enviadas
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

    /*
    * Fun��o que era referida anteriormente, apaga as replies atrav�s do ID das send
    * **/

    public boolean deleteReplies(Message message)
    {
        System.out.println("Delete Replies of "+message.getMessageID()+"!");
        try
        {
            query = "DELETE FROM messages_reply WHERE messageSendID = ?";   //apagamos todas as replies que tenham como foreign key aquela messageSendID
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

    /*
    * Fun��o para eleminar todas as rewards respectivas a um projecto, � chamada atrav�s do cancelProject()
    * **/

    public boolean deleteRewards(Project project)
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


    /*
    * Fun��o para eleminar todos os productTypes referentes a um projecto, � chamada atrav�s do cancelProject()
    * **/

    public boolean deleteProductTypes(Project project)
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

    /*
    * Fun��o para cancelar um projecto
    * **/

    public boolean cancelProject(User user, Project project) throws RemoteException
    {
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
                query = "UPDATE projects SET alive = ? WHERE projectID = ?";    //faz update no projecto colocando alive=false, depois de eliminar tudo o que � referente o projecto
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

    /*
    * Esta fun��o � para o caso de um projecto ter obtido 5* mais do que pedia, ou seja, o String text que recebe � o tipo de producto extra que foi pedido ao user
    * **/

    public boolean setFinalProduct(Project project, String text) throws RemoteException
    {
        System.out.println("Set Final Product of "+project.getProjectName()+"!");
        try
        {
            int maxVotes = 0,maxVotesIndex = 0; //verificamos qual o tipo de produto mais votado para o colocar como producto final
            for(int i=0;i<project.getProductTypes().size();i++)
            {
                if(maxVotes<project.getProductTypes().get(i).getVote())
                {
                    maxVotes = project.getProductTypes().get(i).getVote();
                    maxVotesIndex = i;
                }
            }
            String type= project.getProductTypes().get(maxVotesIndex).getType()+", ";
            project.getProductTypes().remove(maxVotesIndex);
            if(project.getProductTypes().size()!=0)     //verificamos qual o segundo tipo de produto mais votado para o colocar como producto final tamb�m, se existir mais do que 1
            {
                maxVotes = 0;
                maxVotesIndex = 0;
                for(int i=0;i<project.getProductTypes().size();i++)
                {
                    if(maxVotes<project.getProductTypes().get(i).getVote())
                    {
                        maxVotes = project.getProductTypes().get(i).getVote();
                        maxVotesIndex = i;
                    }
                }
                type = type + project.getProductTypes().get(maxVotesIndex)+", ";
            }

            type = type + text;     //adicionamos mais o tipo de producto extra que recebemos
            query = "UPDATE projects SET finalProduct = ? WHERE projectID = ?";     //fazemos update para poder colocar o produto final
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,type);
            preparedStatement.setInt(2,project.getProjectID());
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

    /*
    * Fun��o que obtem os projectos que j� passaram do prazo, mas que foram concluidos com sucesso, esta fun��o � utilizada para
    * fins de colocar o produto final extra, caso o montante for 5* mais que o pedido
    * **/

    public ArrayList<Project> getMyEndedProjects(User user) throws RemoteException
    {
        System.out.println("Get Projects of "+user.getUsername()+"!");
        try
        {
            query = "SELECT projectID, projectName, description, dateLimit, requestedValue, currentAmount, finalProduct,success FROM projects WHERE usernameID=? AND alive = ? AND success = ? AND finalProduct IS NULL";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            preparedStatement.setBoolean(2, false);
            preparedStatement.setBoolean(3, true);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"),rs.getBoolean("success"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(productTypes(newProject));
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

    /*
    * Fun��o que obt�m os projectos de um utilizador que ainda est�o activos, para fins de adicionar rewards, remover rewards, cancelar projectos...
    * **/

    public ArrayList<Project> getMyProjects(User user) throws RemoteException
    {
        System.out.println("Get Projects of "+user.getUsername()+"!");
        try
        {
            query = "SELECT projectID, projectName, description, dateLimit, requestedValue, currentAmount, success FROM projects WHERE usernameID=? AND alive = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getUsernameID());
            preparedStatement.setBoolean(2, true);
            ArrayList<Project> projects = new ArrayList<Project>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Project newProject = new Project(user, rs.getInt("projectID"), rs.getString("projectName"), rs.getString("description"), rs.getDate("dateLimit"), rs.getInt("requestedValue"), rs.getInt("currentAmount"),rs.getBoolean("success"));
                newProject.setRewards(projectRewards(newProject));
                newProject.setProductTypes(productTypes(newProject));
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

    /*
    * Fun��o que vai buscar as mensagens que um utilizador j� mandou para poder verificar se j� obtiveram resposta
    * **/

    public ArrayList<Message> getMySendMessages(User user) throws RemoteException
    {
        System.out.println("Get "+user.getUsernameID()+" Send Messages!");
        try
        {
            query = "SELECT messageSendID, projectID, message, projectID, usernameID FROM messages_send WHERE usernameID = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1,user.getUsernameID());
            ArrayList<Message> messages = new ArrayList<Message>();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                messages.add(new Message(rs.getInt("messageSendID"),rs.getString("message"),new Project(user,rs.getInt("projectId")),user,getReplyMessage(new Message(rs.getInt("messageSendID")))));
            }
            return messages;
        }
        catch(SQLException e)
        {
            System.err.println("SQLException:" + e);
        }
        return null;
    }

    /*
    * Fun��o para enviar uma reply a uma mensagem
    * **/

    public boolean replyMessage(Message message, Reply reply) throws RemoteException
    {
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
                //message.getReplies().add(reply);
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

    /*
    * Fun��o para obter as repostas a uma mensagem
    * **/

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

    /*
    * Fun��o que obt�m todas as mensagens, send e replies de um respectivo projecto
    * **/

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

    /*
    * Fun��o que � chamada pela thread para verificar se um projecto j� passou do tempo, se sim, verificar o montoante, e se este corresponder ao pedido coloca como bem sucedidom, se n�o chama a fun��o cancela projecto
    * **/

    public boolean endProject(Project project) throws RemoteException
    {
        System.out.println("End Project "+project.getProjectName()+"!");
        try
        {
            Date dateNow = new Date(new java.util.Date().getTime());
            Date date = project.getDateLimit();
            if(dateNow != date && date.compareTo(dateNow)<0)
            {
                if(project.getCurrentAmount()>=project.getRequestedValue())
                {
                    query = "SELECT money FROM users WHERE usernameID = ?";     //obt�m o utilizador que det�m o projecto
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1,project.getUser().getUsernameID());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next())
                    {
                        query = "UPDATE users SET money = ? WHERE usernameID = ?";  //transfere o total montante que o projecto det�m para o owner dele
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
                        if(project.getCurrentAmount()>=project.getRequestedValue()*5)   //caso tenha conseguido 5* mais do que pedia
                        {
                            query = "UPDATE projects SET alive = ?, success = ? WHERE projectID = ?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setBoolean(1,false);
                            preparedStatement.setBoolean(2,true);
                            preparedStatement.setInt(3,project.getProjectID());
                            int res = preparedStatement.executeUpdate();
                            if(res!=1)
                            {
                                return false;
                            }
                        }
                        else if(project.getCurrentAmount()>=project.getRequestedValue()*2)    //caso tenha conseguido apenas 2* mais, mas n�o chegou aos 5* mais
                        {
                            String type= project.getProductTypes().get(maxVotesIndex).getType();
                            if(project.getProductTypes().size()>1)
                            {
                                project.getProductTypes().remove(maxVotesIndex);
                                maxVotes = 0;
                                maxVotesIndex = 0;
                                for(int i=0;i<project.getProductTypes().size();i++)
                                {
                                    if(maxVotes<project.getProductTypes().get(i).getVote())
                                    {
                                        maxVotes = project.getProductTypes().get(i).getVote();
                                        maxVotesIndex = i;
                                    }
                                }
                                System.out.println(maxVotesIndex);
                                type = type+", "+ project.getProductTypes().get(maxVotesIndex).getType();
                            }
                            query = "UPDATE projects SET alive = ?, success = ?, finalProduct = ? WHERE projectID = ?"; //coloca o producto final a jun��o dos dois tipos produtos mais votados se existirem mais do que um
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setBoolean(1,false);
                            preparedStatement.setBoolean(2,true);
                            preparedStatement.setString(3,type);
                            preparedStatement.setInt(4,project.getProjectID());
                            int result1 = preparedStatement.executeUpdate();
                            if(result1 !=1)
                            {
                                return false;
                            }
                        }
                        else if(project.getCurrentAmount()<project.getRequestedValue()*2)   //se apenas recolheu o pedido ou pouco mais, o produto final ser� apenas o tipo de produto mais votado
                        {
                            query = "UPDATE projects SET alive = ?, success = ?, finalProduct = ? WHERE projectID = ?";
                            preparedStatement = conn.prepareStatement(query);
                            preparedStatement.setBoolean(1,false);
                            preparedStatement.setBoolean(2,true);
                            preparedStatement.setString(3,project.getProductTypes().get(maxVotesIndex).getType());
                            preparedStatement.setInt(4,project.getProjectID());
                            int result1 = preparedStatement.executeUpdate();
                            if(result1 !=1)
                            {
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    cancelProject(project.getUser(), project);
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

    /*
    * Fun��o para apenas obter o id de um projecto
    * **/

    public Project getProjectID(String projectName, User user)
    {
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

    /*
    *   Fun��o que realiza a conec��o � base de dados
    * **/

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

    /*
    * main onde consta a conec��o do RMI no qual ele fica tipo � escuta de uma liga��o (rebind)
    * **/

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
