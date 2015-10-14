import java.net.*;
import java.io.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class TCPClient {

    public static void main(String args[]) {
        Socket s = null;
        int serversocket=6000;

        try {
            s = new Socket(args[0], serversocket);

            System.out.println("SOCKET=" + s);

            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String texto = "teste";
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);

            out.writeUTF(texto);

            System.out.println(in.readUTF());
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
}