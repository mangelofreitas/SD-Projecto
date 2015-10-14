import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.sql.Date;
import java.util.Scanner;

/**
 * Created by miguel on 13/10/2015.
 */
public class Teste extends UnicastRemoteObject
{
    public static RMI rmiConnection;
    protected Teste() throws RemoteException
    {
        super();
    }

    public static void main(String args[])
    {
        System.getProperties().put("java.security.policy", "politics.policy");
        System.setSecurityManager(new SecurityManager());
        Scanner sc = new Scanner(System.in);
        int rmiport = 7697;
        String name = "DB";
        try
        {
            Registry regis = LocateRegistry.getRegistry(rmiport);
            rmiConnection = (RMI) regis.lookup(name);
            System.out.println("Connected to RMI");
        } catch (Exception e) {
            System.err.println("RMI connection:"+e);
        }

        try
        {
            User user = new User("bla@cenas.com","123");
            user = rmiConnection.makeLogin(user);
            if(user==null)
            {
                System.out.println("fail!");
            }
            else
            {
                System.out.println("Username:" + user.getUsername() + "\nWith ID:" + user.getUsernameID() + "\nCash:" + user.getMoney());
                Project project = new Project();
                project.setProjectID(1);
                Reward reward = new Reward();
                System.out.println("Name:");
                reward.setName(sc.nextLine());
                System.out.println("Description:");
                reward.setDescription(sc.nextLine());
                System.out.println("Value of Reward:");
                reward.setValueOfReward(sc.nextInt());
                sc.nextLine();
                rmiConnection.addReward(user,project,reward);
                /*//Criar o projecto
                System.out.println(">>>Create Project<<<\nProject Name:");
                project.setProjectName(sc.nextLine());
                System.out.println("Description:");
                project.setDescription(sc.nextLine());
                //System.out.println("Date Limit:");
                project.setDateLimit(new Date(2015));
                System.out.println("Requested Value:");
                project.setRequestedValue(sc.nextInt());
                sc.nextLine();
                System.out.println("Types of object (ex: azul, vermelho...)");
                while(true)
                {
                    ProductType productType = new ProductType();
                    System.out.println("Type:");
                    productType.setType(sc.nextLine());
                    project.getProductTypes().add(productType);
                    System.out.println("Another? (Y/N)");
                    if(sc.nextLine().equals("N"))
                    {
                        break;
                    }
                }
                System.out.println("Rewards of project");
                while (true)
                {
                    Reward reward = new Reward();
                    System.out.println("Name:");
                    reward.setName(sc.nextLine());
                    System.out.println("Description:");
                    reward.setDescription(sc.nextLine());
                    System.out.println("Value of Reward:");
                    reward.setValueOfReward(sc.nextInt());
                    sc.nextLine();
                    project.getRewards().add(reward);
                    System.out.println("Another? (Y/N)");
                    if(sc.nextLine().equals("N"))
                    {
                        break;
                    }
                }
                System.out.println("Creating project...");
                rmiConnection.createProject(user, project.getProjectName(), project.getDescription(), project.getDateLimit(), project.getRequestedValue(),project.getRewards(), project.getProductTypes());*/
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception:"+e);
        }
    }
}
