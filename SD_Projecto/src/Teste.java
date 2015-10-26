import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by miguel and maria
 */
public class Teste extends UnicastRemoteObject
{
    public static RMI rmiConnection;
    protected Teste() throws RemoteException
    {
        super();
    }

    public static void main(String args[])
    {
        System.getProperties().put("java.security.policy", "politics.policy");
        System.setSecurityManager(new SecurityManager());
        Scanner sc = new Scanner(System.in);
        int rmiport = 7697;
        String name = "DB";
        try
        {
            Registry regis = LocateRegistry.getRegistry(rmiport);
            rmiConnection = (RMI) regis.lookup(name);
            System.out.println("Connected to RMI");
        } catch (Exception e) {
            System.err.println("RMI connection:"+e);
        }

        try
        {
            User user = new User("bla@cenas.com","123");
            user = rmiConnection.makeLogin(user);
            if(user==null)
            {
                System.out.println("fail!");
            }
            else
            {
                System.out.println("Username:" + user.getUsername() + "\nWith ID:" + user.getUsernameID() + "\nCash:" + user.getMoney()+"\nYour Rewards:");

                System.out.println(rmiConnection.endProject(new Project(new User(3),7)));
                /*for (int i=0;i<user.getRewards().size();i++)
                {
                    System.out.println(user.getRewards().get(i));
                }

                ArrayList<Project> projects = rmiConnection.getMyProjects(user);
                for(int i=0;i<projects.size();i++)
                {
                    System.out.println("\n"+projects.get(i)+"\n\n");
                }
                System.out.println("Select project to end [from 0 to " + (projects.size() - 1) + "]:");
                int choose = sc.nextInt();
                sc.nextLine();
                System.out.println(rmiConnection.cancelProject(user,projects.get(choose)));*/

                /*//Cancelar um projecto
                ArrayList<Project> projects = rmiConnection.getMyProjects(user);
                for(int i=0;i<projects.size();i++)
                {
                    System.out.println("\n"+projects.get(i)+"\n\n");
                }
                System.out.println("Select project to delete [from 0 to " + (projects.size() - 1) + "]:");
                int choose = sc.nextInt();
                sc.nextLine();
                System.out.println(rmiConnection.cancelProject(user,projects.get(choose)));*/

                /*//Verificar as mensagens dos projectos
                ArrayList<Project> projects = rmiConnection.getMyProjects(user);
                for(int i=0;i<projects.size();i++)
                {
                    System.out.println("\n"+projects.get(i)+"\n\n");
                }
                System.out.println("Select project to see the messages [from 0 to " + (projects.size() - 1) + "]:");
                int choose = sc.nextInt();
                sc.nextLine();
                ArrayList<Message> messages = rmiConnection.getProjectMessages(projects.get(choose));
                System.out.println("\n\nMessage of project "+projects.get(choose).getProjectName());
                for(int i=0;i<messages.size();i++)
                {
                    System.out.println(messages.get(i));
                }*/

                /*//Mandar uma resposta a uma mensagem
                System.out.println("Select the message to reply [from 0 to " + (messages.size() - 1) + "]:");
                int msg = sc.nextInt();
                sc.nextLine();
                System.out.println("Message to reply:");
                String reply = sc.nextLine();
                System.out.println(rmiConnection.replyMessage(messages.get(msg),new Reply(reply,user)));*/

                /*//Verificar as mensagens para um projecto
                ArrayList<Project> projects = rmiConnection.actualProjects();
                for(int i=0;i<projects.size();i++)
                {
                    System.out.println("\n"+projects.get(i)+"\n\n");
                }
                System.out.println("Select project to Send a message [from 0 to " + (projects.size() - 1) + "]:");
                int choose = sc.nextInt();
                sc.nextLine();

                ArrayList<Message> messages = rmiConnection.getProjectMessages(projects.get(choose));
                System.out.println("\n\nMessage of project "+projects.get(choose).getProjectName());
                for(int i=0;i<messages.size();i++)
                {
                    System.out.println(messages.get(i));
                }*/

                /*//Mandar uma mensagem a um projecto
                System.out.println("Message:");
                String message = sc.nextLine();
                rmiConnection.sendMessage(new Message(message,projects.get(choose),user));*/


                /*//Projectos actuais e doação de dinheiro
                ArrayList<Project> projects = rmiConnection.actualProjects();
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
                boolean pass = rmiConnection.donateMoney(user,projects.get(choose),projects.get(choose).getProductTypes().get(vote),money);
                System.out.println(pass);*/

                /*//Projectos antigos
                ArrayList<Project> projects = rmiConnection.oldProjects();
                for(int i=0;i<projects.size();i++)
                {
                    System.out.println(projects.get(i).getProjectID() + "," + projects.get(i).getProjectName() + "," + projects.get(i).getDescription());
                }*/

                /*//Remove reward
                Project project = new Project();
                project.setProjectID(2);
                System.out.println(rmiConnection.removeReward(user,project,5));*/

                /*//Adiciona reward
                Reward reward = new Reward();
                System.out.println("Name:");
                reward.setName(sc.nextLine());
                System.out.println("Description:");
                reward.setDescription(sc.nextLine());
                System.out.println("Value of Reward:");
                reward.setValueOfReward(sc.nextInt());
                sc.nextLine();
                rmiConnection.addReward(user,project,reward);*/

                /*//Criar o projecto
                Project project = new Project();
                System.out.println(">>>Create Project<<<\nProject Name:");
                project.setProjectName(sc.nextLine());
                System.out.println("Description:");
                project.setDescription(sc.nextLine());
                System.out.println("Date Limit (Year Moth Day):");
                project.setDateLimit(new Date(sc.nextInt()-1900,sc.nextInt()-1,sc.nextInt()));
                sc.nextLine();
                System.out.println("Requested Value:");
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
                System.out.println("Creating project...");
                rmiConnection.createProject(user, project.getProjectName(), project.getDescription(), project.getDateLimit(), project.getRequestedValue(),project.getRewards(), project.getProductTypes());*/
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception:"+e);
        }
    }
}
