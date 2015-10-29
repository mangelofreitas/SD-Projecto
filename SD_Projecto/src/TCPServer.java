import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by miguel and maria
 */

public class TCPServer
{
    private static RMI rmiConnection;

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        try {
            int serverPort = 6000;
            int serverNumber;
            if(args.length==0)
            {
                serverNumber = 2;
            }
            else
            {
                serverNumber = Integer.parseInt(args[0]);
            }
            if(serverNumber == 1)
            {
                int numero = 0;
                ArrayList<DataOutputStream> clientes = new ArrayList<DataOutputStream>();
                String path;
                if(args.length==0)
                {
                    System.out.print("Address of server 2: ");
                    path = sc.nextLine();
                }
                else
                {
                    path = args[1];
                }
                new UDPThread(serverNumber,path);
                System.out.println("A Escuta no Porto 6000");
                ServerSocket listenSocket = new ServerSocket(serverPort);
                System.out.println("LISTEN SOCKET=" + listenSocket);
                System.out.print("IP do RMI:");
                String ip = sc.nextLine();
                while (true)
                {
                    Socket clientSocket = listenSocket.accept();
                    rmiConnection = null;
                    RMIConnection thread = new RMIConnection(rmiConnection,ip);
                    thread.join();
                    rmiConnection = thread.rmiConnection;
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    clientes.add(new DataOutputStream(clientSocket.getOutputStream()));
                    new Connection(clientSocket, numero, rmiConnection);
                }
            }
            else if(serverNumber == 2)
            {
                System.out.print("Address of the other server: ");
                String path = sc.nextLine();
                new UDPThread(serverNumber,path);
            }

        } catch (IOException e) {
            System.err.println("Listen:" + e);

        }
        catch (InterruptedException e)
        {
            System.err.println("Interruption Exception " + e);
        }
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;
    ObjectOutputStream objOut;
    ObjectInputStream objIn;
    RMI rmiConnection;
    User log = null;


    public Connection(Socket aClientSocket, int numero,  RMI rmiConnection) {
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
            this.rmiConnection = rmiConnection;

            this.start();
        } catch (IOException e) {
            System.err.println("Connection:" + e);
        }
    }

    public void run() {
        try
        {
            int choose;
            while(log==null) {
                choose = in.readInt();
                if (choose == 1)
                {
                    log = (User) objIn.readObject();
                    log = rmiConnection.makeLogin(log);
                    objOut.writeObject(log);
                    ArrayList<Project> projects = null;
                    if(log != null)
                    {
                        projects = rmiConnection.getMyEndedProjects(log);
                        objOut.writeObject(projects);
                    }
                    if(projects!=null)
                    {
                        Project project;
                        String text;
                        for(int i=0;i<projects.size();i++)
                        {
                            project = (Project)objIn.readObject();
                            text = (String) objIn.readObject();
                            rmiConnection.setFinalProduct(project,text);
                        }
                    }

                }
                else if (choose == 2)
                {
                    log = (User) objIn.readObject();
                    log = rmiConnection.makeRegist(log);
                    objOut.writeObject(log);
                    objOut.flush();
                    if(log.getUsernameID()==-1)
                    {
                        log = null;
                    }
                    if(log!=null)
                    {
                        in.readInt();
                        log = (User) objIn.readObject();
                        log = rmiConnection.makeLogin(log);
                        objOut.writeObject(log);
                    }
                }
                else if(choose == 3)
                {
                    int choose1 = in.readInt();
                    ArrayList<Project> projects = null;
                    if(choose1 == 1)
                    {
                        projects = rmiConnection.oldProjects();
                    }
                    else if(choose1 == 2)
                    {
                        projects = rmiConnection.actualProjects();
                    }
                    objOut.writeObject(projects);
                    objOut.flush();
                }
            }
            if(log!=null)
            {
                while (true)
                {
                    choose = in.readInt();
                    if(choose == 1)
                    {
                        Project newProject = (Project) objIn.readObject();
                        boolean pass = rmiConnection.createProject(log,newProject.getProjectName(),newProject.getDescription(),newProject.getDateLimit(),newProject.getRequestedValue(),newProject.getRewards(),newProject.getProductTypes());
                        out.writeBoolean(pass);
                    }
                    if(choose == 2)
                    {
                        ArrayList<Project> projects = rmiConnection.actualProjects();
                        objOut.writeObject(projects);
                        objOut.flush();
                        Project projectChoosen = (Project) objIn.readObject();
                        ProductType typeChoosen = (ProductType) objIn.readObject();
                        int money = in.readInt();
                        boolean pass = rmiConnection.donateMoney(log,projectChoosen,typeChoosen,money);
                        out.writeBoolean(pass);
                    }
                    if(choose == 3)
                    {
                        int choose1 = in.readInt();
                        ArrayList<Project> projects = null;
                        if(choose1 == 1)
                        {
                            projects = rmiConnection.oldProjects();
                        }
                        else if(choose1 == 2)
                        {
                            projects = rmiConnection.actualProjects();
                        }
                        objOut.writeObject(projects);
                        objOut.flush();
                    }
                    if(choose == 4)
                    {
                        int choose2 = in.readInt();
                        ArrayList<Project> projects = null;
                        if(choose2 == 2)
                        {
                            projects = rmiConnection.actualProjects();
                        }
                        objOut.writeObject(projects);
                        objOut.flush();
                    }
                    if (choose == 5){
                        objOut.writeObject(rmiConnection.getMyProjects(log));
                        objOut.flush();
                        Project projectChoosen = (Project) objIn.readObject();
                        Reward newR = (Reward) objIn.readObject();
                        boolean pass = rmiConnection.addReward(log, projectChoosen, newR );
                        out.writeBoolean(pass);
                    }
                    if(choose == 6){
                        objOut.writeObject(rmiConnection.getMyProjects(log));
                        objOut.flush();
                        Project projectChoosen = (Project) objIn.readObject();
                        Reward rewardChoosen = (Reward) objIn.readObject();
                        boolean pass = rmiConnection.removeReward(log, projectChoosen, rewardChoosen.getRewardID());
                        out.writeBoolean(pass);
                    }
                    if(choose == 7){
                        ArrayList<Project> projects = rmiConnection.actualProjects();
                        objOut.writeObject(projects);
                        objOut.flush();
                        Message messageChoosen = (Message) objIn.readObject();
                        boolean pass = rmiConnection.sendMessage(messageChoosen);
                        out.writeBoolean(pass);
                    }
                    if(choose == 8){
                        ArrayList<Project> projects = rmiConnection.getMyProjects(log);
                        for(int i=0;i<projects.size();i++)
                        {
                            if(rmiConnection.getProjectMessages(projects.get(i)).size()==0)
                            {
                                projects.remove(i);
                                i--;
                            }
                        }
                        objOut.writeObject(projects);
                        Project project = (Project)objIn.readObject();
                        objOut.writeObject(rmiConnection.getProjectMessages(project));
                        objOut.flush();
                        Message message =(Message) objIn.readObject();
                        Reply reply = (Reply) objIn.readObject();
                        boolean pass = rmiConnection.replyMessage(message, reply);
                        out.writeBoolean(pass);
                    }
                    if(choose == 9){
                        objOut.writeObject(rmiConnection.getMyProjects(log));
                        objOut.flush();
                        Project projectChoosen = (Project) objIn.readObject();
                        boolean pass = rmiConnection.cancelProject(log, projectChoosen);
                        out.writeBoolean(pass);
                    }
                }
            }


        }
        catch (EOFException e)
        {
            System.err.println("EOF:" + e);
        }
        catch (RemoteException e) {
            System.err.println("RemoteException RMI Down Attempt to reconnect");
            try
            {
                clientSocket.close();
            }
            catch (IOException e1) {
                System.err.println("Close Socket Exception");
            }
        }
        catch (IOException e)
        {
            System.err.println("IO Exception "+e);

        }
        catch (ClassNotFoundException e) {
            System.err.println("Class Not Found Exception:" + e);
        }
    }
}

