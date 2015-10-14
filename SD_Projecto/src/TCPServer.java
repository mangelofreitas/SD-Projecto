import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Maria Filipa on 12-10-2015.
 */

public class TCPServer {
    public static void main(String args[]) {

        int numero = 0;
        ArrayList<DataOutputStream> clientes = new ArrayList<DataOutputStream>();

        try {
            int serverPort = 6000;
            System.out.println("A Escuta no Porto 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET=" + listenSocket);
            while (true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                numero++;
                clientes.add(new DataOutputStream(clientSocket.getOutputStream()));
                new Connection(clientSocket, numero);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }
    }
}
class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int thread_number;

    public Connection(Socket aClientSocket, int numero) {
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run () {

        //System.out.println("Thread "+thread_number+" started! ");
        String resposta;
        try{
            while(true){
                String data = in.readUTF();
                System.out.println("T["+thread_number + "] Recebeu: "+data);
                resposta=data.toUpperCase();

                out.writeUTF(resposta);

            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}
    }

}



