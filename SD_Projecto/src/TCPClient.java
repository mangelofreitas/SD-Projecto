import java.net.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by miguel and maria
 */

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
    //Inicializacao de streams para comunicar com o server
    private static DataInputStream in;
    private static DataOutputStream out;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static Socket s;
    private static Addresses path;

    public static void createSocket()
    {
        boolean notIO = false;
        while(!notIO)
        {
            try {
                s = new Socket(path.getAddress(), 6000); //porto = 6000
                System.out.println("SOCKET=" + s);

                //criar de streams para comunicar com o server
                in = new DataInputStream(s.getInputStream());
                out = new DataOutputStream(s.getOutputStream());
                objOut = new ObjectOutputStream(out);
                objIn = new ObjectInputStream(in);
                notIO = true;
            }

            catch (UnknownHostException e) {
                System.out.println("Sock:" + e.getMessage());
            } catch (EOFException e) {
                System.out.println("EOF:" + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO:" + e.getMessage());
            }
        }

    }

    public static void main(String args[])
    {
        //Pedir os ips para conexao
        Scanner sc = new Scanner(System.in);
        String address1, address2;
        System.out.print("Address of server 1: ");
        address1 = sc.nextLine();
        System.out.print("Address of server 2: ");
        address2 = sc.nextLine();
        path = new Addresses(address1,address2);
        createSocket();
        welcome();
    }

    public static void welcome()
    {
        while(true)
        {
            System.out.print("\n\nWELCOME TO FUNDSTARTER\n");
            System.out.println("\n1 - Log in;");
            System.out.println("2 - Sign up;");
            System.out.println("3 - List;");
            System.out.println("4 - Exit.");
            Scanner sc = new Scanner(System.in);
            System.out.println("\nOption: ");
            String option = sc.nextLine();
            boolean notIO = false;

            switch(option)
            {
                case "1":
                    login();
                    break;

                case "2":
                    signup();
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
                        } catch (ClassNotFoundException e) {
                            System.err.println("Class Not Found Exception:" + e);                        }
                    }
                    notIO = false;
                    break;

                case "4":
                    exit();
                    break;

                default:
                    System.out.println("\nWRONG OPTION! Try again.");
                    welcome();
                    break;
            }
        }

    }

    public static void login()
    {
        boolean notIO = false;
        String mail, password;
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\n LOG IN");

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

                if (log == null){
                    System.out.println("\n WRONG LOGIN!\n Exiting now...\n");
                    login();
                }
                else
                {
                    ArrayList<Project> projects = (ArrayList<Project>)objIn.readObject();
                    String text;
                    System.out.println("\nLOGIN ACCEPTED!\n");
                    if(projects!=null)
                    {
                        for (int i = 0;i<projects.size();i++)
                        {
                            System.out.println("\n" + projects.get(i) + "\n(Required) Project ended with success.\nInsert an extra type of product because you have 5 times more money than the required.\n");
                            text = sc.nextLine();
                            objOut.writeObject(projects.get(i));
                            objOut.writeObject(text);
                        }
                    }

                }
                notIO = true;
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
        Scanner sc = new Scanner(System.in);
        boolean notIO = false;
        while(!notIO)
        {
            try {
                out.writeInt(1);
                objOut.writeObject(log);
                objOut.flush();
                log = (User)objIn.readObject();
                ArrayList<Project> projects = (ArrayList<Project>)objIn.readObject();
                if(projects!=null)
                {
                    String text;
                    for (int i = 0;i<projects.size();i++)
                    {
                        System.out.println("\n" + projects.get(i) + "\n(Required) Project ended with success.\nInsert an extra type of product because you have 5 times more money than the required.\n");
                        text = sc.nextLine();
                        objOut.writeObject(projects.get(i));
                        objOut.writeObject(text);
                    }
                }
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

        System.out.println("SIGN UP");

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

                    while(true){
                        try{
                            System.out.println("\nDeadline: ");
                            System.out.print("Ano:");
                            String anoS = sc.nextLine();
                            System.out.print("Mes:");
                            String mesS = sc.nextLine();
                            System.out.print("Dia:");
                            String diaS = sc.nextLine();
                            project.setDateLimit(new Date(Integer.parseInt(anoS) - 1900, Integer.parseInt(mesS) - 1, Integer.parseInt(diaS)));
                            sc.nextLine();

                            System.out.println("\nRequested value: ");
                            String valueS = sc.nextLine();
                            project.setRequestedValue(Integer.parseInt(valueS));
                            sc.nextLine();
                            break;
                        }
                        catch(NumberFormatException e)
                        {
                            System.out.println("It's not a number, try again.");
                        }

                    }

                    System.out.println("\nTypes of object (ex: azul, vermelho...)");
                    String another="";
                    Boolean pass = false;
                    while (true)
                    {
                        ProductType productType = new ProductType();
                        System.out.println("Type:");
                        productType.setType(sc.nextLine());
                        project.getProductTypes().add(productType);

                        while(pass==false)
                        {
                            System.out.println("Another? (Y/N)");
                            another = sc.nextLine();
                            if (another.equals("N") || another.equals("n")|| another.equals("Y") || another.equals("y"))
                            {
                                pass=true;
                            }
                            else
                            {
                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                pass=false;
                            }
                        }
                        pass=false;
                        if (another.equals("N") || another.equals("n")) {
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

                        int valueInt;
                        while(true)
                        {
                            try
                            {
                                System.out.println("\nValue of reward: ");
                                String value = sc.nextLine();
                                valueInt = Integer.parseInt(value);
                                break;
                            }
                            catch(NumberFormatException e)
                            {
                                System.out.println("It's not a number, try again.");
                            }

                        }
                        reward.setValueOfReward(valueInt);
                        sc.nextLine();

                        project.getRewards().add(reward);
                        while(pass==false)
                        {
                            System.out.println("Another? (Y/N)");
                            another = sc.nextLine();
                            if (another.equals("N") || another.equals("n")|| another.equals("Y") || another.equals("y"))
                            {
                                pass=true;
                            }
                            else
                            {
                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                pass=false;
                            }
                        }
                        pass=false;
                        if (another.equals("N") || another.equals("n")) {
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
                    int vote=-1,money=-1;
                    int choose= -1;

                    while(!notIO)
                    {
                        try {
                            out.writeInt(2);
                            ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();
                            if(projects.size()>0)
                            {
                                if(choose==-1||vote==-1||money==-1)
                                {
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }
                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try{
                                            System.out.println("\nSelect project to donate money [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if(choose > projects.size()-1 || choose < 0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;

                                    System.out.println("\n\n");
                                    for (int n = 0; n < projects.get(choose).getProductTypes().size(); n++) {
                                        System.out.println(n + " -> " + projects.get(choose).getProductTypes().get(n));
                                    }

                                    while(pass1==false){
                                        try{
                                            System.out.println("\nVote on the product type [from 0 to " + (projects.get(choose).getProductTypes().size() - 1) + "]:");
                                            String voteS = sc.nextLine();
                                            vote = Integer.parseInt(voteS);

                                            if(vote > (projects.get(choose).getProductTypes().size() - 1) || vote <0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;

                                    while (pass1 == false) {
                                        try{
                                            System.out.println("\nChoose the reward according to the money you want to donate [from 0 to " + (projects.get(choose).getRewards().size() - 1) + "]:");
                                            String moneyS = sc.nextLine();
                                            money = Integer.parseInt(moneyS);

                                            if(money > (projects.get(choose).getRewards().size() - 1) || money <0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;

                                }
                                objOut.writeObject(projects.get(choose));
                                objOut.writeObject(projects.get(choose).getProductTypes().get(vote));
                                out.writeInt(projects.get(choose).getRewards().get(money).getValueOfReward());
                                objOut.flush();
                                if (!in.readBoolean()) {
                                    if(money>log.getMoney())
                                    {
                                        System.out.println("\nInsufficient Money...\n");
                                    }
                                    System.out.println("\n ERROR!\n Exiting now...\n");
                                } else {
                                    System.out.println("\nDONATE ACCEPTED!\n");
                                }
                                log = (User)objIn.readObject();
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
                    System.out.println("2 - Consult my projects messages;");
                    System.out.println("3 - Consult rewards.");
                    System.out.println("4 - Consult my send messages and replies to them.");
                    System.out.println("\nOption: ");
                    String option2 = sc.nextLine();
                    choose = -1;
                    while(!notIO)
                    {
                        try {
                            ArrayList<Project> projects;
                            switch (option2) {
                                case "1":
                                    System.out.println("\nMoney: " + log.getMoney());
                                    break;
                                case "2":
                                    out.writeInt(4);
                                    out.writeInt(2);
                                    projects = (ArrayList<Project>) objIn.readObject();
                                    if(projects.size()!=0)
                                    {
                                        if(choose==-1)
                                        {

                                            for (int i = 0; i < projects.size(); i++) {
                                                System.out.println("\n" + projects.get(i) + "\n");
                                            }

                                            Boolean pass1 = false;
                                            while (pass1 == false)
                                            {
                                                try{
                                                    System.out.println("\nSelect project to consult messages [from 0 to " + (projects.size() - 1) + "]:");
                                                    String chooseS = sc.nextLine();
                                                    choose = Integer.parseInt(chooseS);
                                                    pass1=true;
                                                }
                                                catch(NumberFormatException e)
                                                {
                                                    System.out.println("It's not a number, try again.");
                                                }
                                            }
                                            pass1=false;
                                            objOut.writeObject(projects.get(choose));
                                            ArrayList<Message> messages = (ArrayList<Message>) objIn.readObject();
                                            for(int i=0;i<messages.size();i++)
                                            {
                                                System.out.println("\n" + messages.get(i) + "\n");
                                            }
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("YOU HAVE NO PROJECTS!");
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
                                case "4":
                                    out.writeInt(4);
                                    out.writeInt(4);
                                    ArrayList<Message> messages = (ArrayList<Message>)objIn.readObject();
                                    for(int i=0;i<messages.size();i++)
                                    {
                                        System.out.println("\n" + messages.get(i) + "\n");
                                    }
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
                            if(projects.size() != 0)
                            {
                                if(reward==null || choose==-1)
                                {
                                    reward = new Reward();
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }

                                    Boolean pass1 = false;
                                    while(pass1==false){
                                        try {
                                            System.out.println("\nSelect project to add reward [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if (choose > projects.size() - 1 || choose < 0) {
                                                pass1 = false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            } else {
                                                pass1 = true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;


                                    System.out.println("\nName: ");
                                    reward.setName(sc.nextLine());

                                    System.out.println("\nDescription: ");
                                    reward.setDescription(sc.nextLine());
                                    int valueInt;
                                    while(true)
                                    {
                                        try
                                        {
                                            System.out.println("\nValue of reward: ");
                                            String value = sc.nextLine();
                                            valueInt = Integer.parseInt(value);
                                            break;
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }

                                    }
                                    reward.setValueOfReward(valueInt);
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
                            }
                            else {
                                System.out.println("\nYOU DON'T HAVE PROJECTS TO ADD REWARDS!\n");
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
                            if(projects.size() != 0)
                            {
                                if(rew==-1||choose==-1)
                                {
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }

                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try{
                                            System.out.println("\nSelect project to remove reward [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if(choose > projects.size()-1 || choose < 0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;


                                    System.out.println("\n\n");
                                    for(int n=0;n<projects.get(choose).getRewards().size();n++)
                                    {
                                        System.out.println(n+" -> "+projects.get(choose).getRewards().get(n));
                                    }
                                    if(projects.get(choose).getRewards().size()!=0)
                                    {
                                        while(pass1==false){
                                            try{
                                                System.out.println("\nSelect reward to remove [from 0 to " + (projects.get(choose).getRewards().size() - 1) + "]:");
                                                String rewS = sc.nextLine();
                                                rew = Integer.parseInt(rewS);

                                                if(rew > projects.get(choose).getRewards().size() - 1 || rew < 0){
                                                    pass1=false;
                                                    System.out.println("WRONG OPTION... \nPlease insert another option.");
                                                }
                                                else{
                                                    pass1=true;
                                                }
                                            }
                                            catch(NumberFormatException e)
                                            {
                                                System.out.println("It's not a number, try again.");
                                            }
                                        }
                                        pass1=false;
                                    }


                                }
                                if(projects.get(choose).getRewards().size()!=0)
                                {
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
                                }
                                else
                                {
                                    System.out.println("\nYOU DON'T REWARDS ON THE PROJECT TO REMOVE!\n");
                                }
                            }
                            else
                            {
                                System.out.println("\nYOU DON'T HAVE PROJECTS TO REMOVE REWARDS!\n");
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
                            if(projects.size()!=0)
                            {
                                if(sms == null || choose == -1)
                                {
                                    sms = new Message();
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }

                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try {
                                            System.out.println("\nSelect project to send message [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if (choose > projects.size() - 1 || choose < 0) {
                                                pass1 = false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            } else {
                                                pass1 = true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;
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
                            }
                            else
                            {
                                System.out.println("\nDOESN'T EXIST PROJECTS TO SEND MESSAGE!\n");
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
                            if(projects.size()!=0)
                            {
                                if(choose == -1)
                                {
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }

                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try{
                                            System.out.println("\nSelect project to answer supporters messages [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if(choose > projects.size()-1 || choose < 0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;

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

                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try{
                                            System.out.println("\nSelect message to answer [from 0 to " + (messages.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose1 = Integer.parseInt(chooseS);

                                            if(choose1 > messages.size()-1 || choose1 < 0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;


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
                            }
                            else
                            {
                                System.out.println("\nYOU DON'T HAVE PROJECTS TO ANSWER MESSAGES!\n");
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
                            if(projects.size() != 0)
                            {
                                if(choose == -1)
                                {
                                    for (int i = 0; i < projects.size(); i++) {
                                        System.out.println("\n" + projects.get(i) + "\n\n");
                                    }

                                    Boolean pass1=false;
                                    while(pass1==false){
                                        try{
                                            System.out.println("\nSelect project to cancel [from 0 to " + (projects.size() - 1) + "]:");
                                            String chooseS = sc.nextLine();
                                            choose = Integer.parseInt(chooseS);

                                            if(choose > projects.size()-1 || choose < 0){
                                                pass1=false;
                                                System.out.println("WRONG OPTION... \nPlease insert another option.");
                                            }
                                            else{
                                                pass1=true;
                                            }
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            System.out.println("It's not a number, try again.");
                                        }
                                    }
                                    pass1=false;
                                }
                                objOut.writeObject(projects.get(choose));
                                objOut.flush();

                                if (!in.readBoolean()){
                                    System.out.println("\n ERROR!\n Exiting now...\n");
                                }
                                else{
                                    System.out.println("\nPROJECT CANCELED!\n");
                                }
                            }
                            else
                            {
                                System.out.println("\nYOU HAVE NO PROJECTS!\n");
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

        try {
            in.close();
            out.close();
            objIn.close();
            objOut.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

}