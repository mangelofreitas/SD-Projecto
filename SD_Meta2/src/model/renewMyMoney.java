package model;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by mianj on 18/12/2015.
 */
@ServerEndpoint(value = "/renewMyMoney",configurator = HandShake.class)
public class renewMyMoney
{
    private SessionModel user;
    private Session session;
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config)
    {
        this.session = session;
        this.httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        this.user = (SessionModel)httpSession.getAttribute("user");
    }

    @OnClose
    public void onClose()
    {

    }

    @OnMessage
    public void onMessage(String text)
    {
        try
        {
            this.session.getBasicRemote().sendText(""+user.renewMoneyOfUser());
        }
        catch (IOException e)
        {
            System.err.println("IO Exception: "+e);
        }
    }
}
