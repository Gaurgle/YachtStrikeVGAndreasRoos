package ServerSide;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {
    public ServerListener() {

        AudioManager audioManager = AudioManager.getInstance();
//        audioManager.playThemeSong("play");
        final String serverUp = "The server is running!";
        final String welcome = "Welcome Player 1. Please wait for Player 2 to connect.";

        try (ServerSocket listener = new ServerSocket(23456)){
            System.out.println(serverUp);
            while (true) {
                Player player1 = new Player(listener.accept(), '1');
                player1.sendToClient(welcome); // <- denna rad är temporär kod för test, ska antagligen ersättas
                Player player2 = new Player(listener.accept(), '2');
                Game game = new Game(player1, player2);
                game.start();
            }
        } catch (IOException e) {
            System.err.println("In/Out stream error.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerListener sl = new ServerListener();
    }
}