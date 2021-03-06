package model;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import model.*;

import java.rmi.*;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by miguel and maria
 */
public interface RMI extends Remote
{
    public String printTest() throws RemoteException;
    public ArrayList<Project> actualProjects() throws RemoteException;
    public ArrayList<Project> oldProjects() throws RemoteException;
    public Project projectDetail(Project project) throws RemoteException;
    public User makeRegist(User user) throws RemoteException;
    public User makeLogin(User user) throws RemoteException;
    //public int consultMoney(User user) throws RemoteException; -> Na classe User recebe o saldo quando o login
    public ArrayList<Reward> projectRewards(Project project) throws RemoteException;
    public ArrayList<ProductType> productTypes(Project project) throws RemoteException;
    public boolean donateMoney(User user, Project project, ProductType productType, int moneyGiven) throws RemoteException;
    public boolean sendMessage(Message message) throws RemoteException;
    public boolean createProject(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes) throws RemoteException;
    public boolean addReward(User user, Project project, Reward reward) throws RemoteException;
    public boolean removeReward(User user, Project project, int rewardID) throws RemoteException;
    public boolean cancelProject(User user, Project project) throws RemoteException;
    public boolean replyMessage(Message message, Reply reply) throws RemoteException;
    public ArrayList<Reward> getUserRewards(User user) throws RemoteException;
    public ArrayList<Message> getProjectMessages(Project project) throws RemoteException;
    public ArrayList<Reply> getReplyMessage(Message message) throws RemoteException;
    public ArrayList<Project> getMyProjects(User user) throws RemoteException;
    public ArrayList<Project> getMyEndedProjects(User user) throws RemoteException;
    public boolean setFinalProduct(Project project, String text) throws RemoteException;
    public ArrayList<Message> getMySendMessages(User user) throws RemoteException;
    public int renewMoney(User user) throws RemoteException;
    public ArrayList<Reward> getUserRewardsFuture(User user) throws RemoteException;
    public User getUserByID(User user) throws RemoteException;
    public int getLastMessageID() throws RemoteException;
    public int getLastReplyID() throws RemoteException;
    public String getUsernameByMessage(int messageID) throws RemoteException;
    public boolean setPostIDProject(Long postID) throws RemoteException;
}
