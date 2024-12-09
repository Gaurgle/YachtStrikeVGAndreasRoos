package ClientSide;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public Client() {

        int portNumber = 23456;

        try (Socket socket = new Socket("localhost", portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            //Bara lite temporär kod för att testa att uppkopling funkar:
            String input;
            //while ((input = in.readLine()) != null) {
            while(true){
                if (in.ready()) {
                    input = in.readLine();
                    System.out.println(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
