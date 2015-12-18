package model;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/message",configurator = HandShake.class)
public class Chat
{
    private SessionModel user;
    private Session session;
    private HttpSession httpSession;
    private static Set<Chat> connections = new CopyOnWriteArraySet<Chat>();

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
        String type = split[0];
        int differedID = Integer.parseInt(split[1]);
        int id = -1;
        String username = null;
        if(type.matches("message"))
        {
            username = user.getUsernameByProject(differedID);
            id = user.getLastMessageID()+1;
        }
        else if(type.matches("reply"))
        {
            username = user.getUsernameByMessage(differedID);
            id = user.getLastReplyID()+1;
        }
        notify(id,type,username);
    }

    public void notifyWait()
    {
        for(Chat client:connections)
        {
            if(user==null || user.getWaitMessages()==null)
            {
                return;
            }
            for(int i=0;i<user.getWaitMessages().size();i++)
            {
                try
                {
                    synchronized (client)
                    {
                        if (client.user.getUser().getUsername().matches(user.getWaitMessages().get(i).getUsername()))
                        {
                            client.session.getBasicRemote().sendText(user.getWaitMessages().get(i).getMessage());
                            user.getWaitMessages().remove(i);
                            user.removeWaitMessage(i);
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

    public void notify(int id, String type, String user)
    {
        for(Chat client:connections)
        {
            try
            {
                boolean send = false;
                synchronized (client)
                {
                    if (client.user.getUser().getUsername().matches(user))
                    {
                        if(type.matches("message"))
                        {
                            client.session.getBasicRemote().sendText(type+"_"+id+":You have received a message from "+this.user.getUser().getUsername());
                            System.out.println(type+"_"+id+":You have received a message from "+this.user.getUser().getUsername());
                            send = true;
                        }
                        else if(type.matches("reply"))
                        {
                            client.session.getBasicRemote().sendText(type+"_"+id+":You have received a reply from "+this.user.getUser().getUsername());
                            System.out.println(type+"_"+id+":You have received a reply from "+this.user.getUser().getUsername());
                            send = true;
                        }

                    }
                }
                if(send == false)
                {
                    if(type.matches("message"))
                    {
                        this.user.addWaitMessage(user,type+"_"+id+":You have received a message from "+this.user.getUser().getUsername());
                    }
                    else if(type.matches("reply"))
                    {
                        this.user.addWaitMessage(user,type+"_"+id+":You have received a reply from "+this.user.getUser().getUsername());
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
