package model;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/notification",configurator = HandShake.class)
public class Notification
{
    private SessionModel user;
    private Session session;
    private HttpSession httpSession;
    private static Set<Notification> connections = new CopyOnWriteArraySet<Notification>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        this.httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        connections.add(this);
        this.user = (SessionModel)httpSession.getAttribute("user");
        notifyWait();
    }

    @OnClose
    public void onClose()
    {
        connections.remove(this);
    }

    @OnMessage
    public void onMessage(String text)
    {
        String split[] = text.split(" ");
        int projectID = Integer.parseInt(split[0]);
        int value = Integer.parseInt(split[1]);
        String username = user.getUsernameByProject(projectID);
        notify(username,value);
    }

    public void notifyWait()
    {
        for(Notification client:connections)
        {
            if(user == null || user.getWaitNotifications()==null)
            {
                return;
            }
            for(int i=0;i<user.getWaitNotifications().size();i++)
            {
                try
                {
                    synchronized (client)
                    {
                        if (client.user.getUser().getUsername().matches(user.getWaitNotifications().get(i).getUsername()))
                        {
                            client.session.getBasicRemote().sendText(user.getWaitNotifications().get(i).getMessage());
                            user.getWaitNotifications().remove(i);
                            user.removeWaitNotification(i);
                            i--;
                        }
                    }
                }
                catch(IOException e)
                {
                    connections.remove(client);
                    try
                    {
                        client.session.close();
                    }
                    catch (IOException e1)
                    {
                        System.err.println("IO Exception: "+e1);
                    }
                }
            }

        }
    }

    public void notify(String user,int value)
    {
        for(Notification client:connections)
        {
            try
            {
                boolean send = false;
                synchronized (client)
                {
                    if (client.user.getUser().getUsername().matches(user))
                    {
                        client.session.getBasicRemote().sendText("You have received a pledge of value "+value+"€ from "+this.user.getUser().getUsername());
                        send = true;
                    }
                }
                if(send == false)
                {
                    this.user.addWaitNotification(user,"You have received a pledge of value "+value+"€ from "+this.user.getUser().getUsername());
                }
            }
            catch(IOException e)
            {
                connections.remove(client);
                try
                {
                    client.session.close();
                }
                catch (IOException e1)
                {
                    System.err.println("IO Exception: "+e1);
                }
            }
        }
    }
}
