import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Addresses
{
    private String addressPrimary;
    private String addressSecundary;
    private int number;

    public Addresses(String addressPrimary,String addressSecundary)
    {
        this.addressPrimary = addressPrimary;
        this.addressSecundary = addressSecundary;
        this.number = 1;
    }

    public String getAddress()
    {
        if(number == 1)
        {
            number = 2;
            return addressPrimary;
        }
        else
        {
            number = 1;
            return addressSecundary;
        }
    }
}

public class TCPClient {

    private static DataInputStream in;
    private static DataOutputStream out;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static Socket s;
    private static Addresses path;

    public static void createSocket()
    {
        try {
            s = new Socket(path.getAddress(), 6000);
            System.out.println("SOCKET=" + s);
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
        }

        catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        }
    }

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        String address1, address2;
        System.out.print("Address of server 1: ");
        address1 = sc.nextLine();
        System.out.print("Address of server 2: ");
        address2 = sc.nextLine();
        path = new Addresses(address1,address2);
        createSocket();
        loginSignup();
    }

    public static void loginSignup()
    {
        System.out.print("\n\nWELCOME TO FUNDSTARTER\n");
        System.out.println("\n1 - Log in;");
        System.out.println("2 - Sign up;");
        System.out.println("3 - Exit.");
        Scanner sc = new Scanner(System.in);
        System.out.println("\nOption: ");
        String option = sc.nextLine();

        switch(option){
            case "1":
                login();
                break;

            case "2":
                signup();
                break;

            case "3":
                exit();
                break;

            default:
                System.out.println("\nWRONG OPTION! Try again.");
                loginSignup();
                break;
        }
    }

    public static void login()
    {
        boolean notIO = false;
        String mail, password;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nMail: ");
        mail = sc.nextLine();

        System.out.println("\nPassword: ");
        password = sc.nextLine();

        User log = new User(mail, password);
        while(!notIO)
        {
            try {
                out.writeInt(1);
                objOut.writeObject(log);
                objOut.flush();

                log=(User)objIn.readObject();
                notIO = true;
                if (log == null){
                    System.out.println("\n WRONG LOGIN!\n Exiting now...\n");
                    login();
                }
                else{
                    System.out.println("\nLOGIN ACCEPTED!\n");
                }

            } catch (IOException e) {
                System.err.println("IO exception:" + e);
                createSocket();

            } catch (ClassNotFoundException e) {
                System.err.println("Class Not Found Exception:" + e);
            }
        }
        menu(log);
    }

    public static void loginAfterFail(User log)
    {
        boolean notIO = false;
        while(!notIO)
        {
            try {
                out.writeInt(1);
                objOut.writeObject(log);
                objOut.flush();
                objIn.readObject();

                notIO = true;
            } catch (IOException e) {
                System.err.println("IO exception:" + e);
                createSocket();

            } catch (ClassNotFoundException e) {
                System.err.println("Class Not Found Exception:" + e);
            }
        }
    }

    public static void signup()
    {
        String name,email,password;
        boolean notIO = false;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nName: ");
        name = sc.nextLine();

        System.out.println("\nEmail: ");
        email = sc.nextLine();

        System.out.println("\nPassword: ");
        password = sc.nextLine();



        User sig = new User(name, email, password);

        while (!notIO)
        {
            try {
                out.writeInt(2);
                objOut.writeObject(sig);
                objOut.flush();

                sig=(User)objIn.readObject();
                notIO = true;
                if (sig == null){
                    System.out.println("\n WRONG SIGN UP!\n Exiting now...\n");
                    signup();
                }
                else if (sig.getUsernameID()==-1)
                {
                    System.out.println("\n USERNAME OR MAIL ALREADY TAKEN CHOOSE ANOTHER\n Exiting now...");
                    signup();
                }
                else
                {
                    System.out.println("\nSIGN UP ACCEPTED!\n");
                }
            } catch (IOException e) {
                System.err.println("IO exception:" + e);
                createSocket();
            } catch (ClassNotFoundException e) {
                System.err.println("Class Not Found Exception:" + e);
            }
        }
        login();
    }



    public static void menu(User log){
        Scanner sc = new Scanner(System.in);
        boolean notIO = false;
        while(true)
        {
            System.out.println("\nChoose an option:\n");
            System.out.println("1  - Create a project;");
            System.out.println("2  - Donate money to the project;");
            System.out.println("3  - List;");
            System.out.println("4  - Consult;");
            System.out.println("5  - Add rewards;");
            System.out.println("6  - Remove rewards;");
            System.out.println("7  - Send message to the project;");
            System.out.println("8  - Answer supporters messages;");
            System.out.println("9  - Cancel project;");
            System.out.println("10 - Exit FUNDSTARTER.");
            System.out.println("\nOption: ");
            String option = sc.nextLine();

            switch(option) {
                case "1":
                    Project project = new Project();
                    System.out.println("\n\nCREATE A PROJECT");
                    System.out.println("\nName: ");
                    project.setProjectName(sc.nextLine());

                    System.out.println("\nDescription: ");
                    project.setDescription(sc.nextLine());

                    System.out.println("\nDeadline(Year, Moth, Day): ");
                    project.setDateLimit(new Date(sc.nextInt() - 1900, sc.nextInt() - 1, sc.nextInt()));
                    sc.nextLine();

                    System.out.println("\nRequested value: ");
                    project.setRequestedValue(sc.nextInt());
                    sc.nextLine();

                    System.out.println("\nTypes of object (ex: azul, vermelho...)");
                    while (true) {
                        ProductType productType = new ProductType();
                        System.out.println("Type:");
                        productType.setType(sc.nextLine());
                        project.getProductTypes().add(productType);
                        System.out.println("Another? (Y/N)");
                        if (sc.nextLine().equals("N")) {
                            break;
                        }
                    }
                    System.out.println("\nRewards of project");
                    while (true) {
                        Reward reward = new Reward();
                        System.out.println("Name:");
                        reward.setName(sc.nextLine());
                        System.out.println("Description:");
                        reward.setDescription(sc.nextLine());
                        System.out.println("Value of Reward:");
                        reward.setValueOfReward(sc.nextInt());
                        sc.nextLine();
                        project.getRewards().add(reward);
                        System.out.println("Another? (Y/N)");
                        if (sc.nextLine().equals("N")) {
                            break;
                        }
                    }
                    while(!notIO)
                    {
                        try {
                            out.writeInt(1);
                            objOut.writeObject(project);
                            objOut.flush();
                            notIO = true;

                            if (in.readBoolean() == false) {
                                System.out.println("\n ERROR!\n Exiting now...\n");
                                menu(log);
                            }
                            else
                            {
                                System.out.println("\nPROJECT ACCEPTED!\n");
                            }
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        }
                    }
                    notIO = false;
                    break;

                case "2":
                    System.out.println("\n\nDONATE MONEY TO THE PROJECT");
                    int choose=-1,vote=-1,money=-1;
                    while(!notIO)
                    {
                        try {
                            out.writeInt(2);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            if(choose==-1||vote==-1||money==-1)
                            {
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to donate money [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();
                                System.out.println("\nVote on the product type [from 0 to " + (projects.get(choose).getProductTypes().size() - 1) + "]:");
                                for (int n = 0; n < projects.get(choose).getProductTypes().size(); n++) {
                                    System.out.println(n + " -> " + projects.get(choose).getProductTypes().get(n));
                                }
                                vote = sc.nextInt();
                                sc.nextLine();
                                System.out.println("\nQuantity of money you donate:");
                                money = sc.nextInt();
                                sc.nextLine();
                            }
                            objOut.writeObject(projects.get(choose));
                            objOut.writeObject(projects.get(choose).getProductTypes().get(vote));
                            out.writeInt(money);
                            objOut.flush();
                            if (!in.readBoolean()) {
                                System.out.println("\n ERROR!\n Exiting now...\n");
                            } else {
                                System.out.println("\nDONATE ACCEPTED!\n");
                            }
                            notIO = true;
                        } catch (IOException e) {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        } catch (ClassNotFoundException e) {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;

                    break;

                case "3":
                    System.out.println("\n\nLIST");
                    System.out.println("\n1 - List Old Projects;");
                    System.out.println("2 - List Current Projects.");
                    System.out.println("\nOption: ");
                    String option1 = sc.nextLine();
                    while(!notIO)
                    {
                        try {
                            out.writeInt(3);
                            switch (option1) {
                                case "1":
                                    out.writeInt(1);
                                    break;
                                case "2":
                                    out.writeInt(2);
                                    break;
                                default:
                                    System.out.println("\nWRONG OPTION! Try again.");
                                    break;
                            }
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            notIO = true;
                            for (int i = 0; i < projects.size(); i++) {
                                System.out.println("\n" + projects.get(i) + "\n\n");
                            }
                        } catch (IOException e) {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        } catch (ClassNotFoundException e) {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;
                    break;

                case "4":
                    System.out.println("\n\nCONSULT");
                    System.out.println("\n1 - Consult money;");
                    System.out.println("2 - Consult project details;");
                    System.out.println("3 - Consult rewards.");
                    System.out.println("\nOption: ");
                    String option2 = sc.nextLine();
                    choose = -1;
                    while(!notIO)
                    {
                        try {
                            ArrayList<Project> projects1;
                            switch (option2) {
                                case "1":
                                    System.out.println("\nMoney: " + log.getMoney());
                                    break;
                                case "2":
                                    out.writeInt(4);
                                    out.writeInt(2);
                                    projects1 = (ArrayList<Project>) objIn.readObject();
                                    if(choose==-1)
                                    {
                                        System.out.println("\nSelect project to consult details [from 0 to " + (projects1.size() - 1) + "]:");

                                        for (int i = 0; i < projects1.size(); i++) {
                                            System.out.println("\n" + projects1.get(i).getProjectName() + "\n");
                                        }
                                        choose = sc.nextInt();
                                        sc.nextLine();
                                        System.out.println("\n" + projects1.get(choose) + "\n");
                                    }
                                    break;
                                case "3":
                                    System.out.println("\n");
                                    for (int i=0;i<log.getRewards().size();i++)
                                    {
                                        System.out.println(log.getRewards().get(i));
                                    }
                                    System.out.println("\n");
                                    break;
                                default:
                                    System.out.println("\nWRONG OPTION! Try again.");
                                    break;
                            }
                            notIO = true;
                        } catch (IOException e) {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        } catch (ClassNotFoundException e) {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO=false;
                    break;
                case "5":
                    System.out.println("\n\nADD REWARDS");
                    Reward reward = null;
                    choose = -1;
                    while(!notIO)
                    {
                        try
                        {
                            out.writeInt(5);

                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();

                            if(reward==null || choose==-1)
                            {
                                reward = new Reward();
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to add reward [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();

                                System.out.println("\nName: ");
                                reward.setName(sc.nextLine());

                                System.out.println("\nDescription: ");
                                reward.setDescription(sc.nextLine());

                                System.out.println("\nValue of reward: ");
                                reward.setValueOfReward(sc.nextInt());
                                sc.nextLine();
                            }
                            objOut.writeObject(projects.get(choose));
                            objOut.writeObject(reward);
                            objOut.flush();

                            if (!in.readBoolean()) {
                                System.out.println("\n ERROR!\n Exiting now...\n");
                            } else {
                                System.out.println("\nREWARD ADDED!\n");
                            }
                            notIO = true;
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);

                        } catch (ClassNotFoundException e) {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;


                    break;

                case "6":
                    System.out.println("\n\nREMOVE REWARDS");
                    int rew = -1;
                    choose = -1;
                    while(!notIO)
                    {
                        try {
                            out.writeInt(6);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            if(rew==-1||choose==-1)
                            {
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to remove reward [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();

                                System.out.println("\nSelect reward to remove [from 0 to " + (projects.get(choose).getRewards().size() - 1) + "]:");
                                for(int n=0;n<projects.get(choose).getRewards().size();n++)
                                {
                                    System.out.println(n+" -> "+projects.get(choose).getRewards().get(n));
                                }
                                rew = sc.nextInt();
                                sc.nextLine();
                            }
                            objOut.writeObject(projects.get(choose));
                            objOut.writeObject(projects.get(choose).getRewards().get(rew));
                            objOut.flush();

                            if (!in.readBoolean()){
                                System.out.println("\n ERROR!\n Exiting now...\n");
                                menu(log);
                            }
                            else{
                                System.out.println("\nREWARD REMOVED!\n");
                            }
                            notIO = true;
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        }
                        catch (ClassNotFoundException e)
                        {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;
                    break;

                case "7":
                    System.out.println("\n\nSEND MESSAGE TO THE PROJECT");
                    Message sms = null;
                    choose = -1;
                    while(!notIO)
                    {
                        try{
                            out.writeInt(7);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            for(int i=0;i<projects.size();i++)
                            {
                                if(projects.get(i).getUser().getUsernameID()==log.getUsernameID())
                                {
                                    projects.remove(i);
                                    i--;
                                }
                            }
                            if(sms == null || choose == -1)
                            {
                                sms = new Message();
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to send message [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();
                                sms.setProject(projects.get(choose));

                                System.out.println("\nMessage: ");
                                sms.setMessage(sc.nextLine());
                                sms.setUser(log);
                            }
                            objOut.writeObject(sms);
                            objOut.flush();

                            if (!in.readBoolean()){
                                System.out.println("\n ERROR!\n Exiting now...\n");
                            }
                            else{
                                System.out.println("\nMESSAGE SENT!\n");
                            }
                            notIO = true;
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        }
                        catch (ClassNotFoundException e)
                        {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;
                    break;

                case "8":
                    System.out.println("\n\nANSWER SUPPORTERS MESSAGES");
                    Reply rep = null;
                    choose = -1;
                    int choose1 = -1;
                    while(!notIO)
                    {
                        try{
                            out.writeInt(8);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            if(choose == -1)
                            {
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to answer supporters messages [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();
                            }
                            objOut.writeObject(projects.get(choose));
                            objOut.flush();
                            ArrayList<Message> messages = (ArrayList<Message>) objIn.readObject();
                            if(rep == null || choose1 == -1)
                            {
                                rep = new Reply();
                                for (int j = 0; j < messages.size(); j++) {
                                    System.out.println("\n" + messages.get(j) + "\n\n");
                                }
                                System.out.println("\nSelect message to answer [from 0 to " + (messages.size() - 1) + "]:");
                                choose1 =sc.nextInt();
                                sc.nextLine();

                                System.out.println("\nAnswer: ");
                                rep.setMessage(sc.nextLine());
                                rep.setUser(log);
                            }
                            objOut.writeObject(messages.get(choose1));
                            objOut.writeObject(rep);
                            objOut.flush();

                            if (!in.readBoolean()){
                                System.out.println("\n ERROR!\n Exiting now...\n");
                            }
                            else{
                                System.out.println("\nMESSAGE SENT!\n");
                            }
                            notIO = true;
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        }
                        catch (ClassNotFoundException e)
                        {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;
                    break;

                case "9":
                    System.out.println("\n\nCANCEL PROJECT");
                    choose = -1;
                    while(!notIO)
                    {
                        try{
                            out.writeInt(9);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            if(choose == -1)
                            {
                                for (int i = 0; i < projects.size(); i++) {
                                    System.out.println("\n" + projects.get(i) + "\n\n");
                                }
                                System.out.println("\nSelect project to cancel [from 0 to " + (projects.size() - 1) + "]:");
                                choose = sc.nextInt();
                                sc.nextLine();
                            }
                            objOut.writeObject(projects.get(choose));
                            objOut.flush();

                            if (!in.readBoolean()){
                                System.out.println("\n ERROR!\n Exiting now...\n");
                            }
                            else{
                                System.out.println("\nPROJECT CANCELED!\n");
                            }
                            notIO = true;
                        }
                        catch (IOException e)
                        {
                            System.err.println("IO exception:" + e);
                            createSocket();
                            loginAfterFail(log);
                        }
                        catch (ClassNotFoundException e)
                        {
                            System.err.println("Class Not Found Exception:" + e);
                        }
                    }
                    notIO = false;
                    break;

                case "10":
                    System.out.println("\n\nEXIT FUNDSTARTER");
                    exit();
                    break;

                default:
                    System.out.println("\nWRONG OPTION! Try again.");
                    break;
            }
        }

    }

    public static void exit(){
        System.out.println("\nBye bye. Thanks for using FUNDSTARTER!\n");
        System.exit(0);
        /*TRATAR DEPOIS
        try{
            try{
                out.writeInt(0);
            }
            catch (IOException e){
                out.write(0);
            }
            in.close();
            out.close();
            objIn.close();
            objOut.close();
            s.close();
            System.exit(0);
        }
        catch(IOException ex){

        }*/
    }

    public static void pause(){
        Scanner sc = new Scanner(System.in);

        System.out.println("\nPress Enter to continue, please.");
        sc.nextLine();
    }
}