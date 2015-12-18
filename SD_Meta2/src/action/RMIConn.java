package action;

import model.RMI;
import model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class RMIConn
{
    private static RMI rmiConnection;
    private static String ip = "localhost";
    private static ArrayList<WaitNotification> waitNotifications = new ArrayList<>();
    private static ArrayList<WaitMessage> waitMessages = new ArrayList<>();

    public static void main(String args[])
    {
        connectToRMI();
        ServerSocket listenSocket = null;
        try
        {
            listenSocket = new ServerSocket(6000);
        }
        catch (IOException e)
        {
            System.err.println("IOException: "+e);
        }
        while(true)
        {
            try
            {
                Socket session = listenSocket.accept();
                boolean connect = false;
                while(connect==false)
                {
                    try
                    {
                        System.out.println("Is Connected: "+rmiConnection.printTest());
                        connect = true;
                    }
                    catch(RemoteException e)
                    {
                        System.out.println("RMI Down");
                        rmiConnection = null;
                        connectToRMI();
                    }
                }
                new Connection(rmiConnection,session,waitNotifications,waitMessages);
            }
            catch (IOException e)
            {
                System.err.println("IOException: "+e);
            }
        }

    }

    public static void connectToRMI()
    {
        try
        {
            RMIConnection thread = new RMIConnection(rmiConnection,ip);
            thread.join();
            rmiConnection = thread.getRmiConnection();
        }
        catch (InterruptedException e)
        {
            System.err.println("Interruption Exception: "+e);
        }

    }
}



