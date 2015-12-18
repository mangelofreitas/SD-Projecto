package model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIConnection extends Thread
{
    private RMI rmiConnection;
    private String ip;

    public RMIConnection(RMI rmiConnection, String ip)
    {
        this.rmiConnection = rmiConnection;
        this.ip = ip;
        this.start();
    }

    public void run()
    {
        while (rmiConnection == null)
        {
            try
            {
                System.getProperties().put("java.security.policy", "politics.policy");
                int rmiport = 7697;
                String name = "rmi://"+ip+":"+rmiport+"/DB";
                System.setProperty("java.rmi.server.hostname", ip);
                rmiConnection = (RMI) Naming.lookup(name);
                System.out.println("New Connection: "+rmiConnection.printTest());
            }
            catch (NotBoundException e)
            {
                System.err.println("model.RMI Not Bound Exception:" + e);
            }
            catch (RemoteException e)
            {
                System.err.println("model.RMI is drinking beers, wait for him");

                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e1)
                {
                    System.err.println("No sleep for you!");
                }
            }
            catch (MalformedURLException e)
            {
                System.err.println("model.RMI Malformed URL Exception");
            }
        }
    }

    public RMI getRmiConnection() {
        return rmiConnection;
    }

    public void setRmiConnection(RMI rmiConnection) {
        this.rmiConnection = rmiConnection;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}