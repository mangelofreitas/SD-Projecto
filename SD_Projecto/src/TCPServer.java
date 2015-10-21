import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Maria Filipa on 12-10-2015.
 */

public class TCPServer {
    private static RMI rmiConnection;


    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        try {
            int serverPort = 6000;
            int serverNumber;
            if(args.length==0)
            {
                System.out.print("Number of the server: ");
                serverNumber = sc.nextInt();
                sc.nextLine();
            }
            else
            {
                serverNumber = Integer.parseInt(args[0]);
            }
            if(serverNumber == 1)
            {
                int numero = 0;
                ArrayList<DataOutputStream> clientes = new ArrayList<DataOutputStream>();
                System.getProperties().put("java.security.policy", "politics.policy");
                System.setSecurityManager(new SecurityManager());
                int rmiport = 7697;
                String name = "DB";
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
                System.out.println("I'm the number 1");
                new UDPThread(serverNumber,path);
                System.out.println("A Escuta no Porto 6000");
                ServerSocket listenSocket = new ServerSocket(serverPort);
                System.out.println("LISTEN SOCKET=" + listenSocket);
                Registry regis = LocateRegistry.getRegistry(rmiport);
                rmiConnection = (RMI) regis.lookup(name);
                System.out.println("Connected to RMI");
                while (true)
                {
                    Socket clientSocket = listenSocket.accept();
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    clientes.add(new DataOutputStream(clientSocket.getOutputStream()));
                    new Connection(clientSocket, numero, rmiConnection);
                }
            }
            else if(serverNumber == 2)
            {
                System.out.print("Address of server 1: ");
                String path = sc.nextLine();
                System.out.println("I'm the number 2");
                new UDPThread(serverNumber,path);
            }

        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
        catch (NotBoundException e)
        {
            System.err.println("RMI connection:" + e);
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
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {

        //System.out.println("Thread "+thread_number+" started! ");

        try {


            int choose;
            User log = null;
            while(log==null) {
                 choose = in.readInt();
                if (choose == 1) {
                    log = (User) objIn.readObject();
                    log = rmiConnection.makeLogin(log);
                    objOut.writeObject(log);
                } else if (choose == 2) {
                    log = (User) objIn.readObject();
                    log = rmiConnection.makeRegist(log);
                    objOut.writeObject(log);
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
                        objOut.writeObject(rmiConnection.getMyProjects(log));
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


        } catch (EOFException e) {
            System.out.println("EOF:" + e);
        } catch (IOException e) {
            System.out.println("IO:" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            String text = "Ping 1";
            try
            {
                aSocket = new DatagramSocket(6788);
                aSocket.setSoTimeout(8000);
                while(true)
                {
                    byte[] buffer = text.getBytes();
                    InetAddress aHost = InetAddress.getByName(path);
                    int serverPort = 6789;
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length,aHost,serverPort);
                    aSocket.send(request);
                    buffer = new byte[1024];
                    DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
                    aSocket.receive(reply);
                    aSocket.setSoTimeout(1000);
                }
            }
            catch (IOException e)
            {
                System.out.println("IO:" + e);
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
            String text = "Ping 2";
            try
            {
                aSocket = new DatagramSocket(6789);
                aSocket.setSoTimeout(8000);
                while(true)
                {
                    byte[] buffer = text.getBytes();
                    InetAddress aHost = InetAddress.getByName(path);
                    int serverPort = 6788;
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length,aHost,serverPort);
                    aSocket.send(request);
                    buffer = new byte[1024];
                    DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
                    aSocket.receive(reply);
                    aSocket.setSoTimeout(1000);
                }
            }
            catch (SocketException e)
            {
                System.out.println("Socket Exception:" + e);
            }
            catch(UnknownHostException e)
            {
                System.out.println("Unknown Host Exception:" + e);
            }
            catch(IOException e)
            {
                aSocket.close();
                System.out.println("IO:" + e);
                String[] strings = {"1",path};
                new TCPServer().main(strings);
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