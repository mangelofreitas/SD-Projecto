import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
            User user = new User("teste@coisa.com","321");
            user = rmiConnection.makeLogin(user);
            if(user==null)
            {
                System.out.println("fail!");
            }
            else
            {
                System.out.println("Username:"+user.getUsername()+"\nWith ID:"+user.getUsernameID()+"\nCash:"+user.getMoney());
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception:"+e);
        }
    }
}
