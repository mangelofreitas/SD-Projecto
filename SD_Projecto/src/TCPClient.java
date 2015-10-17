import java.net.*;
import java.io.*;
import java.sql.Date;
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        login();
    }

    public static void menu(){
        Scanner sc = new Scanner(System.in);
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

                System.out.println("\nDeadline: ");
                project.setDateLimit(new Date(2015));

                System.out.println("\nRequested value: ");
                project.setRequestedValue(sc.nextInt());

                /*try{
                    out.writeInt(1);
                    objOut.writeObject(project);
                    objOut.flush();

                    project=(Project)objIn.readObject();

                    if (project == null){
                        System.out.println("\n ERROR!\n Exiting now...\n");
                        menu();
                    }
                    else{
                        System.out.println("\nPROJECT ACCEPTED!\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }*/

                break;

            case "2":
                System.out.println("\n\nDONATE PROJECT TO THE PROJECT");
                System.out.println("Money Given: ");
                int money = sc.nextInt();

                break;

            case "3":
                System.out.println("\n\nLIST");
                System.out.println("\n1 - List Old Projects;");
                System.out.println("2 - List Current Projects.");
                System.out.println("\nOption: ");
                String option1 = sc.nextLine();

                switch (option1){
                    case "1":
                        break;

                    case "2":
                        break;

                    default:
                        System.out.println("\nWRONG OPTION! Try again.");
                        menu();
                        break;
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