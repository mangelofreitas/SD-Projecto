import java.net.*;
import java.io.*;
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

            String texto = "teste";
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);

            out.writeUTF(texto);

            System.out.println(in.readUTF());

            loginSignup();
        }

        catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {//quer de exce��o ou nao corre sempre
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

    public static void login(){ //throws IOException{

        String mail, password;
        Scanner sc = new Scanner(System.in);

        System.out.println("\nMail: ");
        mail = sc.nextLine();

        System.out.println("\nPassword: ");
        password = sc.nextLine();

        User log = new User(mail, password);

        User confirmation = null;

        /*try {
            out.writeInt(1);
            objOut.writeObject(log);
            objOut.flush();
            confirmation = (User) objIn.readObject();
        } catch (IOException ex) {
            reconnect();
            out.writeInt(1);
            objOut.writeObject(log);
            objOut.flush();
            try {
                confirmation = (User) objIn.readObject();
            } catch (ClassNotFoundException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            if (confirmation.getId() > 0) {
                System.out.println("\n LOGIN ACCEPTED!\n Welcome, " + confirmation.getUsername() + ", ID: " + confirmation.getId() + "\n");
                activeUser.setUsername(confirmation.getUsername());
                activeUser.setPassword(confirmation.getPassword());
                activeUser.setId(confirmation.getId());
                printMenu();
            } else {
                System.out.println("\n WRONG LOGIN!\n Exiting now...");
                loginSignup()
            }
        } catch (Exception e) {
            System.out.println("ERRO: recep��o de confirma��o de login no cliente");
            e.printStackTrace();
        }*/
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
                System.out.println("\n\nCREATE A PROJECT");
                System.out.println("\nName: ");
                String nameP = sc.nextLine();

                System.out.println("\nDescription: ");
                String descrip = sc.nextLine();

                System.out.println("\nDeadline: ");
                String deadline = sc.nextLine();

                System.out.println("\nRequested value: ");
                String reqValue = sc.nextLine();

                System.out.println("\nCurrent amount: ");
                String currentA = sc.nextLine();

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