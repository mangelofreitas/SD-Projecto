package websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import model.SessionModel;

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
    }

    @OnClose
    public void onClose()
    {
        connections.remove(this);
    }

    @OnMessage
    public void onMessage(String user)
    {
        String userToNotify = this.user.getUser().getUsername();
        notify(userToNotify);
    }

    public void notify(String user)
    {
        for(Notification client:connections)
        {
            try
            {
                synchronized (client)
                {
                    if (client.user.getUser().getUsername().matches(user))
                    {
                        client.session.getBasicRemote().sendText("Recebeu uma notificação");
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
