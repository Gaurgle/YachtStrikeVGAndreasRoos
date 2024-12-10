package ServerSide;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Player {
    private Socket socket;
    private char playerNum;
    private Player opponent;
    private BufferedReader in;
    private PrintWriter out;

    public Player(Socket socket, char playerNum) {
        this.socket = socket;
        this.playerNum = playerNum;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String message){
        try {
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
                out.println(message);
                out.flush();
            } else {
                System.err.println("Socket disconnected.");
                closeConnection();
            }
        } catch (Exception e) { //TODO: fix exception
            e.printStackTrace();
            closeConnection();
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return opponent;
    }

    public char getPlayerNum() {
        return playerNum;
    }

    public String receieveFromClient(){
        String input = "";
        try {
            if (socket != null && !socket.isClosed() && socket.isConnected()) {
                input = in.readLine();
                if (input != null && input.equals("DISCONNECT")) {
                    System.out.println("Client has disconnected.");
                    closeConnection();
                }
            } else {
                System.err.println("Socket disconnected.");
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNullElse(input, "");
    }

    public void closeConnection() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket or in/out streams: " + e.getMessage());
        }
    }
}
