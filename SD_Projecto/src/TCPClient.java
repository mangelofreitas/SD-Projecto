import java.net.*;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient {

    private static DataInputStream in;
    private static DataOutputStream out;
    private static ObjectOutputStream objOut;
    private static ObjectInputStream objIn;
    private static Socket s;
    private static User actUser;

    public static void main(String args[]) {

        int serversocket=6000;

        try {
            s = new Socket(args[0], serversocket);

            System.out.println("SOCKET=" + s);

            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());
            objOut = new ObjectOutputStream(out);
            objIn = new ObjectInputStream(in);
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);

            loginSignup();
        }

        catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {//quer de exceção ou nao corre sempre
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:" + e.getMessage());
                }
        }
    }

    public static void loginSignup(){
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

    public static void login(){

        String mail, password;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nMail: ");
        mail = sc.nextLine();

        System.out.println("\nPassword: ");
        password = sc.nextLine();

        User log = new User(mail, password);

        try {
            out.writeInt(1);
            objOut.writeObject(log);
            objOut.flush();

            log=(User)objIn.readObject();

            if (log == null){
                System.out.println("\n WRONG LOGIN!\n Exiting now...\n");
                login();
            }
            else{
                System.out.println("\nLOGIN ACCEPTED!\n");
            }

        } catch (IOException e) {
            System.err.println("IO exception:" + e);
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found Exception:" + e);
        }

        menu();
    }

    public static void signup(){
        String name;
        String email;
        String password;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nName: ");
        name = sc.nextLine();

        System.out.println("\nEmail: ");
        email = sc.nextLine();

        System.out.println("\nPassword: ");
        password = sc.nextLine();

        User sig = new User(name, email, password);

        try {
            out.writeInt(2);
            objOut.writeObject(sig);
            objOut.flush();

            sig=(User)objIn.readObject();

            if (sig == null){
                System.out.println("\n WRONG SIGN UP!\n Exiting now...\n");
                signup();
            }
            else{
                System.out.println("\nSIGN UP ACCEPTED!\n");
            }

        } catch (IOException e) {
            System.err.println("IO exception:" + e);
        } catch (ClassNotFoundException e) {
            System.err.println("Class Not Found Exception:" + e);
        }

        login();
    }

    public static void menu(){
        Scanner sc = new Scanner(System.in);
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
            System.out.println("10 - End of project;");
            System.out.println("11 - Exit FUNDSTARTER.");
            System.out.println("\nOption: ");
            String option = sc.nextLine();

            switch(option){
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

                    System.out.println("Types of object (ex: azul, vermelho...)");
                    while(true)
                    {
                        ProductType productType = new ProductType();
                        System.out.println("Type:");
                        productType.setType(sc.nextLine());
                        project.getProductTypes().add(productType);
                        System.out.println("Another? (Y/N)");
                        if(sc.nextLine().equals("N"))
                        {
                            break;
                        }
                    }
                    System.out.println("Rewards of project");
                    while (true)
                    {
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
                        if(sc.nextLine().equals("N"))
                        {
                            break;
                        }
                    }

                    try{
                        out.writeInt(1);
                        objOut.writeObject(project);
                        objOut.flush();



                        if (in.readBoolean() == false){
                            System.out.println("\n ERROR!\n Exiting now...\n");
                            menu();
                        }
                        else{
                            System.out.println("\nPROJECT ACCEPTED!\n");
                        }
                    }
                    catch (IOException e)
                    {
                        System.err.println("IO exception:" + e);
                    }

                    break;

                case "2":
                    System.out.println("\n\nDONATE PROJECT TO THE PROJECT");
                    try
                    {
                        out.writeInt(2);

                        ArrayList<Project> projects = (ArrayList<Project>) objIn.readObject();// = rmiConnection.actualProjects();
                        for(int i=0;i<projects.size();i++)
                        {
                            System.out.println("\n"+projects.get(i)+"\n\n");
                        }
                        System.out.println("Select project to donate money [from 0 to " + (projects.size() - 1) + "]:");
                        int choose = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Vote on the product type [from 0 to " + (projects.get(choose).getProductTypes().size() - 1) + "]:");
                        for(int n=0;n<projects.get(choose).getProductTypes().size();n++)
                        {
                            System.out.println(n+" -> "+projects.get(choose).getProductTypes().get(n));
                        }
                        int vote = sc.nextInt();
                        sc.nextLine();
                        System.out.println("Quantity of money you donate:");
                        int money = sc.nextInt();
                        sc.nextLine();
                        objOut.writeObject(projects.get(choose));
                        objOut.writeObject(projects.get(choose).getProductTypes().get(vote));
                        out.writeInt(money);
                        if (!in.readBoolean()){
                            System.out.println("\n ERROR!\n Exiting now...\n");
                            menu();
                        }
                        else{
                            System.out.println("\nDONATE ACCEPTED!\n");
                        }
                    }
                    catch (IOException e)
                    {
                        System.err.println("IO exception:" + e);
                    }
                    catch (ClassNotFoundException e)
                    {
                        System.err.println("Class Not Found Exception:" + e);
                    }


                    break;

                case "3":
                    System.out.println("\n\nLIST");
                    System.out.println("\n1 - List Old Projects;");
                    System.out.println("2 - List Current Projects.");
                    System.out.println("\nOption: ");
                    try
                    {
                        out.writeInt(3);
                        String option1 = sc.nextLine();
                        ArrayList<Project> projects;
                        switch (option1){
                            case "1":
                                out.writeInt(1);
                                break;
                            case "2":
                                out.writeInt(2);
                                break;
                            default:
                                System.out.println("\nWRONG OPTION! Try again.");
                                menu();
                                break;
                        }
                        projects = (ArrayList<Project>)objIn.readObject();
                        for(int i=0;i<projects.size();i++)
                        {
                            System.out.println("\n"+projects.get(i)+"\n\n");
                        }
                    }
                    catch (IOException e)
                    {
                        System.err.println("IO exception:" + e);
                    }
                    catch (ClassNotFoundException e)
                    {
                        System.err.println("Class Not Found Exception:" + e);
                    }
                    break;

                case "4":
                    System.out.println("\n\nCONSULT");
                    System.out.println("\n1 - Consult money;");
                    System.out.println("2 - Consult project details;");
                    System.out.println("3 - Consult rewards.");
                    System.out.println("\nOption: ");
                    String option2 = sc.nextLine();

                    switch(option2){
                        case "1":
                            break;

                        case "2":
                            break;

                        case "3":
                            break;

                        default:
                            System.out.println("\nWRONG OPTION! Try again.");
                            menu();
                            break;
                    }
                    break;

                case "5":
                    System.out.println("\n\nADD REWARDS");
                    System.out.println("\nName: ");
                    String nameR = sc.nextLine();

                    System.out.println("\nDescription: ");
                    String desc = sc.nextLine();

                    System.out.println("\nValue of reward: ");
                    int valueR = sc.nextInt();

                    break;

                case "6":
                    System.out.println("\n\nREMOVE REWARDS");
                    //listar todos os projetos
                    int valueRe = sc.nextInt();

                    break;

                case "7":
                    System.out.println("\n\nSEND MESSAGE TO THE PROJECT");
                    System.out.println("\nProject name: ");
                    String namePro= sc.nextLine();

                    System.out.println("\nMessage: ");
                    String message= sc.nextLine();

                    break;

                case "8":
                    //listar sms dos apoiantes
                    System.out.println("\n\nANSWER SUPPORTERS MESSAGES");
                    System.out.println("\nOption");
                    int supporter = sc.nextInt();
                    System.out.println("\nAnswer: ");

                    break;

                case "9":
                    //listar para cancelar projetos
                    System.out.println("\n\nCANCEL PROJECT");
                    System.out.println("\nOption");
                    int projectC = sc.nextInt();

                    break;

                case "10":
                    System.out.println("\n\nEND OF PROJECT");
                    break;

                case "11":
                    System.out.println("EXIT FUNDSTARTER");
                    exit();
                    break;

                default:
                    System.out.println("\nWRONG OPTION! Try again.");
                    menu();
                    break;
            }
        }

    }

    public static void exit(){
        System.out.println("\nBye bye. Thanks for using FUNDSTARTER!\n");

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