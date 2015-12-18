package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Connection extends Thread
{
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Socket client;
    private RMI rmiConnection;
    private ArrayList<WaitNotification> waitNotifications;
    private ArrayList<WaitMessage> waitMessages;

    public Connection(RMI rmiConnection, Socket client, ArrayList<WaitNotification> waitNotifications, ArrayList<WaitMessage> waitMessages)
    {
        try
        {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
            this.client = client;
            this.rmiConnection = rmiConnection;
            this.waitNotifications = waitNotifications;
            this.waitMessages = waitMessages;
            this.start();
        }
        catch (IOException e)
        {
            System.err.println("Connection:" + e);
        }
    }

    public void run()
    {
        try
        {
            objOut.writeObject(rmiConnection);
            objOut.writeObject(waitNotifications);
            objOut.writeObject(waitMessages);
            while(true)
            {
                boolean down = (boolean)objIn.readObject();
                if(down == true)
                {
                    System.out.println("Try to reconnect");
                    client.close();
                }
                else
                {
                    String type = (String)objIn.readObject();
                    if(type.matches("addNotification"))
                    {
                        String message = (String)objIn.readObject();
                        String username = (String)objIn.readObject();
                        waitNotifications.add(new WaitNotification(message,username));
                    }
                    else if(type.matches("removeNotification"))
                    {
                        int id = (Integer)objIn.readObject();
                        waitNotifications.remove(id);
                    }
                    else if(type.matches("addMessage"))
                    {
                        String message = (String)objIn.readObject();
                        String username = (String)objIn.readObject();
                        waitMessages.add(new WaitMessage(message,username));
                    }
                    else if(type.matches("removeMessage"))
                    {
                        int id = (Integer)objIn.readObject();
                        waitMessages.remove(id);
                    }

                }
            }

        }
        catch (IOException e)
        {
            System.err.println("Connection:" + e);
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Class Not Found Exception:" + e);
        }
    }
}