class RMIConnection extends Thread
{
    RMI rmiConnection;
    String ip;

    RMIConnection(RMI rmiConnection, String ip)
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
                //System.setSecurityManager(new SecurityManager());
                int rmiport = 7697;
                String name = "rmi://"+ip+":"+rmiport+"/DB";
                System.setProperty("java.rmi.server.hostname", ip);
                rmiConnection = (RMI) Naming.lookup(name);
                System.out.println(rmiConnection.printTest());
            }
            catch (NotBoundException e)
            {
                System.err.println("RMI Not Bound Exception:" + e);
            }
            catch (RemoteException e)
            {
                System.err.println("RMI is drinking beers, wait for him");

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
                System.err.println("RMI Malformed URL Exception");
            }
        }
    }
}

class UDPThread extends Thread
{
    private int number;
    private DatagramSocket aSocket = null;
    private String path;

    public UDPThread(int number, String path)
    {
        this.number = number;
        this.path = path;
        this.start();
    }

    public void run()
    {
        if(number == 1)
        {
            try
            {
                aSocket = new DatagramSocket(6788);
                aSocket.setSoTimeout(8000);
                while(true)
                {
                    byte[] buffer = new byte[10];
                    InetAddress aHost = InetAddress.getByName(path);
                    int serverPort = 6789;
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length,aHost,serverPort);
                    aSocket.send(request);
                    buffer = new byte[10];
                    DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
                    aSocket.receive(reply);
                    aSocket.setSoTimeout(3000);
                }
            }
            catch (IOException e)
            {
                System.out.println("IO No Ping from other server...");
                new UDPThread(number,path);
            }
            finally
            {
                if(aSocket!=null)
                {
                    aSocket.close();
                }
            }
        }
        else if(number == 2)
        {
            int tries = 0;
            try
            {
                aSocket = new DatagramSocket(6789);
                aSocket.setSoTimeout(2000);
                while(tries<5)
                {
                    try
                    {
                        byte[] buffer = new byte[10];
                        InetAddress aHost = InetAddress.getByName(path);
                        int serverPort = 6788;
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length,aHost,serverPort);
                        aSocket.send(request);
                        buffer = new byte[10];
                        DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
                        aSocket.receive(reply);
                        aSocket.setSoTimeout(3000);
                    }
                    catch(SocketTimeoutException e)
                    {
                        System.err.println("Timeout No Ping from other Server\nAttempt number "+(tries+1));
                        tries++;
                    }
                }
                aSocket.close();
                System.err.println("Going to Primary Server...");
                String[] strings = {"1",path};
                new TCPServer().main(strings);
            }
            catch (SocketException e) {
                System.err.println("Socket Exception:" + e);
                new UDPThread(number,path);

            }
            catch(UnknownHostException e) {
                System.err.println("Unknown Host Exception:" + e);
            }
            catch(IOException e)
            {
                System.err.println("IO Exception "+e);
            }
            finally
            {
                if(aSocket!=null)
                {
                    aSocket.close();
                }
            }
        }
    }

}